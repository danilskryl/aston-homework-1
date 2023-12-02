package org.danilskryl.restapi.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static final Config config = ConfigFactory.load();
    private static final String JDBC_URL = config.getString("db.url");
    private static final String DRIVER_NAME = config.getString("db.driver");
    private static final String USERNAME = config.getString("db.username");
    private static final String PASSWORD = config.getString("db.password");
    private static HikariDataSource dataSource;

    private ConnectionPool() {
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (dataSource == null) {
            initializeDataSource();
        }
        return dataSource.getConnection();
    }

    private static void initializeDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setDriverClassName(DRIVER_NAME);
        config.setAutoCommit(false);

        dataSource = new HikariDataSource(config);
    }
}
