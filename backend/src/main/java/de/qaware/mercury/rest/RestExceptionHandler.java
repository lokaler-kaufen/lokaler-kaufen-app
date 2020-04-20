package de.qaware.mercury.rest;

import de.qaware.mercury.business.image.ImageNotFoundException;
import de.qaware.mercury.business.image.InvalidImageIdException;
import de.qaware.mercury.business.location.impl.LocationNotFoundException;
import de.qaware.mercury.business.login.LoginException;
import de.qaware.mercury.business.login.ShopLoginNotFoundException;
import de.qaware.mercury.business.reservation.InvalidReservationIdException;
import de.qaware.mercury.business.reservation.InvalidSlotIdException;
import de.qaware.mercury.business.reservation.ReservationFailedException;
import de.qaware.mercury.business.reservation.ReservationNotFoundException;
import de.qaware.mercury.business.shop.InvalidBreakException;
import de.qaware.mercury.business.shop.InvalidShopIdException;
import de.qaware.mercury.business.shop.ShopAlreadyExistsException;
import de.qaware.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.rest.shop.InvalidTimeException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private final UUIDFactory uuidFactory;

    @ExceptionHandler(ShopNotFoundException.class)
    public ResponseEntity<ErrorDto> handleShopNotFoundException(ShopNotFoundException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "SHOP_NOT_FOUND", exception.getMessage());
        log.debug("Handled ShopNotFoundException with exception id {}", errorDto.getId(), exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(ShopLoginNotFoundException.class)
    public ResponseEntity<ErrorDto> handleShopLoginNotFoundException(ShopLoginNotFoundException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "SHOP_LOGIN_NOT_FOUND", exception.getMessage());
        log.debug("Handled ShopLoginNotFoundException with exception id {}", errorDto.getId(), exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(LocationNotFoundException.class)
    public ResponseEntity<ErrorDto> handleLocationNotFoundException(LocationNotFoundException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "LOCATION_NOT_FOUND", exception.getMessage());
        log.debug("Handled LocationNotFoundException with exception id {}", errorDto.getId(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ErrorDto> handleReservationNotFoundException(ReservationNotFoundException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "RESERVATION_NOT_FOUND", exception.getMessage());
        log.debug("Handled ReservationNotFoundException with exception id {}", errorDto.getId(), exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ErrorDto> handleLoginException(LoginException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "AUTHENTICATION_FAILED", exception.getMessage());
        log.debug("Handled LoginException with exception id {}", errorDto.getId(), exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

    @ExceptionHandler(InvalidShopIdException.class)
    public ResponseEntity<ErrorDto> handleInvalidShopIdException(InvalidShopIdException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "INVALID_SHOP_ID", exception.getMessage());
        log.debug("Handled InvalidShopIdException with exception id {}", errorDto.getId(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(InvalidReservationIdException.class)
    public ResponseEntity<ErrorDto> handleInvalidReservationIdException(InvalidReservationIdException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "INVALID_RESERVATION_ID", exception.getMessage());
        log.debug("Handled InvalidReservationIdException with exception id {}", errorDto.getId(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "REQUEST_VALIDATION_FAILED", exception.getMessage());
        log.debug("Handled ConstraintViolationException with exception id {}", errorDto.getId(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(InvalidSlotIdException.class)
    public ResponseEntity<ErrorDto> handleInvalidSlotIdException(InvalidSlotIdException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "INVALID_SLOT_ID", exception.getMessage());
        log.debug("Handled InvalidSlotIdException with exception id {}", errorDto.getId(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(InvalidImageIdException.class)
    public ResponseEntity<ErrorDto> handleInvalidImageIdException(InvalidImageIdException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "INVALID_IMAGE_ID", exception.getMessage());
        log.debug("Handled InvalidImageIdException with exception id {}", errorDto.getId(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(InvalidBreakException.class)
    public ResponseEntity<ErrorDto> handleInvalidBreakException(InvalidBreakException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "INVALID_BREAK", exception.getMessage());
        log.debug("Handled InvalidBreakException with exception id {}", errorDto.getId(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ErrorDto> handleImageNotFoundException(ImageNotFoundException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "IMAGE_NOT_FOUND", exception.getMessage());
        log.debug("Handled ImageNotFoundException with exception id {}", errorDto.getId(), exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(InvalidTimeException.class)
    public ResponseEntity<ErrorDto> handleInvalidTimeException(InvalidTimeException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "INVALID_TIME", exception.getMessage());
        log.debug("Handled InvalidTimeException with exception id {}", errorDto.getId(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(ShopAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleShopAlreadyExistsException(ShopAlreadyExistsException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "SHOP_ALREADY_EXISTS", exception.getMessage());
        log.debug("Handled ShopAlreadyExistsException with exception id {}", errorDto.getId(), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }

    @ExceptionHandler(ReservationFailedException.class)
    public ResponseEntity<ErrorDto> handleReservationFailedException(ReservationFailedException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "RESERVATION_FAILED", exception.getMessage());
        log.debug("Handled ReservationFailedException with exception id {}", errorDto.getId(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        // Spring just returns 400 Bad Request in these cases without a useful body
        // We format a nice looking error message so that the caller knows what is invalid
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessage
                .append(error.getField()).append(": ")
                .append(error.getDefaultMessage())
                .append("\n");
        }

        ErrorDto errorDto = ErrorDto.of(uuidFactory, "REQUEST_VALIDATION_FAILED", errorMessage.toString());
        log.debug("Handled MethodArgumentNotValidException with exception id {}", errorDto.getId(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }
}
