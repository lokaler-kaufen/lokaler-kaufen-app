package de.qaware.mercury.rest.clienterror;

import de.qaware.mercury.business.clienterror.ClientErrorService;
import de.qaware.mercury.rest.clienterror.dto.request.ClientErrorDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/client-error", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ClientErrorController {
    private final ClientErrorService clientErrorService;

    @PostMapping
    public void reportError(@Valid @RequestBody ClientErrorDto request) {
        clientErrorService.report(request.toClientError());
    }
}
