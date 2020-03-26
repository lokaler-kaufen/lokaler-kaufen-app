package de.qaware.mercury.storage.login.impl;

import de.qaware.mercury.business.login.ShopLogin;
import de.qaware.mercury.business.shop.Shop;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
// See https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shop_login")
public class ShopLoginEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Setter
    @Column(name = "shop_id", nullable = false)
    private UUID shopId;

    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private ZonedDateTime created;

    @Column(nullable = false)
    private ZonedDateTime updated;

    public static ShopLoginEntity of(ShopLogin shopLogin) {
        return new ShopLoginEntity(shopLogin.getId().getId(), shopLogin.getShopId().getId(), shopLogin.getEmail(), shopLogin.getPasswordHash(), shopLogin.getCreated(), shopLogin.getUpdated());
    }

    public ShopLogin toShopLogin() {
        return new ShopLogin(ShopLogin.Id.of(id), Shop.Id.of(shopId), email, passwordHash, created, updated);
    }
}
