package de.qaware.mercury.rest.shop.dto.request;

import de.qaware.mercury.rest.validation.ValidationConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendCreateLinkDto {
    @Email(regexp = ValidationConstants.EMAIL_REGEX, flags = Pattern.Flag.CASE_INSENSITIVE)
    @NotNull
    private String email;
}
