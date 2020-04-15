package de.qaware.mercury.rest.shop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties("mercury.redirect")
@Validated
class ShopRedirectConfigurationProperties {
    /**
     * {{ id }} is replaced with the id of the shop.
     */
    private String shopDetailLinkTemplate;
}
