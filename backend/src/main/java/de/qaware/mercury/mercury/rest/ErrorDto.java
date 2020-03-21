package de.qaware.mercury.mercury.rest;

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
}
