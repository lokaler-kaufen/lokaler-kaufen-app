package de.qaware.mercury.mercury.business.login.impl;

import de.qaware.mercury.mercury.business.admin.Admin;
import de.qaware.mercury.mercury.business.login.AdminLoginService;
import de.qaware.mercury.mercury.business.login.AdminToken;
import de.qaware.mercury.mercury.business.login.LoginException;
import de.qaware.mercury.mercury.business.login.PasswordHasher;
import de.qaware.mercury.mercury.business.login.TokenService;
import de.qaware.mercury.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.mercury.storage.admin.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
class AdminLoginServiceImpl implements AdminLoginService {
    private final AdminRepository adminRepository;
    private final PasswordHasher passwordHasher;
    private final TokenService tokenService;
    private final UUIDFactory uuidFactory;

    AdminLoginServiceImpl(AdminRepository adminRepository, PasswordHasher passwordHasher, TokenService tokenService, UUIDFactory uuidFactory) {
        this.adminRepository = adminRepository;
        this.passwordHasher = passwordHasher;
        this.tokenService = tokenService;
        this.uuidFactory = uuidFactory;
    }

    @Override
    @Transactional
    public Admin createLogin(String email, String password) {
        Admin.Id id = Admin.Id.random(uuidFactory);
        String hash = passwordHasher.hash(password);

        Admin admin = new Admin(id, email, hash);
        adminRepository.insert(admin);
        log.info("Created admin '{}', id {}", email, id);
        return admin;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminToken login(String email, String password) throws LoginException {
        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) {
            log.warn("Admin '{}' not found", email);
            throw LoginException.forAdminEmail(email);
        }

        if (!passwordHasher.verify(password, admin.getPasswordHash())) {
            log.warn("Invalid password for admin '{}'", email);
            throw LoginException.forAdminEmail(email);
        }

        log.info("Logged in admin '{}'", email);
        return tokenService.createAdminToken(admin.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Admin verify(AdminToken token) throws LoginException {
        Admin.Id adminId = tokenService.verifyAdminToken(token);

        Admin admin = adminRepository.findById(adminId);
        if (admin == null) {
            log.warn("Token is valid, but admin with id '{}' not found", adminId);
            throw LoginException.forAdminId(adminId);
        }

        return admin;
    }
}
