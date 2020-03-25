package de.qaware.mercury.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public final class Lists {
    private Lists() {
    }

    /**
     * Maps a collection of type 'input' to a list of type 'output' using a mapper function.
     *
     * @param in     list of type 'input'
     * @param mapper mapper function which maps from 'input' to 'output'
     * @param <I>    input type
     * @param <O>    output type
     * @return list of type 'output'
     */
    public static <I, O> List<O> map(Collection<I> in, Function<I, O> mapper) {
        List<O> result = new ArrayList<>(in.size());

        for (I element : in) {
            result.add(mapper.apply(element));
        }

        return result;
    }
}
