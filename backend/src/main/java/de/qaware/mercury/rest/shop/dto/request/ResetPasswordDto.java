package de.qaware.mercury.rest.shop.dto.request;

import de.qaware.mercury.business.validation.Validation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDto {
    @NotBlank
    @Size(min = Validation.MIN_PASSWORD_LENGTH, max = Validation.MAX_PASSWORD_LENGTH)
    private String password;
}
