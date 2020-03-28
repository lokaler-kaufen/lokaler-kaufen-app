package de.qaware.mercury.util;

import java.util.HashMap;
import java.util.List;
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
    public static <I, O, V> java.util.Map<O, V> mapKeys(java.util.Map<I, V> input, Function<I, O> mapper) {
        java.util.Map<O, V> result = new HashMap<>(input.size());

        for (java.util.Map.Entry<I, V> entry : input.entrySet()) {
            result.put(mapper.apply(entry.getKey()), entry.getValue());
        }

        return result;
    }

    /**
     * Maps the entries of the given input to an output map by applying two mapper functions.
     *
     * @param input       input map
     * @param keyMapper   mapper function which is applied to the keys of the input map
     * @param valueMapper mapper function which is applied to the values of the input map
     * @param <I>         input type
     * @param <O>         output key type
     * @param <W>         output value type
     * @return transformed map
     */
    public static <I, O, W> Map<O, W> mapList(List<I> input, Function<I, O> keyMapper, Function<I, W> valueMapper) {
        Map<O, W> result = new HashMap<>(input.size());
        for (I entry : input) {
            result.put(keyMapper.apply(entry), valueMapper.apply(entry));
        }
        return result;
    }
}
