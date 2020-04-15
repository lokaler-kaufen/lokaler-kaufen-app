package de.qaware.mercury.business.shop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@ConfigurationProperties("mercury.sharing")
@Validated
public class ShopSharingConfigurationProperties {
    /**
     * URL to the shop detail page in the frontend.
     * <p>
     * {{ id }} is replaced with the id of the shop.
     */
    @NotBlank
    private String shopDetailLinkTemplate;

    /**
     * URL which can be shared by shops, with a pretty format (no UUIDs in there).
     * <p>
     * {{ slug }} is replaced with the slug of the shop.
     */
    @NotBlank
    private String shopShareLinkTemplate;
}
