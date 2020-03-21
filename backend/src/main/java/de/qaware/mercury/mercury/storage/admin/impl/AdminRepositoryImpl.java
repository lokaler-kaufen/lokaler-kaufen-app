package de.qaware.mercury.mercury.storage.admin.impl;

import de.qaware.mercury.mercury.business.admin.Admin;
import de.qaware.mercury.mercury.storage.admin.AdminRepository;
import de.qaware.mercury.mercury.util.Null;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class AdminRepositoryImpl implements AdminRepository {
    private final AdminDataRepository adminDataRepository;

    AdminRepositoryImpl(AdminDataRepository adminDataRepository) {
        this.adminDataRepository = adminDataRepository;
    }

    @Override
    public Admin findByEmail(String email) {
        AdminEntity adminEntity = adminDataRepository.findFirstByEmail(email);

        return Null.map(adminEntity, AdminEntity::toAdmin);
    }

    @Override
    public Admin findById(Admin.Id id) {
        AdminEntity adminEntity = adminDataRepository.findById(id.getId()).orElse(null);

        return Null.map(adminEntity, AdminEntity::toAdmin);
    }

    @Override
    public void insert(Admin admin) {
        log.debug("Insert admin {}", admin);
        adminDataRepository.save(AdminEntity.of(admin));
    }
}
