package de.qaware.mercury.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class Maps {
    private Maps() {
    }

    /**
     * Maps the keys of the given input map to an output map by applying a mapper function.
     *
     * @param input  input map
     * @param mapper mapper function which is applied to the keys of the input map
     * @param <I>    input type
     * @param <O>    output type
     * @param <V>    value type
     * @return transformed map
     */
    public static <I, O, V> Map<O, V> mapKeys(Map<I, V> input, Function<I, O> mapper) {
        Map<O, V> result = new HashMap<>(input.size());

        for (Map.Entry<I, V> entry : input.entrySet()) {
            result.put(mapper.apply(entry.getKey()), entry.getValue());
        }

        return result;
    }

    public static <I, O, V, W> Map<O, W> map(Map<I, V> input, Function<I, O> keyMapper, Function<V, W> valueMapper) {
        Map<O, W> result = new HashMap<>(input.size());

        for (Map.Entry<I, V> entry : input.entrySet()) {
            result.put(keyMapper.apply(entry.getKey()), valueMapper.apply(entry.getValue()));
        }

        return result;
    }
}
