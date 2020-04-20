package de.qaware.mercury.rest.image;

import de.qaware.mercury.business.image.Image;
import de.qaware.mercury.business.image.ImageNotFoundException;
import de.qaware.mercury.business.image.ImageService;
import de.qaware.mercury.business.login.LoginException;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.ShopService;
import de.qaware.mercury.rest.image.response.ImageDto;
import de.qaware.mercury.rest.plumbing.authentication.AuthenticationHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * This controller manages shop (thumbnail) images.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/image/shop")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ShopImageController {
    private final AuthenticationHelper authenticationHelper;
    private final ImageService imageService;
    private final ShopService shopService;

    /**
     * Uploads a image to be used as the shop's preview thumbnail.
     *
     * @param servletRequest servlet request
     * @return the id of the uploaded image
     * @throws LoginException if the caller is not authenticated as a shop owner
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ImageDto uploadImageForShop(
        @RequestParam("file") MultipartFile file, HttpServletRequest servletRequest
    ) throws LoginException, IOException {
        Shop shop = authenticationHelper.authenticateShop(servletRequest);
        String color = null;

        // Store the image
        Image image;
        try (InputStream stream = file.getInputStream()) {
            image = imageService.addImage(shop.getId(), stream);
            try {
                color = imageService.getImageBackgroundColor(image.getId());
            } catch (ImageNotFoundException e) {
                log.error("Could not derive image color.", e);
                // This should not be possible since we just received this image (and id) from the add-operation
                throw new AssertionError("Unexpected exception.", e);
            }
        }

        // Set the uploaded image as new shop image
        shopService.setImage(shop, image, color);
        return ImageDto.of(image);
    }

    /**
     * Deletes the shop image.
     *
     * @param servletRequest http request
     * @throws LoginException if the caller is not authenticated as a shop owner
     */
    @DeleteMapping
    public void deleteImageFromShop(HttpServletRequest servletRequest) throws LoginException {
        Shop shop = authenticationHelper.authenticateShop(servletRequest);

        shopService.deleteImage(shop);
    }
}
