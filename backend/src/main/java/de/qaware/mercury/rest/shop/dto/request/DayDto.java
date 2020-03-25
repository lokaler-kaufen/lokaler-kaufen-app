package de.qaware.mercury.rest.shop.dto.request;

import de.qaware.mercury.business.shop.DayConfig;
import de.qaware.mercury.rest.shop.InvalidTimeException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayDto {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @NotBlank
    // TODO: Validate pattern
    private String start;
    @NotBlank
    // TODO: Validate pattern
    private String end;

    public static DayDto of(DayConfig dayConfig) {
        return new DayDto(dayConfig.getStart().toString(), dayConfig.getEnd().toString());
    }

    public DayConfig toSlot() {
        return new DayConfig(parse(start), parse(end));
    }

    private LocalTime parse(String time) {
        try {
            return LocalTime.parse(time, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidTimeException(time, e);
        }
    }
}