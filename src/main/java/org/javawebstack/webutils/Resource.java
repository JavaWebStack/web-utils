package org.javawebstack.webutils;

import java.util.*;
import java.util.stream.Collectors;

public interface Resource<T> {

    void map(T source, Context context);

    static <T, R extends Resource<T>> R make(Class<R> type, T source) {
        return make(type, new Context(), source);
    }

    static <T, R extends Resource<T>> List<R> make(Class<R> type, List<T> source) {
        return make(type, new Context(), source);
    }

    static <T, R extends Resource<T>> List<R> make(Class<R> type, T... source) {
        return make(type, new Context(), source);
    }

    static <T, R extends Resource<T>> R make(Class<R> type, Context context, T source) {
        if (source == null)
            return null;
        try {
            R resource = type.newInstance();
            resource.map(source, context);
            return resource;
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    static <T, R extends Resource<T>> List<R> make(Class<R> type, Context context, List<T> source) {
        return source.stream().map(s -> make(type, context, s)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    static <T, R extends Resource<T>> List<R> make(Class<R> type, Context context, T... source) {
        return make(type, context, Arrays.asList(source));
    }

    class Context {
        final Map<String, Object> attribs = new HashMap<>();
        public <T> T attrib(String key) {
            return (T) attribs.get(key);
        }
        public Context attrib(String key, Object value) {
            attribs.put(key, value);
            return this;
        }
    }

}
