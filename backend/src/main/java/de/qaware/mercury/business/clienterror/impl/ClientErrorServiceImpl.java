package de.qaware.mercury.business.clienterror.impl;

import de.qaware.mercury.business.clienterror.ClientError;
import de.qaware.mercury.business.clienterror.ClientErrorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class ClientErrorServiceImpl implements ClientErrorService {
    @Override
    public void report(ClientError error) {
        log.warn("Client error report: trace id '{}', requested url '{}', http code '{}', body '{}'", error.getTraceId(), error.getRequestedUrl(), error.getHttpCode(), error.getBody());
    }
}
