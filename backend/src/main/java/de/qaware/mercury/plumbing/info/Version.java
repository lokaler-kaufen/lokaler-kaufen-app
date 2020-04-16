package de.qaware.mercury.plumbing.info;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
@SuppressWarnings("java:S1700") // Shut up SonarQube
public class Version {
    String commitHash;
    OffsetDateTime commitTime;
    boolean localChanges;
    String version;
}
