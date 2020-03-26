package de.qaware.mercury.business.shop.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "mercury.features")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
class ShopServiceConfigurationProperties {
    /**
     * Determines if a shop is approved on creation (true) or if an admin has to approve id (false).
     */
    private boolean approveShopsOnCreation;
}
