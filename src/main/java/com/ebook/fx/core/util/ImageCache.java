package com.ebook.fx.core.util;

import com.ebook.fx.core.command.LoadPdfCoverImageCommand;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;

/**
 *
 * @author maykoone
 */
@Singleton
public class ImageCache {

    private Map<String, Image> cachedImages;
    private ExecutorService imageLoadExecutor;

    public ImageCache() {
        this.cachedImages = new ConcurrentHashMap<>();
        this.imageLoadExecutor = Executors.newCachedThreadPool();
    }

    public Task<Image> get(String url) {
        Task<Image> task = new Task<Image>() {
            @Override
            protected Image call() throws Exception {
                return imageSupplier.apply(url);
            }
        };

        imageLoadExecutor.submit(task);
        return task;
    }

    public CompletableFuture<Image> getAsync(String url) {
        return CompletableFuture.supplyAsync(() -> imageSupplier.apply(url));
    }

    @PreDestroy
    public void clear() {
        cachedImages.clear();
        if (!imageLoadExecutor.isShutdown()) {
            imageLoadExecutor.shutdown();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        clear();
    }

    private Function<String, Image> imageSupplier = (String url) -> {
        Image image = cachedImages.get(url);
        if (image == null) {
            LoadPdfCoverImageCommand command = new LoadPdfCoverImageCommand(url);
            image = command.get();
            if (!Objects.equals(image, LoadPdfCoverImageCommand.defaultImage)) {
                cachedImages.put(url, image);
            }
        }
        return image;
    };

}
