package de.qaware.mercury.business.shop.impl;

import de.qaware.mercury.business.shop.SlugService;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class SlugServiceImpl implements SlugService {
    private static final int MAX_TRIES = 1000;

    /**
     * Allowed chars in a slug. You only need to include lower case characters.
     */
    private static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";

    @Override
    public String generateShopSlug(String shopName, Predicate<String> isSlugUnique) {
        int counter = 1;
        do {
            String slug = generateSlug(shopName, counter);

            // Check if the slug is unique
            if (isSlugUnique.test(slug)) {
                return slug;
            }

            // Slug isn't unique, so append a counter and try again
            counter++;

            // Failsafe against a broken isSlugUnique predicate
            if (counter > MAX_TRIES) {
                throw new IllegalStateException("Reached maximum tries for slug creation");
            }
        } while (true);
    }

    private String generateSlug(String name, int counter) {
        StringBuilder slug = new StringBuilder();

        // Iterate over the shop name chars, lowercase it, discard the forbidden chars and replace spaces with a dash
        for (int i = 0; i < name.length(); i++) {
            char c = Character.toLowerCase(name.charAt(i));

            if (c == ' ') {
                slug.append('-');
            } else if (c == 'ä') {
                slug.append("ae");
            } else if (c == 'ö') {
                slug.append("oe");
            } else if (c == 'ü') {
                slug.append("ue");
            } else if (c == 'ß') {
                slug.append("ss");
            } else if (ALLOWED_CHARS.indexOf(c) > -1) {
                slug.append(c);
            } // If not covered above, the char is just dropped
        }

        // If this isn't the 1st try, append a counter
        if (counter > 1) {
            slug.append('-').append(counter);
        }

        return slug.toString();
    }
}
