package de.qaware.mercury.mercury.business.reservation.impl;

import de.qaware.mercury.mercury.business.reservation.Interval;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class IntervalTest {
    @Test
    void no_overlap_i1_before_i2() {
        Interval i1 = Interval.of(time(1), time(2));
        Interval i2 = Interval.of(time(3), time(4));

        assertThat(i1.overlaps(i2)).isFalse();
        assertThat(i2.overlaps(i1)).isFalse();
    }

    @Test
    void no_overlap_i1_after_i2() {
        Interval i1 = Interval.of(time(3), time(4));
        Interval i2 = Interval.of(time(1), time(2));

        assertThat(i1.overlaps(i2)).isFalse();
        assertThat(i2.overlaps(i1)).isFalse();
    }

    @Test
    void overlap_i1_is_i2() {
        Interval i1 = Interval.of(time(1), time(2));
        Interval i2 = Interval.of(time(1), time(2));

        assertThat(i1.overlaps(i2)).isTrue();
        assertThat(i2.overlaps(i1)).isTrue();
    }

    @Test
    void overlap_i1_overlaps_i2_in_front() {
        Interval i1 = Interval.of(time(1), time(3));
        Interval i2 = Interval.of(time(2), time(4));

        assertThat(i1.overlaps(i2)).isTrue();
        assertThat(i2.overlaps(i1)).isTrue();
    }

    @Test
    void overlap_i1_overlaps_i2_in_back() {
        Interval i1 = Interval.of(time(2), time(4));
        Interval i2 = Interval.of(time(1), time(3));

        assertThat(i1.overlaps(i2)).isTrue();
        assertThat(i2.overlaps(i1)).isTrue();
    }

    @Test
    void no_overlap_i1_cuddles_i2_in_front() {
        Interval i1 = Interval.of(time(1), time(2));
        Interval i2 = Interval.of(time(2), time(3));

        assertThat(i1.overlaps(i2)).isFalse();
        assertThat(i2.overlaps(i1)).isFalse();
    }

    @Test
    void no_overlap_i1_cuddles_i2_in_back() {
        Interval i1 = Interval.of(time(2), time(3));
        Interval i2 = Interval.of(time(1), time(2));

        assertThat(i1.overlaps(i2)).isFalse();
        assertThat(i2.overlaps(i1)).isFalse();
    }

    private LocalDateTime time(int num) {
        return LocalDateTime.of(2020, 1, 1, num, 0);
    }
}