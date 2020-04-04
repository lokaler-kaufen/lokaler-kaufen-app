package de.qaware.mercury.image.impl;

import de.qaware.mercury.image.Image;
import de.qaware.mercury.image.ImageException;
import de.qaware.mercury.image.ImageFormat;
import de.qaware.mercury.image.ImageScaler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Uses ImageMagick to resize and convert the images.
 */
@Service
@Slf4j
class IMImageScalerImpl implements ImageScaler {
    private static final int EXIT_CODE_SUCCESS = 0;

    private final Lock tempDirectoryLock = new ReentrantLock();
    @Nullable
    private Path tempDirectory;

    @Override
    public InputStream scale(Image.Id imageId, InputStream data, int maxSize, ImageFormat format, int quality) {
        try {
            return scaleImpl(imageId, data, maxSize, format, quality);
        } catch (IOException e) {
            throw new ImageException(String.format("Failed to scale image '%s'", imageId), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ImageException(String.format("Got interrupted while scaling image '%s'", imageId), e);
        }
    }

    private InputStream scaleImpl(Image.Id imageId, InputStream data, int maxSize, ImageFormat format, int quality) throws IOException, InterruptedException {
        Path tempDirectory = getTempDirectory();

        Path inputImage = tempDirectory.resolve("input-" + imageId.getId()).toAbsolutePath();
        Path outputImage = tempDirectory.resolve("output-" + imageId.getId() + format.getExtension()).toAbsolutePath();

        // Copy input stream to inputImage
        try (OutputStream stream = Files.newOutputStream(inputImage)) {
            long transferred = data.transferTo(stream);
            log.debug("Input image size: {} bytes", transferred);
        }

        // convert [input] -resize WxH -quality [quality] [output]
        String[] args = {
            "convert", inputImage.toString(), "-resize", String.format("%dx%d", maxSize, maxSize), "-quality", Integer.toString(quality), outputImage.toString()
        };

        String commandLine = Arrays.toString(args);
        log.debug("Starting process '{}'", commandLine);
        Process process = new ProcessBuilder(args).start();

        int exitCode = process.waitFor();
        log.debug("Process exited with code {}", exitCode);

        // We now can delete the input image
        // We can't delete the output image right now, as we need an input stream on it
        Files.deleteIfExists(inputImage);

        if (exitCode != EXIT_CODE_SUCCESS) {
            throw new ImageException(String.format("Failed to run process '%s', exit code: %d", commandLine, exitCode));
        }

        // TODO: Delete output image when this stream gets closed
        return Files.newInputStream(outputImage);
    }

    private Path getTempDirectory() throws IOException, InterruptedException {
        tempDirectoryLock.lockInterruptibly();
        try {
            if (tempDirectory == null) {
                // Only create one temp directory. This directory is automatically removed on JVM stop.
                // TODO: Make sure this is really deleted
                tempDirectory = Files.createTempDirectory("mercury-images");
            }

            return tempDirectory;
        } finally {
            tempDirectoryLock.unlock();
        }
    }
}
