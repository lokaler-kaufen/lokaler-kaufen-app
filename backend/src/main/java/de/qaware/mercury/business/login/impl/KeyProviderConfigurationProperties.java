package de.qaware.mercury.business.login.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "mercury.tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
class KeyProviderConfigurationProperties {
    @NotBlank
    private String masterKey;
}
