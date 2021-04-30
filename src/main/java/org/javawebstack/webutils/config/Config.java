package org.javawebstack.webutils.config;

import java.util.*;

public class Config {

    private final Map<String, String> config = new HashMap<>();

    public void set(String path, String key, String value) {
        String prefix = path != null && path.length() > 0 ? path + "." : "";
        set(prefix + key.toLowerCase(Locale.ROOT), value);
    }

    public void add(String path, Map<String, String> data, Map<String, String> mapping) {
        data.forEach((key, value) -> {
            if(mapping != null && mapping.containsKey(key))
                key = mapping.get(key);
            set(path, key, value);
        });
    }

    public void add(Map<String, String> data, Map<String, String> mapping) {
        add(null, data, null);
    }

    public void add(String path, Map<String, String> data) {
        add(path, data, null);
    }

    public void add(Map<String, String> data) {
        add(null, data, null);
    }

    public void add(String path, EnvFile envFile, Map<String, String> mapping) {
        add(path, envFile.getValues(), mapping);
    }

    public void add(String path, EnvFile envFile) {
        add(path, envFile, null);
    }

    public void add(EnvFile envFile, Map<String, String> mapping) {
        add(null, envFile, mapping);
    }

    public void add(EnvFile envFile) {
        add(null, envFile, null);
    }

    public void set(String key, String value) {
        config.put(key, value);
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
