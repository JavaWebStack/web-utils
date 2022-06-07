package org.javawebstack.webutils.middleware;

import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.handler.Middleware;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitMiddleware implements Middleware {
    private int rateLimit;
    private long millis;
    private Map<String, RateLimit> rateLimits = new ConcurrentHashMap<>();

    public RateLimitMiddleware(long millis, int rateLimit){
        this.rateLimit = rateLimit;
        this.millis = millis;
    }

    public RateLimitMiddleware(long millis){
        this(millis, 1);
    }

    public Object handle(Exchange exchange) {
        String ip = exchange.socket().getRemoteAddress();
        if (exchange.header("X-Forwarded-For") != null)
            ip = exchange.header("X-Forwarded-For");
        RateLimit rateLimit = null;
        if (rateLimits.containsKey(ip)) {
            rateLimit = rateLimits.get(ip);
            if (rateLimit.stillAlive() && rateLimit.getAndDecrease() <= 1)
                throw new RateLimitException();
        }
        if (rateLimit == null || !rateLimit.stillAlive())
            rateLimit = new RateLimit(System.currentTimeMillis() + millis, this.rateLimit);
        rateLimits.put(ip, rateLimit);
        exchange.header("X-Rate-Limit-Limit", String.valueOf(this.rateLimit));
        exchange.header("X-Rate-Limit-Remaining", String.valueOf(rateLimit.getRateLimitLeft()));
        exchange.header("X-Rate-Limit-Reset", String.valueOf(rateLimit.getTimeMillis()/1000));
        return null;
    }

    public void removeDeadRateLimits(){
        rateLimits.forEach((ip, rateLimit)-> {
            if (!rateLimit.stillAlive())
                rateLimits.remove(ip);
        });
    }

    public RateLimitMiddleware createAutoDeadRateLimitsRemover(long millis){
        new Timer().schedule(new TimerTask() {
            public void run() {
                removeDeadRateLimits();
            }
        }, millis, millis);
        return this;
    }

    public RateLimitMiddleware createAutoDeadRateLimitsRemover(){
        return createAutoDeadRateLimitsRemover(millis);
    }

    public int getRateLimit() {
        return rateLimit;
    }

    public long getMillis() {
        return millis;
    }

    public void setRateLimit(int rateLimit) {
        this.rateLimit = rateLimit;
    }

    public void setMillis(int millis) {
        this.millis = millis;
    }

    private static class RateLimit {
        private long timeMillis;
        private int rateLimitLeft;

        private RateLimit(long timeMillis, int rateLimitLeft) {
            this.timeMillis = timeMillis;
            this.rateLimitLeft = rateLimitLeft;
        }

        public int getRateLimitLeft() {
            return rateLimitLeft;
        }
        public int getAndDecrease() {
            return rateLimitLeft--;
        }

        public long getTimeMillis() {
            return timeMillis;
        }

        public boolean stillAlive(){
            return timeMillis > System.currentTimeMillis();
        }
    }

    public static class RateLimitException extends RuntimeException {}
}
