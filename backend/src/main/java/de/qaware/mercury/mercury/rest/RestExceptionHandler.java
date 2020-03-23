package de.qaware.mercury.mercury.rest;

import de.qaware.mercury.mercury.business.login.LoginException;
import de.qaware.mercury.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.mercury.business.uuid.UUIDFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private final UUIDFactory uuidFactory;

    @ExceptionHandler(ShopNotFoundException.class)
    ResponseEntity<ErrorDto> handleShopNotFoundException(ShopNotFoundException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "SHOP_NOT_FOUND", exception.getMessage());
        log.debug("Handled ShopNotFoundException with exception id {}", errorDto.getId(), exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(LoginException.class)
    ResponseEntity<ErrorDto> handleLoginException(LoginException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "AUTHENTICATION_FAILED", exception.getMessage());
        log.debug("Handled LoginException with exception id {}", errorDto.getId(), exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }
}
