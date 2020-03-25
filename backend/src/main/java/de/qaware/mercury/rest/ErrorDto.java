package de.qaware.mercury.rest;

import de.qaware.mercury.business.uuid.UUIDFactory;
import lombok.Value;

@Value
public class ErrorDto {
    /**
     * Unique id.
     */
    String id;
    /**
     * Machine readable code.
     */
    String code;
    /**
     * Human readable message.
     */
    String message;

    public static ErrorDto of(UUIDFactory uuidFactory, String code, String message) {
        return new ErrorDto(uuidFactory.create().toString(), code, message);
    }
}
