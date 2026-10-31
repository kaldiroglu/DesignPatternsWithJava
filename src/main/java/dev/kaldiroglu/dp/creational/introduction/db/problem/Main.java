package dev.kaldiroglu.dp.creational.ch01.db.problem;

public class Main {
    public static void main(String[] args) {

        // Scenario 1: Basic local development connection
        DatabaseConnection devConn = new DatabaseConnection();
        devConn.setHost("localhost");
        devConn.setDatabase("myapp_dev");
        devConn.setUsername("dev_user");
        devConn.setPassword("dev_pass");

        // Scenario 2: Production connection with custom security and timeouts
        DatabaseConnection prodConn = new DatabaseConnection();
        prodConn.setHost("prod-db-cluster.company.com");
        prodConn.setPort(5432);  // PostgreSQL
        prodConn.setDatabase("myapp_production");
        prodConn.setUsername("prod_user");
        prodConn.setPassword("complex_secure_password");
        prodConn.setUseSSL(true);
        prodConn.setConnectionTimeout(10000);
        prodConn.setReadTimeout(120000);
        prodConn.setConnectionPool("HikariCP");
        prodConn.setEnableLogging(true);

        // Scenario 3: Testing connection with specific charset and no SSL
        DatabaseConnection testConn = new DatabaseConnection();
        testConn.setHost("test-server");
        testConn.setDatabase("test_db");
        testConn.setUsername("test_user");
        testConn.setPassword("test_pass");
        testConn.setUseSSL(false);
        testConn.setCharset("UTF-8");
        testConn.setAutoReconnect(false);

        // Scenario 4: Analytics connection with custom timezone and logging
        DatabaseConnection analyticsConn = new DatabaseConnection();
        analyticsConn.setHost("analytics-db");
        analyticsConn.setDatabase("warehouse");
        analyticsConn.setUsername("analytics_user");
        analyticsConn.setPassword("analytics_pass");
        analyticsConn.setTimezone("UTC");
        analyticsConn.setEnableLogging(true);
        analyticsConn.setLogLevel("DEBUG");
        analyticsConn.setReadTimeout(300000); // 5 minutes for long queries
    }
}
