package de.qaware.mercury.storage.shop.impl;

import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.ShopContact;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Getter
// See https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shop_contact")
class ShopContactEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShopEntity shop;

    @Setter
    @Column(nullable = false)
    private String contactType;

    @Setter
    @Column(nullable = false)
    private String data;

    public ShopContact toShopContact() {
        return new ShopContact(
            ShopContact.Id.of(getId()), ContactType.parse(getContactType()), getData()
        );
    }

    public static ShopContactEntity of(ShopContact contact, ShopEntity shopEntity) {
        return new ShopContactEntity(
            contact.getId().getId(), shopEntity, contact.getContactType().getId(), contact.getData()
        );
    }
}
