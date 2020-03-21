package de.qaware.mercury.mercury.business.login;

import de.qaware.mercury.mercury.business.TechnicalException;

public class TokenTechnicalException extends TechnicalException {
    public TokenTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }
}
