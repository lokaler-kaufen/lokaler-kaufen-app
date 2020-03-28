package de.qaware.mercury.rest.info;

import de.qaware.mercury.plumbing.info.VersionProvider;
import de.qaware.mercury.rest.info.response.VersionDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/info", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class InfoRestController {
    private final VersionProvider versionProvider;

    @GetMapping("/version")
    public VersionDto version() {
        return VersionDto.of(versionProvider.getVersion());
    }
}
