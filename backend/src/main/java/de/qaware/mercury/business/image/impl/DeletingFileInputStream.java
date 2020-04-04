package de.qaware.mercury.business.image.impl;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A {@link FileInputStream} which deletes the file it reads when it gets closed.
 */
@Slf4j
class DeletingFileInputStream extends FileInputStream {
    private Path file;

    public DeletingFileInputStream(Path file) throws FileNotFoundException {
        super(file.toFile());
        this.file = file.toAbsolutePath();
    }

    @Override
    public void close() throws IOException {
        // Close all handles
        super.close();

        log.debug("Deleting file {}", file);
        Files.deleteIfExists(file);
    }
}
