package de.qaware.mercury.rest.image;

import de.qaware.mercury.business.image.Image;
import de.qaware.mercury.business.image.ImageFormat;
import de.qaware.mercury.business.image.ImageNotFoundException;
import de.qaware.mercury.business.image.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

/**
 * Can be used while developing.
 * <p>
 * When requesting an URL like
 * <p>
 * GET http://localhost:8080/dev/image/fbd52c31-235f-482d-828f-4fa875b1791e.jpeg
 * <p>
 * this controller will load the image with id fbd52c31-235f-482d-828f-4fa875b1791e and serves it. This is useful when
 * developing. In production you would want to serve the static files with a real web server.
 * <p>
 * To enable this controller, set mercury.images.devControllerEnabled = true in the configuration.
 */
@RestController
@RequestMapping("/dev/image")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@ConditionalOnProperty(name = "mercury.images.dev-controller-enabled", havingValue = "true")
public class DevImageController {
    private final ImageService imageService;

    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> loadImage(@PathVariable String filename) throws ImageNotFoundException, IOException {
        int dot = filename.lastIndexOf('.');
        Image.Id imageId;
        if (dot == -1) {
            // No extension, take the whole filename as id
            imageId = Image.Id.parse(filename);
        } else {
            // Strip extension and take this as id
            imageId = Image.Id.parse(filename.substring(0, dot));
        }

        try (InputStream image = imageService.loadImage(imageId)) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", getContentType(imageService.getImageFormat()));
            return new ResponseEntity<>(image.readAllBytes(), headers, HttpStatus.OK);
        }
    }

    private String getContentType(ImageFormat imageFormat) {
        switch (imageFormat) {
            case JPEG:
                return "image/jpeg";
            case PNG:
                return "image/png";
            default:
                throw new IllegalStateException("Unexpected value: " + imageFormat);
        }
    }
}
