package de.qaware.mercury.rest.shop.dto.response;

import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.ShopService;
import de.qaware.mercury.rest.shop.dto.request.SlotConfigDto;
import de.qaware.mercury.rest.shop.dto.requestresponse.SocialLinksDto;
import de.qaware.mercury.util.Maps;
import de.qaware.mercury.util.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopAdminDto {
    private String id;
    private String name;
    private String ownerName;
    private String email;
    private String street;
    private String zipCode;
    private String city;
    private String addressSupplement;
    private Map<String, String> contacts;
    private boolean enabled;
    private boolean approved;
    private String details;
    @Nullable
    private String imageUrl;
    @Nullable
    private String website;
    private boolean autoColorEnabled;
    private SlotConfigDto slots;
    private SocialLinksDto socialLinks;
    private String shareLink;

    public static ShopAdminDto of(Shop shop, ShopService shopService) {
        return new ShopAdminDto(
            shop.getId().getId().toString(),
            shop.getName(),
            shop.getOwnerName(),
            shop.getEmail(),
            shop.getStreet(),
            shop.getZipCode(),
            shop.getCity(),
            shop.getAddressSupplement(),
            Maps.mapKeys(shop.getContacts(), ContactType::getId),
            shop.isEnabled(),
            shop.isApproved(),
            shop.getDetails(),
            Null.map(shopService.generateImageUrl(shop), URI::toString),
            shop.getWebsite(),
            shop.isAutoColorEnabled(),
            Null.map(shop.getSlotConfig(), SlotConfigDto::of),
            SocialLinksDto.of(shop.getSocialLinks()),
            shopService.generateShareLink(shop).toString()
        );
    }
}
