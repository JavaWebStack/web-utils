package org.javawebstack.webutils;

import org.javawebstack.framework.WebApplication;
import org.javawebstack.httpserver.HTTPServer;
import org.javawebstack.orm.exception.ORMConfigurationException;
import org.javawebstack.orm.wrapper.SQL;
import org.javawebstack.orm.wrapper.SQLDriverFactory;
import org.javawebstack.webutils.config.Config;

public interface Module {

    default void beforeSetupConfig(WebApplication application, Config config) {
    }

    default void beforeSetupModels(WebApplication application, SQL sql) throws ORMConfigurationException {
    }

    default void beforeSetupServer(WebApplication application, HTTPServer server) {
    }

    default void beforeSetupSeeding(WebApplication application) {
    }

    default void setupConfig(WebApplication application, Config config) {
    }

    default void setupDriverFactory(WebApplication application, SQLDriverFactory driverFactory) {
    }

    default void setupModels(WebApplication application, SQL sql) throws ORMConfigurationException {
    }

    default void setupServer(WebApplication application, HTTPServer server) {
    }

    default void setupSeeding(WebApplication application) {
    }

}
