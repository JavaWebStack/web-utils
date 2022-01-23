package org.javawebstack.webutils.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.javawebstack.abstractdata.AbstractObject;
import org.javawebstack.httpserver.Exchange;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;

public abstract class AbstractAuthSystem {
    private Algorithm algorithm;
    private int tokenExpire = 3600;

    public AbstractAuthSystem(String secret) {
        this(Algorithm.HMAC256(secret));
    }

    public AbstractAuthSystem(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    protected abstract Optional<IUser> getUserByUsername(String username);
    protected AbstractObject successMessage(String token) {
        return new AbstractObject()
                .set("success", true)
                .set("token", token);
    }

    public void setTokenExpire(int tokenExpire) {
        this.tokenExpire = tokenExpire;
    }

    public int getTokenExpire() {
        return tokenExpire;
    }

    public AbstractObject loginHandler(Exchange exchange) {
        AbstractObject body = exchange.body(AbstractObject.class);
        Optional<IUser> oUser = getUserByUsername(body.string("username"));
        if (!oUser.isPresent())
            throw new AuthException("Wrong credentials");
        IUser user = oUser.get();
        if (!user.checkPassword(body.string("password")))
            throw new AuthException("Wrong credentials");
        return successMessage(signToken(user));
    }

    public boolean tokenHandler (Exchange exchange) {
        String bearer = exchange.bearerAuth();
        if (bearer != null) {
            DecodedJWT jwt = JWT.require(algorithm).acceptExpiresAt(System.currentTimeMillis()).build().verify(bearer);
            if (jwt != null)
                exchange.attrib("jwt", jwt);
        }
        return false;
    }

    public Object authMiddleware (Exchange exchange) {
        if (exchange.attrib("jwt") == null) {
            exchange.status(401);
            throw new AuthException("Authentication required");
        }
        return null;
    }

    public IUser currentUser () {
        Exchange exchange = Exchange.current();
        if (exchange == null)
            throw new RuntimeException("Exchange not available in current Thread!");
        IUser user = exchange.attrib("user");
        if (user == null) {
            DecodedJWT jwt = exchange.attrib("jwt");
            if (jwt == null)
                throw new RuntimeException("JWT not found! Is the tokenHandler in place?");
            user = getUserByUsername(jwt.getSubject()).get();
            exchange.attrib("user", user);
        }
        return user;
    }

    protected String signToken(IUser user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(Date.from(Instant.now().plusSeconds(tokenExpire)))
                .sign(algorithm);
    }
}
