package de.qaware.mercury.business.login.impl;

import de.qaware.mercury.business.admin.Admin;
import de.qaware.mercury.business.login.AdminEmailSettings;
import de.qaware.mercury.business.login.AdminLoginService;
import de.qaware.mercury.business.login.AdminToken;
import de.qaware.mercury.business.login.LoginException;
import de.qaware.mercury.business.login.PasswordHasher;
import de.qaware.mercury.business.login.TokenService;
import de.qaware.mercury.business.login.VerifiedToken;
import de.qaware.mercury.business.time.Clock;
import de.qaware.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.storage.admin.AdminRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class AdminLoginServiceImpl implements AdminLoginService {
    private final AdminRepository adminRepository;
    private final PasswordHasher passwordHasher;
    private final TokenService tokenService;
    private final UUIDFactory uuidFactory;
    private final Clock clock;

    @Override
    @Transactional
    public Admin createLogin(String email, String password, AdminEmailSettings emailSettings) {
        Admin.Id id = Admin.Id.random(uuidFactory);
        String hash = passwordHasher.hash(password);

        Admin admin = new Admin(id, email, hash, emailSettings.isOnShopApprovalNeeded(), clock.nowZoned(), clock.nowZoned());
        adminRepository.insert(admin);
        log.info("Created admin '{}', id {}", email, id);
        return admin;
    }

    @Override
    @Transactional(readOnly = true)
    public VerifiedToken<Admin.Id, AdminToken> login(String email, String password) throws LoginException {
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
        VerifiedToken<Admin.Id, AdminToken> verifiedToken = tokenService.verifyAdminToken(token);

        Admin admin = adminRepository.findById(verifiedToken.getId());
        if (admin == null) {
            log.warn("Token is valid, but admin with id '{}' not found", verifiedToken);
            throw LoginException.forAdminToken(token);
        }

        return admin;
    }

    @Override
    @Transactional(readOnly = true)
    @Nullable
    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    @Override
    @Nullable
    public VerifiedToken<Admin.Id, AdminToken> getTokenInfo(AdminToken token) {
        try {
            return tokenService.verifyAdminToken(token);
        } catch (LoginException e) {
            log.debug("Failed to verify admin token for getTokenInfo()", e);
            return null;
        }
    }
}
