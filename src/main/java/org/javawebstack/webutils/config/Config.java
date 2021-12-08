package org.javawebstack.webutils.config;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.abstractdata.AbstractObject;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Config {

    private final Map<String, String> config = new HashMap<>();

    public Config set(String path, String key, String value) {
        String prefix = path != null && path.length() > 0 ? path + "." : "";
        return set(prefix + key.toLowerCase(Locale.ROOT), value);
    }

    public Config add(String path, Map<String, String> data, Function<String, String> mapping) {
        data.forEach((key, value) -> {
            key = mapping.apply(key);
            if(key != null)
                set(path, key, value);
        });
        return this;
    }

    public Config add(Map<String, String> data, Function<String, String> mapping) {
        return add(null, data, mapping);
    }

    public Config add(String path, Map<String, String> data) {
        return add(path, data, k -> k);
    }

    public Config add(Map<String, String> data) {
        return add(null, data, k -> k);
    }

    public Config add(String path, EnvFile envFile, Function<String, String> mapping) {
        return add(path, envFile.getValues(), mapping);
    }

    public Config add(String path, EnvFile envFile) {
        return add(path, envFile, k -> k);
    }

    public Config add(EnvFile envFile, Function<String, String> mapping) {
        return add(null, envFile, mapping);
    }

    public Config add(EnvFile envFile) {
        return add(null, envFile, k -> k);
    }

    public Config set(String key, String value) {
        config.put(key, value);
        return this;
    }

    public String get(String key, String defaultValue) {
        if (!has(key))
            return defaultValue;
        return config.get(key);
    }

    public String get(String key) {
        return get(key, null);
    }

    public AbstractObject getObject(String key) {
        String prefix = key.length() > 0 ? (key + ".") : "";
        Stream<String> s = config.keySet().stream().filter(k -> k.startsWith(prefix));
        if(s.findAny().isEmpty())
            return null;
        return AbstractElement.fromTree(s.collect(Collectors.toMap(k -> k.substring(prefix.length()).split("\\."), config::get))).object();
    }

    public Config set(String key, AbstractObject object) {
        String prefix = key.length() > 0 ? (key + ".") : "";
        config.keySet().stream().filter(k -> k.startsWith(prefix)).forEach(config::remove);
        object.toTree().forEach((k, v) -> config.put(prefix + String.join(".", k), v.toString()));
        return this;
    }

    public int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null)
            return defaultValue;
        return Integer.parseInt(value);
    }

    public boolean isEnabled(String key, boolean defaultValue) {
        String value = get(key);
        if (value == null)
            return defaultValue;
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1") || value.equalsIgnoreCase("yes"))
            return true;
        return false;
    }

    public boolean isEnabled(String key) {
        return isEnabled(key, false);
    }

    public boolean has(String key) {
        return config.containsKey(key);
    }

    public Set<String> keys() {
        return config.keySet();
    }

    public static String basicEnvMapping(String k) {
        return k.toLowerCase(Locale.ROOT).replace("_", ".");
    }

}
