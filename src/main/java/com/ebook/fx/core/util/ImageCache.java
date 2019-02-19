package com.ebook.fx.core.util;

import com.ebook.fx.core.command.LoadPdfCoverImageCommand;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;

/**
 *
 * @author maykoone
 */
@Singleton
public class ImageCache {

    private final Map<String, Image> cachedImages;
    private static final Logger LOGGER = Logger.getLogger(ImageCache.class.getName());

    public ImageCache() {
        this.cachedImages = new ConcurrentHashMap<>();
    }

    public CompletableFuture<Image> getAsync(String url) {
        return CompletableFuture
                .supplyAsync(() -> cachedImages.computeIfAbsent(url, k -> new LoadPdfCoverImageCommand(k).get()))
                .exceptionally(ex -> {
                    LOGGER.log(Level.INFO, "Returning default image due to fail to load. Details: {}", ex.getMessage());
                    return LoadPdfCoverImageCommand.defaultImage;
                });
    }

    @PreDestroy
    public void clear() {
        Logger.getLogger(ImageCache.class.getName()).info("Destroy imagecache.");
        cachedImages.clear();
    }
}
