package org.javawebstack.webutils.config;

import org.javawebstack.webutils.util.IO;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EnvFile {

    private Map<String, String> values;

    public EnvFile(String source) {
        values = parse(source);
    }

    public EnvFile(File file) {
        try {
            values = parse(IO.readTextFile(file));
        } catch (IOException ex) {
            values = new HashMap<>();
        }
    }

    public EnvFile(Map<String, String> values) {
        this.values = values;
    }

    public EnvFile() {
        this(new HashMap<>());
    }

    public EnvFile withVariables() {
        values.putAll(System.getenv());
        return this;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public String toString() {
        return values.entrySet().stream().map(e -> escape(e.getKey()) + "=" + escape(e.getValue())).collect(Collectors.joining("\n")) + "\n";
    }

    private static String escape(String source) {
        if(source.contains(" ") || source.contains("\"") || source.contains("#"))
            return "\"" + source.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
        return source;
    }

    private static String parseQuotes(String source) {
        if(source == null || source.length() == 0 || source.charAt(0) != '"')
            return null;
        for(int i=1; i<source.length(); i++) {
            if(source.charAt(i) == '"')
                return source.substring(0, i+1);
            if(source.charAt(i) == '\\') {
                if(i+1 == source.length())
                    return null;
                if(source.charAt(i+1) == '"')
                    i++;
            }
        }
        return null;
    }

    private static Map<String, String> parse(String source) {
        Map<String, String> result = new HashMap<>();
        for(String line : source.replace("\r", "").split("\n")) {
            line = line.trim();
            if(line.length() == 0)
                continue;
            String key;
            if(line.startsWith("\"")) {
                key = parseQuotes(line);
                if(key == null)
                    return null;
                line = line.substring(key.length()).trim();
                key = key.replace("\\\"", "\"").replace("\\\\", "\\");
                if(line.startsWith("#")) {
                    result.put(key, "");
                    continue;
                }
                if(!line.startsWith("="))
                    return null;
            } else {
                key = line.split("=", 2)[0];
                if(key.contains("#")) {
                    result.put(key.split("#")[0], "");
                    continue;
                }
                line = line.substring(key.length()+1);
                if(line.length() == 0) {
                    result.put(key, "");
                    continue;
                }
            }
            line = line.trim();
            if(line.startsWith("\"")) {
                String value = parseQuotes(line);
                if(value == null)
                    return null;
                line = line.substring(value.length());
                if(line.length() != 0 && !line.startsWith("#"))
                    return null;
                result.put(key, value.replace("\\\"", "\"").replace("\\\\", "\\"));
            } else {
                result.put(key, line.split("#")[0]);
            }
        }
        return result;
    }

}
