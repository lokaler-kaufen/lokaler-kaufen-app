package de.qaware.mercury.mercury.rest.plumbing.authentication;

import de.qaware.mercury.mercury.rest.RestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED) // Spring then sends a 401 to the client
public class NotAuthenticatedRestException extends RestException {
    public NotAuthenticatedRestException(String message) {
        super(message);
    }
}
