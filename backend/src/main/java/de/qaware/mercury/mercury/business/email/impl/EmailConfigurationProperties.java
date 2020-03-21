package de.qaware.mercury.mercury.business.email.impl;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@ConfigurationProperties("mercury.email")
@Validated
class EmailConfigurationProperties {
    @NotBlank
    private String from;
    /**
     * {{ token }} will be replaced with the creation link token
     */
    @NotBlank
    private String creationLinkTemplate;
}
