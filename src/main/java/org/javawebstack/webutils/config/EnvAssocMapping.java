package org.javawebstack.webutils.config;

import java.util.Locale;
import java.util.function.Function;

public class EnvAssocMapping implements Function<String, String> {

    private final String prefix;
    private final Function<String, String> subMapping;

    public EnvAssocMapping(String prefix, Function<String, String> subMapping) {
        this.prefix = prefix;
        this.subMapping = subMapping;
    }

    public String apply(String s) {
        if(!s.startsWith(prefix+ "_"))
            return null;
        String[] spl = s.substring(prefix.length() + 1).split("_", 2);
        if(spl.length != 2)
            return null;
        String sk = subMapping.apply(spl[1]);
        if(sk == null)
            return null;
        return prefix.toLowerCase(Locale.ROOT) + "." + spl[0].toLowerCase(Locale.ROOT) + "." + sk;
    }

}
