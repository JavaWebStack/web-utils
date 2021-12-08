package org.javawebstack.webutils.config;

import java.util.Locale;
import java.util.function.Function;

public class ArrayMapping implements Function<String, String> {

    private final String prefix;
    private final Function<String, String> subMapping;
    private boolean lowerCaseIndex = true;

    public ArrayMapping(String prefix, Function<String, String> subMapping) {
        this.prefix = prefix;
        this.subMapping = subMapping;
    }

    public ArrayMapping lowerCaseIndex(boolean lowerCaseIndex) {
        this.lowerCaseIndex = lowerCaseIndex;
        return this;
    }

    public String apply(String s) {
        if(!s.startsWith(prefix))
            return null;
        String[] spl = s.substring(prefix.length()).split("_", 2);
        if(spl.length != 2)
            return null;
        String sk = subMapping.apply(spl[1]);
        if(sk == null)
            return null;
        return (lowerCaseIndex ? spl[0].toLowerCase(Locale.ROOT) : spl[0]) + "." + sk;
    }

}
