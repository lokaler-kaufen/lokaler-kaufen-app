package de.qaware.mercury.mercury.business.email.impl;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@ConfigurationProperties("mercury.email")
@Validated
class EmailSenderConfigurationProperties {
    @NotBlank
    private String from;
}
