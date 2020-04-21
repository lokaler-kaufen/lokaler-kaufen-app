package de.qaware.mercury.business.image.color.analysis.graphicsmagick.impl;

import de.qaware.mercury.business.image.ImageException;
import de.qaware.mercury.business.image.color.analysis.graphicsmagick.GMImageToTextConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class GMImageToTextConverterImpl implements GMImageToTextConverter {
    private static final int EXIT_CODE_SUCCESS = 0;

    @Override
    public String convertImageToText(InputStream image, int targetSize) {
        String result;
        try {
            result = convertImage(image, targetSize);
        } catch (IOException e) {
            throw new ImageException("Failed to convert image to text", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ImageException("Got interrupted while converting image", e);
        }
        return result;
    }

    private String convertImage(InputStream data, int targetSize) throws IOException, InterruptedException {
        String resolution = String.format("%dx%d", targetSize, targetSize);
        String[] args = {
            "gm", "convert", "-background", "white", "-extent", "0x0", "+matte", "-size", resolution, "-", "-resize", resolution, "-depth", "8", "txt:-"
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
            throw new ImageException(String.format("Failed to run process '%s', exit code: %d", commandLine, exitCode));
        }
        return inputStreamToString(process.getInputStream());
    }

    private String inputStreamToString(InputStream stream) throws IOException {
        return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    }
}
