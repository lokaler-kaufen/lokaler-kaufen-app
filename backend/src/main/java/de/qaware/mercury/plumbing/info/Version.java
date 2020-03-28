package de.qaware.mercury.plumbing.info;

import lombok.Value;

import java.time.Instant;

@Value
public class Version {
    String commitHash;
    Instant commitTime;
    boolean localChanges;
    String version;
}
