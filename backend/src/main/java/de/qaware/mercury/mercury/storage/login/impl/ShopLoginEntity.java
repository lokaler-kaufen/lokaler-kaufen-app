package de.qaware.mercury.mercury.storage.login.impl;

import de.qaware.mercury.mercury.business.login.ShopLogin;
import de.qaware.mercury.mercury.business.shop.Shop;
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

    public static ShopLoginEntity of(ShopLogin shopLogin) {
        return new ShopLoginEntity(shopLogin.getId().getId(), shopLogin.getShopId().getId(), shopLogin.getEmail(), shopLogin.getPasswordHash());
    }

    public static ShopLogin toShopLogin(ShopLoginEntity entity) {
        return new ShopLogin(ShopLogin.Id.of(entity.getId()), Shop.Id.of(entity.getShopId()), entity.getEmail(), entity.getPasswordHash());
    }
}
