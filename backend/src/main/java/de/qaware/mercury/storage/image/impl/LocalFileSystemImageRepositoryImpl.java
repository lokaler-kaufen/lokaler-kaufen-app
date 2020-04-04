package de.qaware.mercury.storage.image.impl;

import de.qaware.mercury.image.Image;
import de.qaware.mercury.storage.image.ImageRepository;
import de.qaware.mercury.storage.image.ImageStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Component
@Slf4j
class LocalFileSystemImageRepositoryImpl implements ImageRepository {
    private static final Path STORAGE_LOCATION = Path.of("/tmp/");
    public static final String EXTENSION = ".jpeg";

    @Override
    public void store(Image image, InputStream data) {
        Path filename = generateFilename(image.getId()).toAbsolutePath();
        log.debug("Storing image {} to '{}' ...", image.getId(), filename);

        try (OutputStream stream = Files.newOutputStream(filename, StandardOpenOption.CREATE_NEW)) {
            data.transferTo(stream);
        } catch (IOException e) {
            throw new ImageStorageException(String.format("Failed to storage image to '%s'", filename), e);
        }

        log.debug("Stored image {}", image.getId());
    }

    private Path generateFilename(Image.Id id) {
        return STORAGE_LOCATION.resolve(id.getId().toString() + EXTENSION);
    }
}
