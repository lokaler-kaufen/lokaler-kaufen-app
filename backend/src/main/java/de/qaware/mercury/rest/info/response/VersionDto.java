package de.qaware.mercury.rest.info.response;

import de.qaware.mercury.plumbing.info.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersionDto {
    String commitHash;
    String commitTime;
    boolean localChanges;
    String version;

    public static VersionDto of(Version version) {
        return new VersionDto(
            version.getCommitHash(), version.getCommitTime().toString(), version.isLocalChanges(), version.getVersion()
        );
    }
}
