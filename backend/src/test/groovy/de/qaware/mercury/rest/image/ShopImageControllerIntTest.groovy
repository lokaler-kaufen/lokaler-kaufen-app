package de.qaware.mercury.rest.image

import de.qaware.mercury.business.shop.Shop
import de.qaware.mercury.business.shop.ShopCreation
import de.qaware.mercury.business.shop.ShopService
import de.qaware.mercury.integrationtest.ShopMvc
import de.qaware.mercury.test.IntegrationTestSpecification
import de.qaware.mercury.test.fixtures.ShopCreationFixtures
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions

import javax.servlet.http.Cookie

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ShopImageControllerIntTest extends IntegrationTestSpecification {
    @Autowired
    MockMvc mvc

    @Autowired
    ShopService shopService

    def "upload and delete image"() {
        given: "a logged in shop"
        ShopCreation creation = ShopCreationFixtures.create()
        Shop shop = shopService.create(creation)
        shopService.changeApproved(shop.id, true)
        Cookie token = ShopMvc.loginShop(mvc, creation.email, creation.password)

        when: "we upload a new image"
        ResultActions result = mvc.perform(multipart("/api/image/shop")
            .file(new MockMultipartFile("file", "1.jpg", "image/jpeg", this.getClass().getResourceAsStream("/test/images/1.jpg").bytes))
            .cookie(token)
        )

        then: "this image has been created"
        MockHttpServletResponse response = result
            .andExpect(status().isCreated())
            .andReturn().response
        Map<String, Object> imageDto = contentAsMap(response)
        String imageId = imageDto['id']
        imageId != null

        when: "we request the shop details"
        result = mvc.perform(get("/api/shop/{id}", shop.id))
        response = result.andReturn().response
        Map<String, Object> shopDetailDto = contentAsMap(response)

        then: "the shop has an image url"
        result.andExpect(status().isOk())
        shopDetailDto['imageUrl'] != null

        when: "we load that image"
        result = mvc.perform(get("/dev/image/{filename}", imageId + ".jpg"))
        response = result.andReturn().response

        then: "we get some data"
        result.andExpect(status().isOk())
        response.getContentAsByteArray().size() > 0

        when: "we delete the image"
        result = mvc.perform(delete("/api/image/shop").cookie(token))

        then: "the image is gone"
        result.andExpect(status().isOk())
        mvc.perform(get("/dev/image/{filename}", imageId + ".jpg"))
            .andExpect(status().isNotFound())

        when: "we request the shop details"
        response = mvc.perform(get("/api/shop/{id}", shop.id)).andReturn().response
        shopDetailDto = contentAsMap(response)

        then: "the shop has no imageId anymore"
        shopDetailDto['imageId'] == null
    }
}
