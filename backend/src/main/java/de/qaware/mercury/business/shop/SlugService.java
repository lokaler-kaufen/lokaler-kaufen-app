package de.qaware.mercury.business.shop;

import java.util.function.Predicate;

public interface SlugService {
    String generateShopSlug(String shopName, Predicate<String> isSlugUnique);
}
