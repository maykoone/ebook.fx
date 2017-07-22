package com.ebook.fx.core.task;

import com.ebook.fx.core.model.Book;
import com.sun.pdfview.OutlineNode;
import com.sun.pdfview.PDFFile;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author maykoone
 */
public class ImportFileTask extends Task<ObservableList<Book>> {

    private final File sourceFile;
    private final AtomicLong progressCount = new AtomicLong();
    private long numberOfFiles;
    private Pattern ptrn = Pattern.compile("([^\\s]+(\\.(?i)(pdf))$)");
    private Stream<File> filesToImport;

    public ImportFileTask(File sourceFile) {
        this(sourceFile, Function.identity());
    }

    public ImportFileTask(File sourceFile, Function<Book, Book> save) {
        this.sourceFile = sourceFile;
        this.save = save;
        prepare();
    }
    
    public ImportFileTask(List<File> files, Function<Book, Book> save){
        this.save = save;
        this.numberOfFiles = files.size();
        this.filesToImport = files.stream();
        this.sourceFile = null;
    }

    @Override
    protected ObservableList<Book> call() throws Exception {
        return filesToImport
                .map(this::convertToBook)
                .map(save)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
    private Function<Book, Book> save;

    private Book convertToBook(File file) {
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            FileChannel fc = raf.getChannel();
            ByteBuffer buf = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

            PDFFile pdf = new PDFFile(buf);
            Book book
                    = new Book(Optional.ofNullable(pdf.getStringMetadata("Title"))
                                .filter(s -> !s.isEmpty())
                                .orElse(file.getName().replace(".pdf", "")),
                            pdf.getStringMetadata("Author"));

            book.setFileLength((double) file.length() / 1048576);
            book.setPages(pdf.getNumPages());
            book.setFilePath(file.getAbsolutePath());
            book.setTags(parseTags(pdf.getStringMetadata("Keywords")));
            book.setAdded(LocalDate.now());

            OutlineNode outline = pdf.getOutline();
            if (outline != null) {
                Enumeration e = outline.preorderEnumeration();
                while (e.hasMoreElements()) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
                    if (node.getLevel() == 1) {
                        String content = node.toString();
                        book.getContents().add(content.substring(0, Integer.min(content.length(), 60)));
                    }
                }
            }

            updateProgress(progressCount.incrementAndGet(), numberOfFiles);
            fc.close();
            return book;
        } catch (Exception e) {
//            System.out.println("Falha ao importar pdf " + file.getName());
            Book book = new Book(file.getName().replace(".pdf", ""), "");
            book.setFilePath(file.getAbsolutePath());
            book.setFileLength((double) file.length() / 1048576);
            return book;
        }
    }

    private boolean filterFile(File file) {
//        return ptrn.matcher(file.getName()).matches();
        return file.getName().toLowerCase().endsWith(".pdf");
    }

    private void prepare() {
        if (sourceFile.isDirectory()) {
            File[] listFiles = sourceFile.listFiles(this::filterFile);
            numberOfFiles = listFiles.length;
            filesToImport = Stream.of(listFiles);
        } else {
            numberOfFiles = 1;
            filesToImport = Stream.of(sourceFile);
        }
    }

    public long getNumberOfFiles() {
        return numberOfFiles;
    }

    private List<String> parseTags(String keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return null;
        }
        String[] tags = keywords.split(",");
        return Stream.of(tags).map(t -> t.trim()).collect(Collectors.toList());
    }

}
