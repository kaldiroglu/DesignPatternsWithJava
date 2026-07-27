package dev.kaldiroglu.dp.structural.facade.notification.solution1;

/**
 * Domain model: the recipient of notifications.
 * The Facade inspects this to decide which channels are relevant.
 */
public class User {
    private final String id;
    private String email;
    private String phone;
    private String slackChannel;
    private String deviceToken;
    private boolean doNotDisturb;

    public User(String id) {
        this.id = id;
    }

    // -- fluent setters to keep slide demos short --

    public User email(String v)    { this.email = v;         return this; }
    public User phone(String v)    { this.phone = v;         return this; }
    public User slack(String v)    { this.slackChannel = v;  return this; }
    public User deviceToken(String v) { this.deviceToken = v; return this; }
    public User dnd(boolean v)     { this.doNotDisturb = v;  return this; }

    // -- getters --
    public String getId()          { return id; }
    public String getEmail()       { return email; }
    public String getPhone()       { return phone; }
    public String getSlackChannel(){ return slackChannel; }
    public String getDeviceToken() { return deviceToken; }
    public boolean isDoNotDisturb(){ return doNotDisturb; }
}
