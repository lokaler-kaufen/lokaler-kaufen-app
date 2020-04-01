package de.qaware.mercury.cli;

import de.qaware.mercury.business.login.AdminEmailSettings;
import de.qaware.mercury.business.login.AdminLoginService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * This bean is run when the application is started with '--add-admin'
 */
@Component
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class AddAdminRunner {
    private final AdminLoginService adminLoginService;

    public void run() {
        System.out.println("######################");
        System.out.println("# Adding a new admin #");
        System.out.println("######################");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Email: ");
        String email = scanner.nextLine();

        if (adminLoginService.findByEmail(email) != null) {
            log.error("Admin with email '{}' already exists", email);
            return;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine();

        // TODO MKA: Make this configurable
        adminLoginService.createLogin(email, password, new AdminEmailSettings(true));
        log.info("Added admin '{}'", email);
    }
}
