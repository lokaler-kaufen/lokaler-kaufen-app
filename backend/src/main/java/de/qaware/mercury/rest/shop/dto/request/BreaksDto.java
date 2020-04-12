package de.qaware.mercury.rest.shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreaksDto {
    @NotNull
    private Set<String> slotIds;
}
