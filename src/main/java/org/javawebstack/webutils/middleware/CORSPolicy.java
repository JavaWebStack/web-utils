package org.javawebstack.webutils.middleware;

import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.HTTPMethod;
import org.javawebstack.httpserver.handler.RequestInterceptor;

public class CORSPolicy implements RequestInterceptor {
    private final String allowedOrigin;

    public CORSPolicy(String allowedOrigin) {
        this.allowedOrigin = allowedOrigin;
    }

    public boolean intercept(Exchange exchange) {
        exchange.header("Access-Control-Allow-Origin", allowedOrigin);
        if (exchange.getMethod() == HTTPMethod.OPTIONS) {
            exchange.header("Access-Control-Allow-Headers", "*");
            exchange.header("Access-Control-Allow-Methods", "*");
            return true;
        }
        return false;
    }
}
