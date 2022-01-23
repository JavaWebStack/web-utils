package org.javawebstack.webutils.security;

import org.javawebstack.httpserver.Exchange;
import org.javawebstack.webutils.crypt.BCrypt;

public interface IUser {
    String getUsername();

    String getPassword();

    default boolean checkPassword (String password) {
        return BCrypt.check(getPassword(), password);
    }
}
