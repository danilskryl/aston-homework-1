package org.danilskryl.restapi.servlets.data.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseScriptRunner {
    private static final Config config = ConfigFactory.load();
    private static final String JDBC_URL = config.getString("db.url");
    private static final String USER = config.getString("db.username");
    private static final String PASSWORD = config.getString("db.password");

    public static void executeScript(String path) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            String[] queries = readScript(path).split(";");

            for (String query : queries) {
                statement.addBatch(query);
            }

            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String readScript(String scriptPath) {
        try {
            Path path = Paths.get(scriptPath);
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

