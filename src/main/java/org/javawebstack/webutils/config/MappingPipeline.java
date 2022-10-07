package org.javawebstack.webutils.config;

import java.util.function.Function;

public class MappingPipeline implements Function<String, String> {

    private final Function<String, String>[] pipeline;

    public MappingPipeline(Function<String, String>... pipeline) {
        this.pipeline = pipeline;
    }

    public String apply(String s) {
        String k = s;
        for(Function<String, String> mapping : pipeline)
            k = mapping.apply(k);
        return k == null ? s : k;
    }
}
