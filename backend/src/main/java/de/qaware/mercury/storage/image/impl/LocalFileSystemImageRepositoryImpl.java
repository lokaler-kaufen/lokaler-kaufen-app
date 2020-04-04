package de.qaware.mercury.storage.image.impl;

import de.qaware.mercury.image.Image;
import de.qaware.mercury.image.ImageConfigurationProperties;
import de.qaware.mercury.storage.image.ImageRepository;
import de.qaware.mercury.storage.image.ImageStorageException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
@EnableConfigurationProperties(ImageConfigurationProperties.class)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class LocalFileSystemImageRepositoryImpl implements ImageRepository {
    private final ImageConfigurationProperties configuration;
    private final AtomicBoolean storageDirectoryCreated = new AtomicBoolean(false);

    @Override
    public void store(Image.Id imageId, String filename, InputStream data) {
        createStorageDirectory();

        Path path = configuration.getStorageLocationAsPath().resolve(filename).toAbsolutePath();
        log.debug("Storing image {} to '{}' ...", imageId, path);

        try (OutputStream stream = Files.newOutputStream(path, StandardOpenOption.CREATE_NEW)) {
            long transferred = data.transferTo(stream);
            log.debug("Stored image {} ({} bytes)", imageId, transferred);
        } catch (IOException e) {
            throw new ImageStorageException(String.format("Failed to storage image %s to '%s'", imageId, path), e);
        }
    }

    private void createStorageDirectory() {
        // Just do that once
        if (storageDirectoryCreated.compareAndSet(false, true)) {
            Path path = configuration.getStorageLocationAsPath().toAbsolutePath();
            log.debug("Creating storage directory '{}'", path);
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new ImageStorageException(String.format("Failed to create directory '%s'", path), e);
            }
        }
    }
}
