package de.qaware.mercury.rest.shop


import de.qaware.mercury.business.shop.Breaks
import de.qaware.mercury.business.shop.Shop
import de.qaware.mercury.business.shop.ShopService
import de.qaware.mercury.rest.plumbing.authentication.AuthenticationHelper
import de.qaware.mercury.rest.shop.dto.request.SlotConfigDto
import de.qaware.mercury.rest.shop.dto.request.UpdateShopDto
import de.qaware.mercury.rest.shop.dto.requestresponse.BreaksDto
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
    ShopService shopService = Mock(ShopService)
    HttpServletRequest httpServletRequest = Mock(HttpServletRequest)

    void setup() {
        controller = new ShopOwnerController(authenticationHelper, shopService)
    }

    def "Retrieve shop settings"() {
        given:
        Shop shop = ShopFixtures.create()
        String testImageUrl = "http://image.url/path"
        authenticationHelper.authenticateShop(httpServletRequest) >> shop
        shopService.findBreaks(shop) >> Breaks.none()
        shopService.generateImageUrl(shop) >> new URI(testImageUrl)
        shopService.generateShareLink(shop) >> new URI("http://share.url/path")

        when:
        ShopOwnerDetailDto result = controller.getShopSettings(httpServletRequest)

        then:
        result
        result.id == shop.id.toString()
        result.imageUrl == testImageUrl
        result.shareLink == "http://share.url/path"
    }

    def "Shop gets updated"() {
        given:
        Shop shop = ShopFixtures.create()
        SlotConfigDto slots = SlotConfigDto.of(shop.getSlotConfig())
        UpdateShopDto dto = new UpdateShopDto("name", "ownername", "street", "zipCode", "city", "addressSupplement", "details", "www.example.com", true, Map.of(), slots, new SocialLinksDto("instagram", "facebook", "twitter"), BreaksDto.of(Breaks.none()))
        shopService.findBreaks(shop) >> Breaks.none()
        String testImageUrl = "http://image.url/path"
        shopService.generateImageUrl(shop) >> new URI(testImageUrl)
        shopService.generateShareLink(shop) >> new URI("http://share.url/path")

        when:
        ShopOwnerDetailDto result = controller.updateShop(dto, httpServletRequest)

        then:
        result
        result.id == shop.id.toString()
        result.imageUrl == testImageUrl
        result.shareLink == "http://share.url/path"

        1 * authenticationHelper.authenticateShop(httpServletRequest)
        1 * shopService.update(_, _) >> shop
    }
}
