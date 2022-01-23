package org.javawebstack.webutils.security;

import com.auth0.jwt.algorithms.Algorithm;
import org.javawebstack.orm.Model;
import org.javawebstack.orm.annotation.Column;
import org.javawebstack.orm.util.KeyType;


public class SimpleUser extends Model implements IUser {

    @Column(id = true, ai = true, key = KeyType.PRIMARY)
    private long id;
    @Column
    private String username;
    @Column
    private String password;

    public SimpleUser() {}

    public SimpleUser(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
