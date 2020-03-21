package de.qaware.mercury.mercury.util;

import java.util.HashMap;
import java.util.function.Function;

public final class Maps {
    public static <I, O, V> java.util.Map<O, V> mapValues(java.util.Map<I, V> input, Function<I, O> mapper) {
        java.util.Map<O, V> result = new HashMap<>(input.size());

        for (java.util.Map.Entry<I, V> entry : input.entrySet()) {
            result.put(mapper.apply(entry.getKey()), entry.getValue());
        }

        return result;
    }
}
