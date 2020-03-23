package de.qaware.mercury.mercury.rest.shop.dto;

import de.qaware.mercury.mercury.business.shop.DayConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayDto {
    @NotBlank
    // TODO: Validate pattern
    private String start;
    @NotBlank
    // TODO: Validate pattern
    private String end;

    public DayConfig toSlot() {
        return new DayConfig(parse(start), parse(end));
    }

    private LocalTime parse(String time) {
        String[] parts = time.split(":");
        return LocalTime.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }
}