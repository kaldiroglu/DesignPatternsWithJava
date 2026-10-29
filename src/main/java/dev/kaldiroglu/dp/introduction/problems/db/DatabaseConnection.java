package dev.kaldiroglu.dp.introduction.problems.db;

public class DatabaseConnection {
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
    private boolean useSSL;
    private int connectionTimeout;
    private int readTimeout;
    private String charset;
    private boolean autoReconnect;
    private int maxRetries;
    private String connectionPool;
    private boolean enableLogging;
    private String logLevel;
    private String timezone;

    // Default constructor for post-construction initialization
    public DatabaseConnection() {
        // Set reasonable defaults
        this.port = 3306;
        this.useSSL = true;
        this.connectionTimeout = 30000;
        this.readTimeout = 60000;
        this.charset = "UTF-8";
        this.autoReconnect = true;
        this.maxRetries = 3;
        this.enableLogging = false;
        this.logLevel = "INFO";
    }

    // Imagine if we tried to use constructors for all combinations:

    // Constructor for basic connection
    public DatabaseConnection(String host, String database, String username, String password) {
        this();
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    // Constructor with port
    public DatabaseConnection(String host, int port, String database, String username, String password) {
        this(host, database, username, password);
        this.port = port;
    }

    // Constructor with SSL option
    public DatabaseConnection(String host, int port, String database, String username, String password, boolean useSSL) {
        this(host, port, database, username, password);
        this.useSSL = useSSL;
    }

    // Constructor with timeouts... this quickly becomes unmanageable!
    public DatabaseConnection(String host, int port, String database, String username, String password,
                              boolean useSSL, int connectionTimeout, int readTimeout) {
        this(host, port, database, username, password, useSSL);
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
    }

    // We'd need dozens more constructors to cover all meaningful combinations!
    // What if someone wants SSL=true, custom charset, but default timeouts?
    // What if they want custom logging but default connection settings?

    // Setter methods for post-construction initialization
    public void setHost(String host) { this.host = host; }
    public void setPort(int port) { this.port = port; }
    public void setDatabase(String database) { this.database = database; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setUseSSL(boolean useSSL) { this.useSSL = useSSL; }
    public void setConnectionTimeout(int connectionTimeout) { this.connectionTimeout = connectionTimeout; }
    public void setReadTimeout(int readTimeout) { this.readTimeout = readTimeout; }
    public void setCharset(String charset) { this.charset = charset; }
    public void setAutoReconnect(boolean autoReconnect) { this.autoReconnect = autoReconnect; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public void setConnectionPool(String connectionPool) { this.connectionPool = connectionPool; }
    public void setEnableLogging(boolean enableLogging) { this.enableLogging = enableLogging; }
    public void setLogLevel(String logLevel) { this.logLevel = logLevel; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
}

/*
Why constructors would be problematic here:

1. COMBINATORIAL EXPLOSION: With 15 properties, we'd need potentially
   hundreds of constructor overloads to cover meaningful combinations.

2. PARAMETER CONFUSION: A constructor with 8+ parameters becomes error-prone:
   DatabaseConnection(host, port, database, username, password, useSSL, timeout1, timeout2)
   // Which timeout is which? Easy to mix up port and timeout values!

3. MEANINGLESS COMBINATIONS: Not every combination makes sense to have
   a dedicated constructor for.

4. MAINTENANCE NIGHTMARE: Adding one new property means potentially
   adding dozens of new constructors.

5. UNCLEAR INTENT: Post-construction initialization makes it clear
   which properties are being customized for each use case.

6. FLEXIBILITY: Easy to create different "profiles" of connections
   without predefined constructors.
*/