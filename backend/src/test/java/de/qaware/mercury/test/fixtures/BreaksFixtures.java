package de.qaware.mercury.test.fixtures;

import de.qaware.mercury.business.shop.Breaks;

import java.time.LocalTime;
import java.util.Set;

public final class BreaksFixtures {
    private BreaksFixtures() {
    }

    public static Breaks monday(LocalTime start, LocalTime end) {
        return new Breaks(
            Set.of(Breaks.Break.of(start, end)), Set.of(), Set.of(), Set.of(), Set.of(), Set.of(), Set.of()
        );
    }
}
