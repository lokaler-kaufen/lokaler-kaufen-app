package de.qaware.mercury.rest.clienterror.dto.request;

import de.qaware.mercury.business.clienterror.ClientError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientErrorDto {
    @NotBlank
    String traceId;
    @NotBlank
    String requestedUrl;
    // Don't report 1xx and 2xx to us!
    @Min(300)
    int httpCode;
    @NotNull
    String body;

    public ClientError toClientError() {
        return new ClientError(traceId, requestedUrl, httpCode, body);
    }
}
