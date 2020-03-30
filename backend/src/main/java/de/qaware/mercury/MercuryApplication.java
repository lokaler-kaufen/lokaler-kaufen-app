package de.qaware.mercury;

import de.qaware.mercury.cli.AddAdminRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class MercuryApplication {
    public static void main(String[] args) {
        if (hasFlag(args, "--add-admin")) {
            log.info("Starting in 'Add admin' mode");
            // This starts the Spring Boot application without the HTTP layer, then runs the AddAdminRunner bean, then shuts down again
            try (
                ConfigurableApplicationContext context = new SpringApplicationBuilder(MercuryApplication.class)
                    .web(WebApplicationType.NONE).run(args) // Disables the HTTP stuff from Spring Boot
            ) {
                AddAdminRunner runner = context.getBean(AddAdminRunner.class);
                runner.run();
            }
        } else {
            SpringApplication.run(MercuryApplication.class, args);
        }
    }

    private static boolean hasFlag(String[] args, String flag) {
        for (String arg : args) {
            if (arg.equals(flag)) {
                return true;
            }
        }

        return false;
    }
}
