package de.qaware.mercury.rest.clienterror

import de.qaware.mercury.business.clienterror.ClientError
import de.qaware.mercury.business.clienterror.ClientErrorService
import de.qaware.mercury.rest.clienterror.dto.request.ClientErrorDto
import spock.lang.Specification
import spock.lang.Subject

class ClientErrorControllerSpec extends Specification {

    @Subject
    private ClientErrorController controller

    private ClientErrorService clientErrorService

    def setup() {
        clientErrorService = Mock(ClientErrorService)
        controller = new ClientErrorController(clientErrorService)
    }

    def "Stores error report"() {
        setup:
        ClientErrorDto clientErrorDto = new ClientErrorDto("traceid", "http://requestedurl.com", 200, "body")
        ClientError clientError = clientErrorDto.toClientError()

        when:
        controller.reportError(clientErrorDto)

        then:
        1 * clientErrorService.report(clientError);
    }

}
