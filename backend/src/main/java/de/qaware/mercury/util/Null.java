package de.qaware.mercury.util;

import org.springframework.lang.Nullable;

import java.util.function.Function;

public final class Null {
    private Null() {
    }

    /**
     * Maps 'input' to 'output' using the mapper function. Returns null as soon as input is null.
     *
     * @param input  input. May be null
     * @param mapper Mapper function which maps 'input' to 'output'. Is only called when 'input' is not null.
     * @param <I>    'input' type
     * @param <O>    'output' type
     * @return null if input is null, otherwise the mapped input
     */
    @Nullable
    public static <I, O> O map(@Nullable I input, Function<I, O> mapper) {
        if (input == null) {
            return null;
        }

        return mapper.apply(input);
    }
}
