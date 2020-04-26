package de.qaware.mercury.business.email.impl;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@ConfigurationProperties("mercury.ical")
@Validated
class ICalendarConfigurationProperties {
    @NotBlank
    private String organizerName;
    @NotBlank
    private String organizerEmail;
}
