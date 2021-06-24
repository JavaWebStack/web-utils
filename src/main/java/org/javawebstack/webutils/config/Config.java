package org.javawebstack.webutils.config;

import java.util.*;

public class Config {

    private final Map<String, String> config = new HashMap<>();

    public Config set(String path, String key, String value) {
        String prefix = path != null && path.length() > 0 ? path + "." : "";
        return set(prefix + key.toLowerCase(Locale.ROOT), value);
    }

    public Config add(String path, Map<String, String> data, Map<String, String> mapping) {
        data.forEach((key, value) -> {
            if(mapping != null && mapping.containsKey(key))
                key = mapping.get(key);
            set(path, key, value);
        });
        return this;
    }

    public Config add(Map<String, String> data, Map<String, String> mapping) {
        return add(null, data, null);
    }

    public Config add(String path, Map<String, String> data) {
        return add(path, data, null);
    }

    public Config add(Map<String, String> data) {
        return add(null, data, null);
    }

    public Config add(String path, EnvFile envFile, Map<String, String> mapping) {
        return add(path, envFile.getValues(), mapping);
    }

    public Config add(String path, EnvFile envFile) {
        return add(path, envFile, null);
    }

    public Config add(EnvFile envFile, Map<String, String> mapping) {
        return add(null, envFile, mapping);

    }

    public Config add(EnvFile envFile) {
        return add(null, envFile, null);
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

}
