package org.javawebstack.webutils.middleware;

import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.handler.RequestInterceptor;

public class MultipartPolicy implements RequestInterceptor {

    private final String location;

    public MultipartPolicy(String location) {
        this.location = location;
    }

    public boolean intercept(Exchange exchange) {
        /*
        if (location != null)
            exchange.enableMultipart(location);
        */
        return false;
    }

}
