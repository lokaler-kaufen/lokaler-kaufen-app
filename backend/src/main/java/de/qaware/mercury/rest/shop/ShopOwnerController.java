package de.qaware.mercury.rest.shop;

import de.qaware.mercury.business.image.ImageService;
import de.qaware.mercury.business.location.impl.LocationNotFoundException;
import de.qaware.mercury.business.login.LoginException;
import de.qaware.mercury.business.reservation.Slot;
import de.qaware.mercury.business.reservation.SlotService;
import de.qaware.mercury.business.shop.Breaks;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.ShopService;
import de.qaware.mercury.business.shop.ShopUpdate;
import de.qaware.mercury.business.shop.SlotConfig;
import de.qaware.mercury.business.shop.SocialLinks;
import de.qaware.mercury.rest.plumbing.authentication.AuthenticationHelper;
import de.qaware.mercury.rest.shop.dto.request.BreaksDto;
import de.qaware.mercury.rest.shop.dto.request.UpdateShopDto;
import de.qaware.mercury.rest.shop.dto.response.ShopDetailDto;
import de.qaware.mercury.rest.shop.dto.response.ShopOwnerDetailDto;
import de.qaware.mercury.util.Maps;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@RequestMapping(value = "/api/shop", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ShopOwnerController {
    private final AuthenticationHelper authenticationHelper;
    private final ImageService imageService;
    private final ShopService shopService;
    private final SlotService slotService;

    /**
     * Retrieves settings of a shop for the owner.
     *
     * @param servletRequest an instance of {@link HttpServletRequest}.
     * @return an instance of {@link ShopOwnerDetailDto}.
     * @throws LoginException if the caller is not authenticated as a shop owner.
     */
    @GetMapping(path = "/me")
    public ShopOwnerDetailDto getShopSettings(HttpServletRequest servletRequest) throws LoginException {
        Shop shop = authenticationHelper.authenticateShop(servletRequest);
        List<Slot> slots = slotService.convertBreaksToSlots(shopService.findBreaks(shop));

        return ShopOwnerDetailDto.of(shop, BreaksDto.fromSlots(slots), imageService);
    }

    /**
     * Updates shop details.
     *
     * @param request        the update shop request.
     * @param servletRequest an instance of {@link HttpServletRequest}.
     * @return the updated shop as {@link ShopDetailDto}.
     * @throws LoginException if the caller is not authenticated to modify this shop.
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ShopOwnerDetailDto updateShop(@Valid @RequestBody UpdateShopDto request, HttpServletRequest servletRequest) throws LoginException, LocationNotFoundException {
        Shop shop = authenticationHelper.authenticateShop(servletRequest);

        SlotConfig slotConfig = request.getSlots().toSlots();
        Breaks breaks = request.getBreaks() == null ? Breaks.none() : slotService.resolveBreaks(request.getBreaks().getSlotIds(), slotConfig);
        Shop updatedShop = shopService.update(shop, new ShopUpdate(
            request.getName(),
            request.getOwnerName(),
            request.getStreet(),
            request.getZipCode(),
            request.getCity(),
            request.getAddressSupplement(),
            request.getDetails(),
            request.getWebsite(),
            Maps.mapKeys(request.getContacts(), ContactType::parse),
            slotConfig,
            request.getSocialLinks() == null ? SocialLinks.none() : request.getSocialLinks().toSocialLinks(),
            breaks
        ));
        List<Slot> slots = slotService.convertBreaksToSlots(breaks);

        return ShopOwnerDetailDto.of(updatedShop, BreaksDto.fromSlots(slots), imageService);
    }
}
