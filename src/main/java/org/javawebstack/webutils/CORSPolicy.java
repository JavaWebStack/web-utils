package org.javawebstack.webutils;

import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.handler.RequestInterceptor;
import org.javawebstack.httpserver.helper.HttpMethod;

public class CORSPolicy implements RequestInterceptor {
    private final String allowedOrigin;

    public CORSPolicy(String allowedOrigin) {
        this.allowedOrigin = allowedOrigin;
    }

    public boolean intercept(Exchange exchange) {
        exchange.header("Access-Control-Allow-Origin", allowedOrigin);
        if (exchange.getMethod() == HttpMethod.OPTIONS) {
            exchange.header("Access-Control-Allow-Headers", "*");
            exchange.header("Access-Control-Allow-Methods", "*");
            return true;
        }
        return false;
    }
}
