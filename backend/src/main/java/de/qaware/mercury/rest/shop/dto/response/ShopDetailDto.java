package de.qaware.mercury.rest.shop.dto.response;

import de.qaware.mercury.business.image.ImageService;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.util.Null;
import de.qaware.mercury.util.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopDetailDto {
    String id;
    String name;
    String ownerName;
    String email;
    String street;
    String zipCode;
    String city;
    String addressSupplement;
    Set<String> contactTypes;
    String details;
    @With
    @Nullable
    String imageUrl;
    @Nullable
    String website;

    public static ShopDetailDto of(Shop shop, ImageService imageService) {
        URI imageUrl = imageService.generatePublicUrl(shop.getImageId());
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
            Null.map(imageUrl, URI::toString),
            shop.getWebsite()
        );
    }
}
