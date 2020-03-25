package de.qaware.mercury.business.login;

public interface PasswordHasher {
    String hash(String password);

    boolean verify(String password, String hash);
}
