package de.qaware.mercury.cli;

import de.qaware.mercury.business.login.AdminEmailSettings;
import de.qaware.mercury.business.login.AdminLoginService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * This bean is run when the application is started with '--add-admin'
 */
@Component
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class AddAdminRunner {
    private final AdminLoginService adminLoginService;
    private final Console console;

    public void run() {
        console.printLine("######################");
        console.printLine("# Adding a new admin #");
        console.printLine("######################");

        console.print("Email: ");
        String email = console.readLine();

        if (adminLoginService.findByEmail(email) != null) {
            log.error("Admin with email '{}' already exists", email);
            return;
        }

        console.print("Password: ");
        String password = console.readPassword();

        console.print("Notify on shop approval needed? (y/N): ");
        boolean notifyOnShopApprovalNeeded = console.readBoolean(false);

        adminLoginService.createLogin(email, password, new AdminEmailSettings(notifyOnShopApprovalNeeded));
        log.info("Added admin '{}'", email);
    }
}
