package dev.kaldiroglu.dp.structural.facade.notification.solution1;

/**
 * Value object holding all external configuration the Facade needs.
 * Single place to change credentials — no scattering across client code.
 */
public class NotificationConfig {
    public final String smtpHost;
    public final int smtpPort;
    public final String smtpUsername;
    public final String smtpPassword;
    public final String twilioSid;
    public final String twilioToken;
    public final String twilioFromNumber;
    public final String slackWebhookUrl;
    public final String firebaseCredentialsPath;
    public final String logFilePath;

    public NotificationConfig(
            String smtpHost, int smtpPort, String smtpUsername, String smtpPassword,
            String twilioSid, String twilioToken, String twilioFromNumber,
            String slackWebhookUrl,
            String firebaseCredentialsPath,
            String logFilePath) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.smtpUsername = smtpUsername;
        this.smtpPassword = smtpPassword;
        this.twilioSid = twilioSid;
        this.twilioToken = twilioToken;
        this.twilioFromNumber = twilioFromNumber;
        this.slackWebhookUrl = slackWebhookUrl;
        this.firebaseCredentialsPath = firebaseCredentialsPath;
        this.logFilePath = logFilePath;
    }
}
