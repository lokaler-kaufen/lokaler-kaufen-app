package de.qaware.mercury.mercury.rest.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequestDto {
    @Email
    @NotNull
    String email;
}
