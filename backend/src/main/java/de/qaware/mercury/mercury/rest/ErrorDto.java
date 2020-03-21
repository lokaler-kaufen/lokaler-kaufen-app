package de.qaware.mercury.mercury.rest;

import lombok.Value;

@Value
public class ErrorDto {
    String id;
    String code;
    String message;
}
