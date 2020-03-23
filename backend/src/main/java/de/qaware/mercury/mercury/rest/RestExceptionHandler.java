package de.qaware.mercury.mercury.rest;

import de.qaware.mercury.mercury.business.login.LoginException;
import de.qaware.mercury.mercury.business.shop.InvalidShopIdException;
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

import javax.validation.ConstraintViolationException;

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

    @ExceptionHandler(InvalidShopIdException.class)
    ResponseEntity<ErrorDto> handleInvalidShopIdException(InvalidShopIdException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "INVALID_SHOP_ID", exception.getMessage());
        log.debug("Handled InvalidShopIdException with exception id {}", errorDto.getId(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "REQUEST_VALIDATION_FAILED", exception.getMessage());
        log.debug("Handled ConstraintViolationException with exception id {}", errorDto.getId(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }
}
