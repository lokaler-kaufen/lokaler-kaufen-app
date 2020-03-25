package de.qaware.mercury.rest.reservation.dto.request;

import de.qaware.mercury.util.validation.GuidValidation;
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
    @Pattern(regexp = GuidValidation.REGEX)
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
    @Email
    @NotNull
    private String email;
}
