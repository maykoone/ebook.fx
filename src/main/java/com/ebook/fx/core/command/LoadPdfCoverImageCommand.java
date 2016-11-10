/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebook.fx.core.command;

import com.ebook.fx.MainApp;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Transparency;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author maykoone
 */
public class LoadPdfCoverImageCommand {

    private File pdfFile;

    public static final Image defaultImage;

    static {
        Image image;
        try {
            image = new Image(MainApp.class.getResource("/cover.png").toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
            image = null;
        }
        defaultImage = image;
    }

    public LoadPdfCoverImageCommand(File pdfFile) {
        this.pdfFile = pdfFile;
    }

    public LoadPdfCoverImageCommand(String url) {
        this.pdfFile = new File(url);
    }

    public Image get() {
        try {
            RandomAccessFile raf = new RandomAccessFile(pdfFile, "r");
            FileChannel fc = raf.getChannel();
            ByteBuffer buf = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

            PDFFile pdf = new PDFFile(buf);
            PDFPage page = pdf.getPage(1);
            Rectangle2D bBox = page.getBBox();

            double width = Math.abs(bBox.getWidth());
            double height = Math.abs(bBox.getHeight());

            java.awt.Image image = page.getImage((int) width, (int) height, bBox, null, true, true);
            return SwingFXUtils.toFXImage(toBufferedImage(image), null);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultImage;
        }

    }

    private BufferedImage toBufferedImage(java.awt.Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent
        // Pixels
        boolean hasAlpha = hasAlpha(image);
        // Create a buffered image with a format that's compatible with the
        // screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }
            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    private boolean hasAlpha(java.awt.Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage) image;
            return bimage.getColorModel().hasAlpha();
        }
        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }
        // Get the image's color model
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }
}
