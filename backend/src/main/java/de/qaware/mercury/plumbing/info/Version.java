package de.qaware.mercury.plumbing.info;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class Version {
    String commitHash;
    OffsetDateTime commitTime;
    boolean localChanges;
    String version;
}
