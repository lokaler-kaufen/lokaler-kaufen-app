package de.qaware.mercury.business.shop;

import de.qaware.mercury.business.image.Image;
import de.qaware.mercury.business.location.impl.LocationNotFoundException;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.util.List;

public interface ShopService {
    List<Shop> listAll();

    Shop create(ShopCreation creation) throws ShopAlreadyExistsException, LocationNotFoundException;

    Shop update(Shop shop, ShopUpdate update) throws LocationNotFoundException;

    void updateShopColor(Shop shop, String color);

    void changeApproved(Shop.Id id, boolean approved) throws ShopNotFoundException;

    /**
     * Sets the image of a shop.
     *
     * @param shop  the shop
     * @param image the image
     * @param color the shop color
     * @return Updated shop
     */
    Shop setImage(Shop shop, Image image, String color);

    /**
     * Finds all approved and enabled shops
     *
     * @param zipCode the zipCode of the user
     * @return all approved shops
     * @throws LocationNotFoundException if the given zipCode does not exist
     */
    List<ShopWithDistance> findActive(String zipCode) throws LocationNotFoundException;

    /**
     * Finds all approved and enabled shops within the given distance from the given zipCode
     *
     * @param zipCode     the zipCode of the user
     * @param maxDistance the maximum distance from the given zipCode
     * @return all approved shops within maxDistance from zipCode
     * @throws LocationNotFoundException if the given zipCode does not exist
     */
    List<ShopWithDistance> findActive(String zipCode, int maxDistance) throws LocationNotFoundException;

    void delete(Shop.Id parse) throws ShopNotFoundException;

    @Nullable
    Shop findById(Shop.Id id);

    Breaks findBreaks(Shop shop);

    Shop findByIdOrThrow(Shop.Id id) throws ShopNotFoundException;

    /**
     * Sends the email with the creation link for a new shop to the given email address
     *
     * @param email email address
     */
    void sendCreateLink(String email) throws ShopAlreadyExistsException;

    List<Shop> findByName(String name);

    /**
     * Searches for approved and enabled shops matching the given query.
     *
     * @param query   a search query, supports '%' as a 'like' operator, searches on name and description of the shop
     * @param zipCode the zipCode of the user
     * @return a list of shops matching the query
     * @throws LocationNotFoundException if the zipCode does not exist
     */
    List<ShopWithDistance> searchActive(String query, String zipCode) throws LocationNotFoundException;

    /**
     * Searches for approved and enabled shops within a given distance of the given zipCode matching the given query.
     *
     * @param query       a search query, supports '%' as a 'like' operator, searches on name and description of the shop
     * @param zipCode     the zipCode of the user
     * @param maxDistance the maximum distance between the shops and the user in km
     * @return a list of shops matching the query within maxDistance km of the user
     * @throws LocationNotFoundException if the zipCode does not exist
     */
    List<ShopWithDistance> searchActive(String query, String zipCode, int maxDistance) throws LocationNotFoundException;

    /**
     * Deletes the image from the given shop.
     *
     * @param shop shop to delete the image from
     * @return Shop without image id
     */
    Shop deleteImage(Shop shop);

    @Nullable
    Shop findBySlug(String slug);

    /**
     * Generates the share link for the given shop.
     *
     * @param shop shop
     * @return share link
     */
    URI generateShareLink(Shop shop);

    /**
     * Generates the image url for the given shop.
     *
     * @param shop shop
     * @return image url
     */
    @Nullable
    URI generateImageUrl(Shop shop);
}
