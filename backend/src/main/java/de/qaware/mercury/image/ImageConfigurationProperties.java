package de.qaware.mercury.image;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.nio.file.Path;

@Data
@Validated
@ConfigurationProperties("mercury.images")
public class ImageConfigurationProperties {
    @NotBlank
    private String storageLocation;
    /**
     * {{ filename }} is replaced with the image file name.
     */
    @NotBlank
    private String publicUrlTemplate;

    public Path getStorageLocationAsPath() {
        return Path.of(storageLocation);
    }
}
