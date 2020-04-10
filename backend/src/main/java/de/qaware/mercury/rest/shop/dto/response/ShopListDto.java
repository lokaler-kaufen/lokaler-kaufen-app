package de.qaware.mercury.rest.shop.dto.response;

import de.qaware.mercury.business.image.ImageService;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.ShopWithDistance;
import de.qaware.mercury.util.Lists;
import de.qaware.mercury.util.Null;
import de.qaware.mercury.util.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopListDto {
    private List<ShopListEntryDto> shops;

    public static ShopListDto of(List<ShopWithDistance> shops, ImageService imageService) {
        return new ShopListDto(Lists.map(shops, shop -> ShopListEntryDto.of(shop, imageService)));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShopListEntryDto {
        private String id;
        private String name;
        private double distance;
        @Nullable
        private String imageUrl;
        private Set<String> supportedContactTypes;

        public static ShopListEntryDto of(ShopWithDistance shopListEntry, ImageService imageService) {
            URI imageUrl = imageService.generatePublicUrl(shopListEntry.getShop().getImageId());
            return new ShopListEntryDto(
                shopListEntry.getShop().getId().getId().toString(),
                shopListEntry.getShop().getName(),
                shopListEntry.getDistance(),
                Null.map(imageUrl, URI::toString),
                Sets.map(shopListEntry.getShop().getContacts().keySet(), ContactType::getId)
            );
        }
    }
}
