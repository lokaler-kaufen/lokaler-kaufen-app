package de.qaware.mercury.mercury.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public final class Lists {
    private Lists() {
    }

    public static <I, O> List<O> map(Collection<I> in, Function<I, O> mapper) {
        List<O> result = new ArrayList<>(in.size());

        for (I element : in) {
            result.add(mapper.apply(element));
        }

        return result;
    }
}
