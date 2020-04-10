package de.qaware.mercury.rest.shop

import de.qaware.mercury.business.image.Image
import de.qaware.mercury.business.image.ImageService
import de.qaware.mercury.business.login.ShopLoginService
import de.qaware.mercury.business.login.TokenService
import de.qaware.mercury.business.shop.Shop
import de.qaware.mercury.business.shop.ShopNotFoundException
import de.qaware.mercury.business.shop.ShopService
import de.qaware.mercury.business.shop.ShopWithDistance
import de.qaware.mercury.rest.shop.dto.request.CreateShopDto
import de.qaware.mercury.rest.shop.dto.request.ResetPasswordDto
import de.qaware.mercury.rest.shop.dto.request.SendCreateLinkDto
import de.qaware.mercury.rest.shop.dto.request.SendPasswordResetLinkDto
import de.qaware.mercury.rest.shop.dto.request.SlotConfigDto
import de.qaware.mercury.rest.shop.dto.requestresponse.SocialLinksDto
import de.qaware.mercury.rest.shop.dto.response.ShopDetailDto
import de.qaware.mercury.rest.shop.dto.response.ShopListDto
import de.qaware.mercury.rest.util.cookie.CookieHelper
import de.qaware.mercury.test.fixtures.ShopFixtures
import spock.lang.Specification
import spock.lang.Subject

import javax.servlet.http.HttpServletResponse

class ShopControllerSpec extends Specification {

    @Subject
    ShopController controller

    ShopService shopService = Mock(ShopService)
    TokenService tokenService = Mock(TokenService)
    ShopLoginService shopLoginService = Mock(ShopLoginService)
    ImageService imageService = Mock(ImageService)

    HttpServletResponse httpServletResponse = Mock(HttpServletResponse)
    CookieHelper cookieHelper = Mock(CookieHelper)

    void setup() {
        controller = new ShopController(shopService, imageService, tokenService, shopLoginService, cookieHelper)
    }

    def "Retrieve shop details"() {
        setup:
        Shop shop = ShopFixtures.create()
        String testImageUrl = "http://image.url/path"
        URI testImageUri = new URI(testImageUrl)

        when:
        ShopDetailDto result = controller.getShopDetails(shop.id.toString())

        then:
        result
        result.id == shop.id.toString()
        result.imageUrl == testImageUrl

        1 * shopService.findById(Shop.Id.parse(shop.id.toString())) >> shop
        1 * imageService.generatePublicUrl(shop.imageId) >> testImageUri
    }

    def "getShopDetails throws ShopNotFoundException if shop is not found"() {
        setup:
        String id = UUID.randomUUID()
        shopService.findById(Shop.Id.parse(id)) > null

        when:
        controller.getShopDetails(id)

        then:
        thrown ShopNotFoundException
    }

    def "sendCreateLink calls shopService"() {
        setup:
        String testEmail = "info@example.com"
        SendCreateLinkDto dto = new SendCreateLinkDto(testEmail)

        when:
        controller.sendCreateLink(dto)

        then:
        1 * shopService.sendCreateLink(testEmail)
    }

    def "sendPasswordResetLink calls shopService"() {
        setup:
        String testEmail = "info@example.com"
        SendPasswordResetLinkDto dto = new SendPasswordResetLinkDto(testEmail)

        when:
        controller.sendPasswordResetLink(dto)

        then:
        1 * shopLoginService.sendPasswordResetLink(testEmail)
    }

    def "Resets password"() {
        setup:
        String newPassword = "siodfhsiuh"
        String token = "dksfhsauifh"
        String email = "info@example.com"
        ResetPasswordDto dto = new ResetPasswordDto(newPassword)

        when:
        controller.resetPassword(dto, token)

        then:
        1 * tokenService.verifyPasswordResetToken(_) >> email
        1 * shopLoginService.resetPassword(email, newPassword)
    }

    def "Shop gets created"() {
        setup:
        Shop shop = ShopFixtures.create()
        SlotConfigDto slots = SlotConfigDto.of(shop.getSlotConfig())
        CreateShopDto dto = new CreateShopDto("name", "ownername", "street", "zipCode", "city", "addressSupplement", "details", "www.example.com", "password", Map.of(), slots, new SocialLinksDto("instagram", "facebook", "twitter"))
        String token = "test-token"
        String testImageUrl = "http://image.url/path"
        URI testImageUri = new URI(testImageUrl)

        when:
        ShopDetailDto result = controller.createShop(dto, token, httpServletResponse)

        then:
        result
        result.id == shop.id.toString()
        result.imageUrl == testImageUrl

        1 * shopService.create(_) >> shop
        1 * imageService.generatePublicUrl(shop.imageId) >> testImageUri
    }

    def "Gets nearby shops by location string"() {
        setup:
        String testLocation = "location string"
        String testImageUrl = "http://image.url/path"
        URI testImageUri = new URI(testImageUrl)
        Shop shop = ShopFixtures.create()
        List<ShopWithDistance> shopWithDistanceList = [new ShopWithDistance(shop, 12.1)]

        when:
        ShopListDto result = controller.findActive(testLocation, null)

        then:
        result
        result.shops.each {
            assert it.imageUrl == testImageUrl
        }
        1 * shopService.findActive(testLocation) >> shopWithDistanceList
        1 * imageService.generatePublicUrl(shop.imageId) >> testImageUri
    }

    def "Gets nearby shops by location string and max distance"() {
        setup:
        String testLocation = "location string"
        String testImageUrl = "http://image.url/path"
        URI testImageUri = new URI(testImageUrl)
        Shop shop = ShopFixtures.create()
        List<ShopWithDistance> shopWithDistanceList = [new ShopWithDistance(shop, 5)]
        int maxDistance = 5

        when:
        ShopListDto result = controller.findActive(testLocation, maxDistance)

        then:
        result
        result.shops.each {
            assert it.imageUrl == testImageUrl
        }
        1 * shopService.findActive(testLocation, maxDistance) >> shopWithDistanceList
        1 * imageService.generatePublicUrl(shop.imageId) >> testImageUri
    }

    def "Search shops by query and location"() {
        setup:
        String testLocation = "location string"
        String testQuery = "query string"
        String testImageUrl = "http://image.url/path"
        URI testImageUri = new URI(testImageUrl)
        Image.Id imageId = Image.Id.of(UUID.randomUUID())
        Shop shop = ShopFixtures.create(imageId)
        List<ShopWithDistance> shopWithDistanceList = [new ShopWithDistance(shop, 12.1)]

        when:
        ShopListDto result = controller.searchActive(testQuery, testLocation, null)

        then:
        result
        result.shops.each {
            assert it.imageUrl == testImageUrl
        }
        1 * shopService.searchActive(testQuery, testLocation) >> shopWithDistanceList
        1 * imageService.generatePublicUrl(imageId) >> testImageUri
    }

    def "Gets nearby shops by query, location and max distance"() {
        setup:
        String testLocation = "location string"
        String testQuery = "query string"
        String testImageUrl = "http://image.url/path"
        URI testImageUri = new URI(testImageUrl)
        int maxDistance = 5
        Image.Id imageId = Image.Id.of(UUID.randomUUID())
        Shop shop = ShopFixtures.create(imageId)
        List<ShopWithDistance> shopWithDistanceList = [new ShopWithDistance(shop, 5)]

        when:
        ShopListDto result = controller.searchActive(testQuery, testLocation, maxDistance)

        then:
        result
        result.shops.each {
            assert it.imageUrl == testImageUrl
        }
        1 * shopService.searchActive(testQuery, testLocation, maxDistance) >> shopWithDistanceList
        1 * imageService.generatePublicUrl(imageId) >> testImageUri
    }
}
