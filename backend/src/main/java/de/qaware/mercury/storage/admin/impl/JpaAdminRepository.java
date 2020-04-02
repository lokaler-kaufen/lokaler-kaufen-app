package de.qaware.mercury.storage.admin.impl;

import de.qaware.mercury.business.admin.Admin;
import de.qaware.mercury.business.time.Clock;
import de.qaware.mercury.storage.admin.AdminRepository;
import de.qaware.mercury.util.Lists;
import de.qaware.mercury.util.Null;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class JpaAdminRepository implements AdminRepository {
    private final AdminDataRepository adminDataRepository;
    private final Clock clock;

    @Override
    public Admin findByEmail(String email) {
        AdminEntity entity = adminDataRepository.findFirstByEmail(email);

        return Null.map(entity, AdminEntity::toAdmin);
    }

    @Override
    public Admin findById(Admin.Id id) {
        AdminEntity entity = adminDataRepository.findById(id.getId()).orElse(null);

        return Null.map(entity, AdminEntity::toAdmin);
    }

    @Override
    public void insert(Admin admin) {
        log.debug("Insert admin {}", admin);
        adminDataRepository.save(AdminEntity.of(admin.withCreated(clock.nowZoned())));
    }

    @Override
    public List<Admin> findWithEmailOnShopApprovalNeeded(boolean value) {
        List<AdminEntity> entities = adminDataRepository.findWithEmailOnShopApprovalNeeded(value);
        return Lists.map(entities, AdminEntity::toAdmin);
    }
}
