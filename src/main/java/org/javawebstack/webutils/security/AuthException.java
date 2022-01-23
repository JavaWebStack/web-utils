package org.javawebstack.webutils.security;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
