package dev.kaldiroglu.dp.creational.ch01.db.better;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection devConn =
                DatabaseConnection.builder("localhost", "myapp_dev", "dev_user", "dev_pass")
                        .build();

        DatabaseConnection prodConn =
                DatabaseConnection.builder("prod-db-cluster.company.com", "myapp_production", "prod_user", "complex_secure_password")
                        .port(5432) // PostgreSQL
                        .useSSL(true)
                        .connectionTimeout(10000)
                        .readTimeout(120000)
                        .connectionPool("HikariCP")
                        .enableLogging(true)
                        .build();

        DatabaseConnection testConn =
                DatabaseConnection.builder("test-server", "test_db", "test_user", "test_pass")
                        .useSSL(false)
                        .charset("UTF-8")
                        .autoReconnect(false)
                        .build();

        DatabaseConnection analyticsConn =
                DatabaseConnection.builder("analytics-db", "warehouse", "analytics_user", "analytics_pass")
                        .timezone("UTC")
                        .enableLogging(true)
                        .logLevel("DEBUG")
                        .readTimeout(300000) // 5 minutes for long queries
                        .build();
    }
}
