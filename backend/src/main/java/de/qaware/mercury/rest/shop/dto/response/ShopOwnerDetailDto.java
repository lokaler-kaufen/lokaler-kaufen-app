package de.qaware.mercury.rest.shop.dto.response;

import de.qaware.mercury.business.image.ImageService;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.rest.shop.dto.request.BreaksDto;
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
public class ShopOwnerDetailDto {
    private String id;
    private String name;
    private String ownerName;
    private String street;
    private String zipCode;
    private String city;
    private String addressSupplement;
    private String details;
    @Nullable
    private String imageUrl;
    @Nullable
    private String website;
    private Map<String, String> contacts;
    private SlotConfigDto slots;
    private SocialLinksDto socialLinks;
    private BreaksDto breaks;

    public static ShopOwnerDetailDto of(Shop shop, BreaksDto breaks, ImageService imageService) {
        URI imageUrl = imageService.generatePublicUrl(shop.getImageId());
        return new ShopOwnerDetailDto(
            shop.getId().getId().toString(),
            shop.getName(),
            shop.getOwnerName(),
            shop.getStreet(),
            shop.getZipCode(),
            shop.getCity(),
            shop.getAddressSupplement(),
            shop.getDetails(),
            Null.map(imageUrl, URI::toString),
            shop.getWebsite(),
            Maps.mapKeys(shop.getContacts(), ContactType::getId),
            Null.map(shop.getSlotConfig(), SlotConfigDto::of),
            SocialLinksDto.of(shop.getSocialLinks()),
            breaks
        );
    }
}
