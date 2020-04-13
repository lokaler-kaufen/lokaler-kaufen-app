package de.qaware.mercury.rest.shop

import de.qaware.mercury.business.image.ImageService
import de.qaware.mercury.business.reservation.SlotService
import de.qaware.mercury.business.shop.Breaks
import de.qaware.mercury.business.shop.Shop
import de.qaware.mercury.business.shop.ShopService
import de.qaware.mercury.rest.plumbing.authentication.AuthenticationHelper
import de.qaware.mercury.rest.shop.dto.request.SlotConfigDto
import de.qaware.mercury.rest.shop.dto.request.UpdateShopDto
import de.qaware.mercury.rest.shop.dto.requestresponse.SocialLinksDto
import de.qaware.mercury.rest.shop.dto.response.ShopOwnerDetailDto
import de.qaware.mercury.test.fixtures.ShopFixtures
import spock.lang.Specification
import spock.lang.Subject

import javax.servlet.http.HttpServletRequest

class ShopOwnerControllerTest extends Specification {
    @Subject
    ShopOwnerController controller

    AuthenticationHelper authenticationHelper = Mock(AuthenticationHelper)
    ImageService imageService = Mock(ImageService)
    ShopService shopService = Mock(ShopService)
    SlotService slotService = Mock(SlotService)
    HttpServletRequest httpServletRequest = Mock(HttpServletRequest)

    void setup() {
        controller = new ShopOwnerController(authenticationHelper, imageService, shopService, slotService)
    }

    def "Retrieve shop settings"() {
        setup:
        Shop shop = ShopFixtures.create()
        String testImageUrl = "http://image.url/path"
        URI testImageUri = new URI(testImageUrl)

        when:
        ShopOwnerDetailDto result = controller.getShopSettings(httpServletRequest)

        then:
        result
        result.id == shop.id.toString()
        result.imageUrl == testImageUrl

        1 * authenticationHelper.authenticateShop(httpServletRequest) >> shop
        1 * imageService.generatePublicUrl(shop.imageId) >> testImageUri
        shopService.findBreaks(shop) >> Breaks.none()
        slotService.convertBreaksToSlots(_) >> List.of()
    }

    def "Shop gets updated"() {
        setup:
        Shop shop = ShopFixtures.create()
        SlotConfigDto slots = SlotConfigDto.of(shop.getSlotConfig())
        UpdateShopDto dto = new UpdateShopDto("name", "ownername", "street", "zipCode", "city", "addressSupplement", "details", "www.example.com", Map.of(), slots, new SocialLinksDto("instagram", "facebook", "twitter"), null)

        String testImageUrl = "http://image.url/path"
        URI testImageUri = new URI(testImageUrl)

        when:
        ShopOwnerDetailDto result = controller.updateShop(dto, httpServletRequest)

        then:
        result
        result.id == shop.id.toString()
        result.imageUrl == testImageUrl

        1 * authenticationHelper.authenticateShop(httpServletRequest)
        1 * shopService.update(_, _) >> shop
        1 * imageService.generatePublicUrl(shop.imageId) >> testImageUri
        shopService.findBreaks(shop) >> Breaks.none()
        slotService.convertBreaksToSlots(_) >> List.of()
    }
}
