package de.qaware.mercury.mercury.rest.shop.dto;

import de.qaware.mercury.mercury.business.shop.DayConfig;
import de.qaware.mercury.mercury.rest.shop.InvalidTimeException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DayDtoTest {
    @Test
    void success() {
        DayConfig dayConfig = new DayDto("10:00", "23:59").toSlot();

        assertThat(dayConfig.getStart()).isEqualTo(LocalTime.of(10, 0));
        assertThat(dayConfig.getEnd()).isEqualTo(LocalTime.of(23, 59));
    }

    @Test
    void fail() {
        assertThatThrownBy(() -> new DayDto("foobar", "23:59").toSlot())
            .isInstanceOf(InvalidTimeException.class)
            .hasMessageContaining("'foobar'");
    }
}