/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebook.fx.util;

import com.ebook.fx.command.LoadPdfCoverImageCommand;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

/**
 *
 * @author maykoone
 */
public class ImageCache {

    private Map<String, Image> cachedImages;
    private static ImageCache instance = new ImageCache();
    private ExecutorService imageLoadExecutor;

    public ImageCache() {
        this.cachedImages = new ConcurrentHashMap<>();
        this.imageLoadExecutor = Executors.newCachedThreadPool();
    }

    public Task<Image> get(String url) {
        Task<Image> task = new Task<Image>() {
            @Override
            protected Image call() throws Exception {
                Image image = cachedImages.get(url);
                if (image == null) {
                    LoadPdfCoverImageCommand command = new LoadPdfCoverImageCommand(url);
                    image = command.get();
                    if (!Objects.equals(image, LoadPdfCoverImageCommand.defaultImage)) {
                        cachedImages.put(url, image);
                    }
                }
                return image;
            }
        };

        imageLoadExecutor.submit(task);
        return task;
    }

    public CompletableFuture<Image> getAsync(String url) {
        return CompletableFuture.supplyAsync(() -> {
            Image image = cachedImages.get(url);
            if (image == null) {
                LoadPdfCoverImageCommand command = new LoadPdfCoverImageCommand(url);
                image = command.get();
                if (!Objects.equals(image, LoadPdfCoverImageCommand.defaultImage)) {
                    cachedImages.put(url, image);
                }
            }
            return image;
        });
    }

    public static ImageCache getInstance() {
        return instance;
    }

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

}
