package de.qaware.mercury.mercury;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MercuryApplication {
    public static void main(String[] args) {
        // Shut up Vlads stupid banner, ffs
        System.setProperty("hibernate.types.print.banner", "false");

        SpringApplication.run(MercuryApplication.class, args);
    }
}
