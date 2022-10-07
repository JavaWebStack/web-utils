package org.javawebstack.webutils.config;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Mapping implements Function<String, String> {

    private final Map<String, String> mapping = new HashMap<>();
    private boolean failIfNotFound = false;

    public Mapping failIfNotFound(boolean failIfNotFound) {
        this.failIfNotFound = failIfNotFound;
        return this;
    }

    public Mapping map(String from, String to) {
        mapping.put(from, to);
        return this;
    }

    public String apply(String s) {
        return mapping.getOrDefault(s, failIfNotFound ? null : s);
    }

}
