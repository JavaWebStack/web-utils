package org.javawebstack.webutils;

import org.javawebstack.abstractdata.AbstractMapper;
import org.javawebstack.abstractdata.NamingPolicy;
import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.transformer.response.ResponseTransformer;

public class SerializedResponseTransformer implements ResponseTransformer {

    private final AbstractMapper mapper;
    private boolean ignoreStrings;

    public SerializedResponseTransformer() {
        this((new AbstractMapper()).setNamingPolicy(NamingPolicy.SNAKE_CASE));
    }

    public SerializedResponseTransformer(AbstractMapper mapper) {
        this.ignoreStrings = false;
        this.mapper = mapper;
    }

    public SerializedResponseTransformer ignoreStrings() {
        this.ignoreStrings = true;
        return this;
    }

    public String transform(Exchange exchange, Object object) {
        if (object instanceof byte[]) {
            return null;
        } else {
            if (this.ignoreStrings && object instanceof String)
                return null;
            String accept = exchange.header("Accept");

            if (accept != null) {
                switch (accept.toLowerCase()) {
                    case "application/x-yaml":
                    case "application/yaml":
                    case "text/yaml":
                    case "text/x-yaml":
                        exchange.contentType(accept);
                        return this.mapper.toAbstract(object).toYaml();
                    case "application/x-www-form-urlencoded":
                        exchange.contentType(accept);
                        return this.mapper.toAbstract(object).toFormDataString();
                }
            }

            exchange.contentType("application/json");
            return this.mapper.toAbstract(object).toJsonString();
        }
    }
}
