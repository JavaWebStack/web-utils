package org.javawebstack.webutils.middleware;

import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.handler.RequestHandler;

import java.util.function.BiFunction;

public class ModelBindCheckMiddleware implements RequestHandler {

    private final BiFunction<Exchange, String, Object> handler;

    public ModelBindCheckMiddleware() {
        this(ModelBindCheckMiddleware::defaultHandler);
    }

    public ModelBindCheckMiddleware(BiFunction<Exchange, String, Object> handler) {
        this.handler = handler;
    }

    public Object handle(Exchange exchange) {
        for (String key : exchange.getPathVariables().keySet()) {
            if (exchange.getPathVariables().get(key) == null)
                return handler.apply(exchange, key);
        }
        return null;
    }

    private static Object defaultHandler(Exchange exchange, String key) {
        exchange.status(404);
        return "Not Found";
    }

}
