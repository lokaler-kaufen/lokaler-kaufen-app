package de.qaware.mercury.storage.shop.impl;

import de.qaware.mercury.business.shop.Contact;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.Shop;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.UUID;

@Entity
@Getter
// See https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shop", uniqueConstraints = @UniqueConstraint(columnNames = {"shop_id", "contact_type"}))
class ContactEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Setter
    @Column(nullable = false)
    private UUID shopId;

    @Setter
    @Column(nullable = false)
    private String contactType;

    @Setter
    @Column(nullable = false)
    private String data;

    public static ContactEntity of(Contact contact) {
        return new ContactEntity(
            contact.getId().getId(),
            contact.getShopId().getId(),
            contact.getContactType().toString(),
            contact.getData()
        );
    }

    public Contact toContact() {
        return new Contact(
            Contact.Id.of(id),
            Shop.Id.of(shopId),
            ContactType.parse(contactType),
            data
        );
    }

}
