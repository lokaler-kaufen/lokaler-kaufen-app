package de.qaware.mercury.business.image.impl;

import de.qaware.mercury.business.image.Image;
import de.qaware.mercury.business.image.ImageException;
import de.qaware.mercury.business.image.ImageFormat;
import de.qaware.mercury.business.image.ImageScaler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Uses GraphicsMagick to resize and convert the images.
 */
@Service
@Slf4j
class GMImageScalerImpl implements ImageScaler {
    private static final int EXIT_CODE_SUCCESS = 0;

    @Override
    public InputStream scale(Image.Id imageId, InputStream data, int maxSize, ImageFormat format, int quality) {
        try {
            return scaleImpl(data, maxSize, format, quality);
        } catch (IOException e) {
            throw new ImageException(String.format("Failed to scale image '%s'", imageId), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ImageException(String.format("Got interrupted while scaling image '%s'", imageId), e);
        }
    }

    private InputStream scaleImpl(InputStream data, int maxSize, ImageFormat format, int quality) throws IOException, InterruptedException {
        Path outputImage = Files.createTempFile("mercury-image-", format.getExtension()).toAbsolutePath();

        // See http://www.graphicsmagick.org/convert.html
        // See https://superuser.com/questions/213336/using-graphicsmagick-or-imagemagick-how-do-i-replace-transparency-with-a-fill-c
        String resolution = String.format("%dx%d", maxSize, maxSize);
        String[] args = {
            "gm", "convert", "-background", "white", "-extent", "0x0", "+matte", "-size", resolution, "-", "-resize", resolution, "-quality", Integer.toString(quality), "+profile", "*", outputImage.toString()
        };

        // Start GraphicsMagick
        String commandLine = String.join(" ", args);
        log.debug("Starting process '{}'", commandLine);
        Process process = new ProcessBuilder(args).start();

        // We pipe the data stream directly into the stdin of GraphicsMagick
        log.debug("Piping image to GraphicsMagick ...");
        // process.getOutputStream() returns the stdin of the process, wtf
        long transferred = data.transferTo(process.getOutputStream());
        process.getOutputStream().close();
        log.debug("Done, piped {} bytes", transferred);

        // Now wait for GraphicsMagick to quit
        log.debug("Waiting for GraphicsMagick to exit ...");
        int exitCode = process.waitFor();
        log.debug("GraphicsMagick exited with code {}", exitCode);

        if (exitCode != EXIT_CODE_SUCCESS) {
            Files.deleteIfExists(outputImage);
            throw new ImageException(String.format("Failed to run process '%s', exit code: %d", commandLine, exitCode));
        }

        // This input stream makes sure that the file gets deleted when the stream is closed
        return new DeletingFileInputStream(outputImage);
    }
}
