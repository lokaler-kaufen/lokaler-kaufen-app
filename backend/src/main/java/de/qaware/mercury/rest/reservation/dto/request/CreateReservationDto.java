package de.qaware.mercury.rest.reservation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationDto {
    @NotBlank
    private String slotId;
    @NotBlank
    // TODO: Validate enum
    private String contactType;
    @NotBlank
    // TODO validate pattern
    private String contact;
    @NotBlank
    // TODO validate pattern
    private String name;
    // From https://www.regular-expressions.info/email.html
    @Email(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", flags = Pattern.Flag.CASE_INSENSITIVE)
    @NotNull
    private String email;
}
