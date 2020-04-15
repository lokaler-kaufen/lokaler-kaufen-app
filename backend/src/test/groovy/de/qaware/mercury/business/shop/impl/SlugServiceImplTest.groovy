package de.qaware.mercury.business.shop.impl


import spock.lang.Specification
import spock.lang.Subject

import java.util.function.Predicate

class SlugServiceImplTest extends Specification {
    @Subject
    SlugServiceImpl sut = new SlugServiceImpl()

    def "unique slug on 1st try"() {
        given: "no existing slugs"
        Predicate predicate = { true }

        when: "we create the slug"
        String slug = sut.generateShopSlug("Awesome Shop Name", predicate)

        then: "we get a unique slug on 1st try"
        slug == "awesome-shop-name"
    }

    def "unique slug on 2nd try"() {
        given: "an existing slug 'awesome-shop-name'"
        Predicate predicate = { slug -> slug != "awesome-shop-name" }

        when: "we create the slug"
        String slug = sut.generateShopSlug("Awesome Shop Name", predicate)

        then: "we get a unique slug on the 2nd try"
        slug == "awesome-shop-name-2"
    }

    def "test umlauts"() {
        given: "no existing slugs"
        Predicate predicate = { true }

        when: "we create the slug with umlauts"
        String slug = sut.generateShopSlug("ä ö ü ß", predicate)

        then: "we get a unique slug"
        slug == "ae-oe-ue-ss"

    }
}
