package de.qaware.mercury.business.clienterror;

import lombok.Value;

@Value
public class ClientError {
    String traceId;
    String requestedUrl;
    int httpCode;
    String body;
}
