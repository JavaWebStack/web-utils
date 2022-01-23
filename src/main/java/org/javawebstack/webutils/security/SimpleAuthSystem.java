package org.javawebstack.webutils.security;

import org.javawebstack.orm.ORM;
import org.javawebstack.orm.exception.ORMConfigurationException;
import org.javawebstack.orm.wrapper.SQL;

import java.util.Optional;

public class SimpleAuthSystem extends AbstractAuthSystem {
    public SimpleAuthSystem(String secret, SQL sql) {
        super(secret);
        try {
            ORM.register(SimpleUser.class, sql);
        } catch (ORMConfigurationException e) {
            e.printStackTrace();
        }
    }

    protected Optional<IUser> getUserByUsername(String username) {
        return Optional.ofNullable(ORM.repo(SimpleUser.class).where("username", username).first());
    }
}
