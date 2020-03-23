package de.qaware.mercury.mercury.rest.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationDto {
    @NotBlank
    // TODO: Validate pattern
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
