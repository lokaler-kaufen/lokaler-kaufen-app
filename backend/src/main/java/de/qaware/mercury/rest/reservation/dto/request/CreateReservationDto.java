package de.qaware.mercury.rest.reservation.dto.request;

import de.qaware.mercury.util.validation.Validation;
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
@SuppressWarnings("java:S4784") // JDK since 9 has additional protection against ReDos attacks
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
    @Email(regexp = Validation.EMAIL_REGEX, flags = Pattern.Flag.CASE_INSENSITIVE)
    @NotNull
    private String email;
}
