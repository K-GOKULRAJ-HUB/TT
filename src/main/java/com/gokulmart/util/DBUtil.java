package com.gokulmart.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.File;

public class DBUtil {

    private static HikariDataSource dataSource;

    static {
        try {
            Class.forName("org.h2.Driver");
            HikariConfig config = new HikariConfig();
            
            // Ensure db directory exists
            File dbDir = new File("db");
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }

            String dbUrl = System.getenv("JDBC_DATABASE_URL");
            if (dbUrl == null || dbUrl.trim().isEmpty()) {
                dbUrl = "jdbc:h2:./db/gokulmart;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE";
            }

            config.setJdbcUrl(dbUrl);
            config.setUsername("sa");
            config.setPassword("");
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(10000);
            config.setPoolName("GokulMartHikariCP");

            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("Failed to initialize HikariCP connection pool: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
