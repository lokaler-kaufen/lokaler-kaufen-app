package de.qaware.mercury.rest.shop.dto.response;

import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.ShopService;
import de.qaware.mercury.rest.shop.dto.requestresponse.SocialLinksDto;
import de.qaware.mercury.util.Null;
import de.qaware.mercury.util.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopDetailDto {
    private String id;
    private String name;
    private String ownerName;
    private String email;
    private String street;
    private String zipCode;
    private String city;
    private String addressSupplement;
    private Set<String> contactTypes;
    private String details;
    @Nullable
    private String imageUrl;
    @Nullable
    private String shopColor;
    private boolean autoColorEnabled;
    @Nullable
    private String website;
    private SocialLinksDto socialLinks;
    private String shareLink;

    public static ShopDetailDto of(Shop shop, ShopService shopService) {
        return new ShopDetailDto(
            shop.getId().getId().toString(),
            shop.getName(),
            shop.getOwnerName(),
            shop.getEmail(),
            shop.getStreet(),
            shop.getZipCode(),
            shop.getCity(),
            shop.getAddressSupplement(),
            Sets.map(shop.getContacts().keySet(), ContactType::getId),
            shop.getDetails(),
            Null.map(shopService.generateImageUrl(shop), URI::toString),
            shop.getShopColor(),
            shop.isAutoColorEnabled(),
            shop.getWebsite(),
            SocialLinksDto.of(shop.getSocialLinks()),
            shopService.generateShareLink(shop).toString()
        );
    }
}
