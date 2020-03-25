package de.qaware.mercury.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public final class Sets {
    private Sets() {
    }

    /**
     * Maps a collection of type 'input' to a set of type 'output' using a mapper function.
     *
     * @param in     collection of type 'input'
     * @param mapper mapper function which maps from 'input' to 'output'
     * @param <I>    input type
     * @param <O>    output type
     * @return set of type 'output'
     */
    public static <I, O> Set<O> map(Collection<I> in, Function<I, O> mapper) {
        Set<O> result = new HashSet<>(in.size());

        for (I element : in) {
            result.add(mapper.apply(element));
        }

        return result;
    }
}
