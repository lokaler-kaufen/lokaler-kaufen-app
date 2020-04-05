package de.qaware.mercury.rest.shop

import de.qaware.mercury.business.image.Image
import de.qaware.mercury.business.image.ImageService
import de.qaware.mercury.business.location.GeoLocation
import de.qaware.mercury.business.login.PasswordResetToken
import de.qaware.mercury.business.login.ShopLoginService
import de.qaware.mercury.business.login.TokenService
import de.qaware.mercury.business.shop.ContactType
import de.qaware.mercury.business.shop.DayConfig
import de.qaware.mercury.business.shop.Shop
import de.qaware.mercury.business.shop.ShopNotFoundException
import de.qaware.mercury.business.shop.ShopService
import de.qaware.mercury.business.shop.ShopWithDistance
import de.qaware.mercury.business.shop.SlotConfig
import de.qaware.mercury.rest.plumbing.authentication.AuthenticationHelper
import de.qaware.mercury.rest.shop.dto.request.CreateShopDto
import de.qaware.mercury.rest.shop.dto.request.ResetPasswordDto
import de.qaware.mercury.rest.shop.dto.request.SendCreateLinkDto
import de.qaware.mercury.rest.shop.dto.request.SendPasswordResetLinkDto
import de.qaware.mercury.rest.shop.dto.request.SlotConfigDto
import de.qaware.mercury.rest.shop.dto.request.UpdateShopDto
import de.qaware.mercury.rest.shop.dto.response.ShopDetailDto
import de.qaware.mercury.rest.shop.dto.response.ShopListDto
import de.qaware.mercury.rest.shop.dto.response.ShopOwnerDetailDto
import de.qaware.mercury.rest.util.cookie.CookieHelper
import de.qaware.mercury.util.Null
import spock.lang.Specification
import spock.lang.Subject

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.time.LocalTime
import java.time.ZonedDateTime

class ShopControllerSpec extends Specification {

    @Subject
    ShopController controller

    ShopService shopService = Mock(ShopService)
    TokenService tokenService = Mock(TokenService)
    AuthenticationHelper authenticationHelper = Mock(AuthenticationHelper)
    ShopLoginService shopLoginService = Mock(ShopLoginService)
    ImageService imageService = Mock(ImageService)

    HttpServletRequest httpServletRequest = Mock(HttpServletRequest)
    HttpServletResponse httpServletResponse = Mock(HttpServletResponse)
    CookieHelper cookieHelper = Mock(CookieHelper)

    void setup() {
        controller = new ShopController(shopService, imageService, tokenService, authenticationHelper, shopLoginService, cookieHelper)
    }

    def "Retrieve shop details"() {
        setup:
        UUID id = UUID.randomUUID()
        Image.Id imageId = Image.Id.of(UUID.randomUUID())
        Shop shop = createShopObject(id, imageId.getId())
        String testImageUrl = "http://image.url/path"
        URI testImageUri = new URI(testImageUrl)

        when:
        ShopDetailDto result = controller.getShopDetails(id.toString())

        then:
        result
        result.id == id.toString()
        result.imageUrl == testImageUrl

        1 * shopService.findById(Shop.Id.parse(id.toString())) >> shop
        1 * imageService.generatePublicUrl(imageId) >> testImageUri
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

    def "Retrieve shop settings"() {
        setup:
        UUID id = UUID.randomUUID()
        Image.Id imageId = Image.Id.of(UUID.randomUUID())
        Shop shop = createShopObject(id, imageId.getId())
        String testImageUrl = "http://image.url/path"
        URI testImageUri = new URI(testImageUrl)

        when:
        ShopOwnerDetailDto result = controller.getShopSettings(httpServletRequest)

        then:
        result
        result.id == id.toString()
        result.imageUrl == testImageUrl

        1 * authenticationHelper.authenticateShop(httpServletRequest) >> shop
        1 * imageService.generatePublicUrl(imageId) >> testImageUri
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
        PasswordResetToken passwordResetToken = new PasswordResetToken(token)

        when:
        controller.resetPassword(dto, token)

        then:
        1 * tokenService.verifyPasswordResetToken(_) >> email
        1 * shopLoginService.resetPassword(email, newPassword)
    }

    def "Shop gets created"() {
        setup:
        UUID id = UUID.randomUUID()
        CreateShopDto dto = new CreateShopDto("name", "ownername", "street", "zipCode", "city", "addressSupplement", "details", "www.example.com", "password", new HashMap<String, String>(), Null.map(createSlotConfig(), { slotConfig -> SlotConfigDto.of(slotConfig) }))
        String token = "test-token"
        Image.Id imageId = Image.Id.of(UUID.randomUUID())
        Shop shop = createShopObject(id, imageId.getId())
        String testImageUrl = "http://image.url/path"
        URI testImageUri = new URI(testImageUrl)

        when:
        ShopDetailDto result = controller.createShop(dto, token, httpServletResponse)

        then:
        result
        result.id == id.toString()
        result.imageUrl == testImageUrl

        1 * shopService.create(_) >> shop
        1 * imageService.generatePublicUrl(imageId) >> testImageUri
    }

    def "Shop gets updated"() {
        setup:
        UUID id = UUID.randomUUID()
        UpdateShopDto dto = new UpdateShopDto("name", "ownername", "street", "zipCode", "city", "addressSupplement", "details", "www.example.com", new HashMap<String, String>(), Null.map(createSlotConfig(), { slotConfig -> SlotConfigDto.of(slotConfig) }))
        Image.Id imageId = Image.Id.of(UUID.randomUUID())
        Shop shop = createShopObject(id, imageId.getId())
        String testImageUrl = "http://image.url/path"
        URI testImageUri = new URI(testImageUrl)

        when:
        ShopOwnerDetailDto result = controller.updateShop(dto, httpServletRequest)

        then:
        result
        result.id == id.toString()
        result.imageUrl == testImageUrl

        1 * authenticationHelper.authenticateShop(httpServletRequest)
        1 * shopService.update(_, _) >> shop
        1 * imageService.generatePublicUrl(imageId) >> testImageUri
    }

    def "Gets nearby shops by location string"() {
        setup:
        String testLocation = "location string"
        String testImageUrl = "http://image.url/path"
        URI testImageUri = new URI(testImageUrl)
        Image.Id imageId = Image.Id.of(UUID.randomUUID())
        List<ShopWithDistance> shopWithDistanceList = [new ShopWithDistance(createShopObject(UUID.randomUUID(), imageId.getId()), 12.1)]

        when:
        ShopListDto result = controller.findActive(testLocation, null)

        then:
        result
        result.shops.each {
            assert it.imageUrl == testImageUrl
        }
        1 * shopService.findActive(testLocation) >> shopWithDistanceList
        1 * imageService.generatePublicUrl(imageId) >> testImageUri
    }

    def "Gets nearby shops by location string and max distance"() {
        setup:
        String testLocation = "location string"
        String testImageUrl = "http://image.url/path"
        URI testImageUri = new URI(testImageUrl)
        Image.Id imageId = Image.Id.of(UUID.randomUUID())
        List<ShopWithDistance> shopWithDistanceList = [new ShopWithDistance(createShopObject(UUID.randomUUID(), imageId.getId()), 5)]
        int maxDistance = 5

        when:
        ShopListDto result = controller.findActive(testLocation, maxDistance)

        then:
        result
        result.shops.each {
            assert it.imageUrl == testImageUrl
        }
        1 * shopService.findActive(testLocation, maxDistance) >> shopWithDistanceList
        1 * imageService.generatePublicUrl(imageId) >> testImageUri
    }

    def "Search shops by query and location"() {
        setup:
        String testLocation = "location string"
        String testQuery = "query string"
        String testImageUrl = "http://image.url/path"
        URI testImageUri = new URI(testImageUrl)
        Image.Id imageId = Image.Id.of(UUID.randomUUID())
        List<ShopWithDistance> shopWithDistanceList = [new ShopWithDistance(createShopObject(UUID.randomUUID(), imageId.getId()), 12.1)]

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
        List<ShopWithDistance> shopWithDistanceList = [new ShopWithDistance(createShopObject(UUID.randomUUID(), imageId.getId()), 5)]

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

    private static Shop createShopObject(UUID id, UUID imageId) {
        return new Shop(
            Shop.Id.of(id),
            "Name",
            "Owner Name",
            "info@example.com",
            "Street",
            "23947",
            "City",
            "Address Supplement",
            new HashMap<ContactType, String>(),
            true,
            true,
            Image.Id.of(imageId),
            GeoLocation.of(47, 12),
            "Details",
            "www.example.com",
            createSlotConfig(),
            createZonedDateTime(),
            createZonedDateTime()
        )
    }

    private static SlotConfig createSlotConfig() {
        return new SlotConfig(
            15,
            15,
            new DayConfig(LocalTime.parse("10:00"), LocalTime.parse("11:00")),
            new DayConfig(LocalTime.parse("11:30"), LocalTime.parse("12:30")),
            new DayConfig(LocalTime.parse("13:00"), LocalTime.parse("14:00")),
            new DayConfig(LocalTime.parse("14:30"), LocalTime.parse("15:30")),
            new DayConfig(LocalTime.parse("16:00"), LocalTime.parse("17:00")),
            new DayConfig(LocalTime.parse("17:30"), LocalTime.parse("18:30")),
            new DayConfig(LocalTime.parse("19:00"), LocalTime.parse("20:00"))
        )
    }

    private static ZonedDateTime createZonedDateTime() {
        return ZonedDateTime.now()
    }
}
