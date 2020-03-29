package de.qaware.mercury.rest.util.cookie;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties("mercury.cookies")
@Validated
class CookieConfigurationProperties {
    private boolean secure;
}
