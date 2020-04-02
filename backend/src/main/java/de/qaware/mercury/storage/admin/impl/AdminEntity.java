package de.qaware.mercury.storage.admin.impl;

import de.qaware.mercury.business.admin.Admin;
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
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
// See https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin")
public class AdminEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private boolean emailOnShopApprovalNeeded;

    @Column(nullable = false)
    private ZonedDateTime created;

    @Column(nullable = false)
    private ZonedDateTime updated;

    public Admin toAdmin() {
        return new Admin(Admin.Id.of(id), email, passwordHash, emailOnShopApprovalNeeded, created, updated);
    }

    public static AdminEntity of(Admin admin) {
        return new AdminEntity(
            admin.getId().getId(), admin.getEmail(), admin.getPasswordHash(),
            admin.isEmailOnShopApprovalNeeded(), admin.getCreated(), admin.getUpdated()
        );
    }
}
