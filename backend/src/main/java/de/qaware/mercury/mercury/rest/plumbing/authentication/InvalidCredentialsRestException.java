package de.qaware.mercury.mercury.rest.plumbing.authentication;

import de.qaware.mercury.mercury.rest.RestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public
    // Spring then sends a 401 to the client
class InvalidCredentialsRestException extends RestException {
    public InvalidCredentialsRestException(String message) {
        super(message);
    }
}
