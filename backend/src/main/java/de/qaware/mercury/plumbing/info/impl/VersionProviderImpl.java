package de.qaware.mercury.plumbing.info.impl;

import de.qaware.mercury.plumbing.info.LoadVersionFileException;
import de.qaware.mercury.plumbing.info.Version;
import de.qaware.mercury.plumbing.info.VersionProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Properties;

@Service
@Slf4j
class VersionProviderImpl implements VersionProvider {
    private static final DateTimeFormatter COMMIT_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

    private static final String UNKNOWN_VALUE = "UNKNOWN";
    private static final OffsetDateTime UNKNOWN_COMMIT_TIME = OffsetDateTime.ofInstant(Instant.ofEpochSecond(0), ZoneOffset.UTC);
    private static final boolean UNKNOWN_LOCAL_CHANGES = false;
    private final Version version;

    public VersionProviderImpl() {
        version = loadVersion();
    }

    private Version loadVersion() {
        // The gradle-git-properties generates this property file
        try (InputStream stream = VersionProviderImpl.class.getResourceAsStream("/git.properties")) {
            if (stream == null) {
                log.warn("Unable to find version file, using default values");
                return new Version(UNKNOWN_VALUE, UNKNOWN_COMMIT_TIME, UNKNOWN_LOCAL_CHANGES, UNKNOWN_VALUE);
            }

            Properties properties = new Properties();
            properties.load(stream);

            return new Version(
                properties.getProperty("git.commit.id", UNKNOWN_VALUE),
                parseCommitTime(properties),
                parseLocalChanges(properties),
                properties.getProperty("git.build.version", UNKNOWN_VALUE)
            );
        } catch (IOException e) {
            throw new LoadVersionFileException("Failed to load /git.properties from classpath", e);
        }
    }

    private boolean parseLocalChanges(Properties properties) {
        String changes = properties.getProperty("git.dirty");
        if (changes == null) {
            return UNKNOWN_LOCAL_CHANGES;
        }

        return Boolean.parseBoolean(changes);
    }

    private OffsetDateTime parseCommitTime(Properties properties) {
        String commitTime = properties.getProperty("git.commit.time");
        if (commitTime == null) {
            return UNKNOWN_COMMIT_TIME;
        }

        try {
            return OffsetDateTime.parse(commitTime, COMMIT_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            log.warn("Failed to parse commit time, using default time", e);
            return UNKNOWN_COMMIT_TIME;
        }
    }

    @Override
    public Version getVersion() {
        return version;
    }
}
