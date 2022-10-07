package org.javawebstack.webutils.config;

import java.util.function.Function;

public class MappingTryout implements Function<String, String> {

    private final Function<String, String>[] tryout;
    private boolean failIfEqual = true;

    public MappingTryout(Function<String, String>... tryout) {
        this.tryout = tryout;
    }

    public MappingTryout failIfEqual(boolean failIfEqual) {
        this.failIfEqual = failIfEqual;
        return this;
    }

    public String apply(String s) {
        for(Function<String, String> mapping : tryout) {
            String k = mapping.apply(s);
            if(k != null && (!failIfEqual || !k.equals(s)))
                return k;
        }
        return null;
    }

}
