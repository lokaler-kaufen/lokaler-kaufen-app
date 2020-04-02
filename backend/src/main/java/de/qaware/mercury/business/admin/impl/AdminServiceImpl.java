package de.qaware.mercury.business.admin.impl;

import de.qaware.mercury.business.admin.Admin;
import de.qaware.mercury.business.admin.AdminService;
import de.qaware.mercury.storage.admin.AdminRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Admin> findAdminsToNotifyOnShopApprovalNeeded() {
        return adminRepository.findWithEmailOnShopApprovalNeeded(true);
    }
}
