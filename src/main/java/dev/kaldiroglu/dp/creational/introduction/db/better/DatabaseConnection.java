package dev.kaldiroglu.dp.creational.ch01.db.better;

public class DatabaseConnection {
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final boolean useSSL;
    private final int connectionTimeout;
    private final int readTimeout;
    private final String charset;
    private final boolean autoReconnect;
    private final int maxRetries;
    private final String connectionPool;
    private final boolean enableLogging;
    private final String logLevel;
    private final String timezone;

    // ... existing code ...

    private DatabaseConnection(Builder b) {
        this.host = b.host;
        this.port = b.port;
        this.database = b.database;
        this.username = b.username;
        this.password = b.password;
        this.useSSL = b.useSSL;
        this.connectionTimeout = b.connectionTimeout;
        this.readTimeout = b.readTimeout;
        this.charset = b.charset;
        this.autoReconnect = b.autoReconnect;
        this.maxRetries = b.maxRetries;
        this.connectionPool = b.connectionPool;
        this.enableLogging = b.enableLogging;
        this.logLevel = b.logLevel;
        this.timezone = b.timezone;
    }

    public static Builder builder(String host, String database, String username, String password) {
        return new Builder(host, database, username, password);
    }

    public static final class Builder {
        // required
        private final String host;
        private final String database;
        private final String username;
        private final String password;

        // optional with defaults
        private int port = 3306;
        private boolean useSSL = true;
        private int connectionTimeout = 30000;
        private int readTimeout = 60000;
        private String charset = "UTF-8";
        private boolean autoReconnect = true;
        private int maxRetries = 3;
        private String connectionPool;
        private boolean enableLogging = false;
        private String logLevel = "INFO";
        private String timezone;

        private Builder(String host, String database, String username, String password) {
            this.host = host;
            this.database = database;
            this.username = username;
            this.password = password;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder useSSL(boolean useSSL) {
            this.useSSL = useSSL;
            return this;
        }

        public Builder connectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder readTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder charset(String charset) {
            this.charset = charset;
            return this;
        }

        public Builder autoReconnect(boolean autoReconnect) {
            this.autoReconnect = autoReconnect;
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder connectionPool(String connectionPool) {
            this.connectionPool = connectionPool;
            return this;
        }

        public Builder enableLogging(boolean enableLogging) {
            this.enableLogging = enableLogging;
            return this;
        }

        public Builder logLevel(String logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public Builder timezone(String timezone) {
            this.timezone = timezone;
            return this;
        }

        public DatabaseConnection build() {
            if (isBlank(host)) throw new IllegalArgumentException("host must not be blank");
            if (isBlank(database)) throw new IllegalArgumentException("database must not be blank");
            if (isBlank(username)) throw new IllegalArgumentException("username must not be blank");
            if (password == null) throw new IllegalArgumentException("password must not be null");

            if (port <= 0) throw new IllegalArgumentException("port must be > 0");
            if (connectionTimeout < 0) throw new IllegalArgumentException("connectionTimeout must be >= 0");
            if (readTimeout < 0) throw new IllegalArgumentException("readTimeout must be >= 0");
            if (maxRetries < 0) throw new IllegalArgumentException("maxRetries must be >= 0");

            return new DatabaseConnection(this);
        }

        private static boolean isBlank(String s) {
            return s == null || s.trim().isEmpty();
        }
    }

    // Simple read-only accessors (immutability-friendly)
    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getDatabase() { return database; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public boolean isUseSSL() { return useSSL; }
    public int getConnectionTimeout() { return connectionTimeout; }
    public int getReadTimeout() { return readTimeout; }
    public String getCharset() { return charset; }
    public boolean isAutoReconnect() { return autoReconnect; }
    public int getMaxRetries() { return maxRetries; }
    public String getConnectionPool() { return connectionPool; }
    public boolean isEnableLogging() { return enableLogging; }
    public String getLogLevel() { return logLevel; }
    public String getTimezone() { return timezone; }
}
