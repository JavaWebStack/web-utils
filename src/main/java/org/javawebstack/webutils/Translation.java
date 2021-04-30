package org.javawebstack.webutils;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.abstractdata.AbstractObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Translation {

    private final Map<String, String> messages = new HashMap<>();

    public Translation(String resourcePath) {
        this(ClassLoader.getSystemClassLoader(), resourcePath);
    }

    public Translation(ClassLoader loader, String resourcePath) {
        this(readResource(loader, resourcePath));
    }

    public Translation(File file) {
        this(readFile(file));
    }

    public Translation(AbstractObject object) {
        addMessages("", object);
    }

    private void addMessages(String prefix, AbstractObject object) {
        for (String key : object.keys()) {
            String fullKey = prefix + (prefix.length() > 0 ? "." : "") + key;
            if (object.get(key).isObject()) {
                addMessages(fullKey, object.get(key).object());
                continue;
            }
            messages.put(fullKey, object.get(key).string());
        }
    }

    public String getMessage(String key, Object... values) {
        String message = messages.get(key);
        if(message == null)
            message = key;
        for (int i = 0; i < values.length; i++)
            message = message.replace("{" + i + "}", values[i] == null ? "null" : values[i].toString());
        return message;
    }

    private static AbstractObject readResource(ClassLoader loader, String path) {
        InputStream stream = loader.getResourceAsStream(path);
        if (stream == null)
            return new AbstractObject();
        try {
            return parseFile(path, IO.readTextResource(loader, path));
        } catch (Exception exception) {
            return new AbstractObject();
        }
    }

    private static AbstractObject readFile(File file) {
        try {
            return parseFile(file.getName(), IO.readTextFile(file));
        } catch (IOException ignored) {
            return new AbstractObject();
        }
    }

    private static AbstractObject parseFile(String fileName, String content) {
        if (content == null)
            return new AbstractObject();
        if (fileName.endsWith(".yaml") || fileName.endsWith(".yml"))
            return AbstractElement.fromYaml(content, true).object();
        return AbstractElement.fromJson(content).object();
    }

}