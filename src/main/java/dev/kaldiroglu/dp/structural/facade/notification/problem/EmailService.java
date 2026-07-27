package dev.kaldiroglu.dp.structural.facade.notification.problem;

/**
 * Subsystem: sends transactional emails via SMTP.
 * In production this wraps JavaMail / SendGrid / SES.
 */
public class EmailService {
    private final String smtpHost;
    private final int smtpPort;
    private final String username;
    private final String password;

    public EmailService(String smtpHost, int smtpPort, String username, String password) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.username = username;
        this.password = password;
    }

    public boolean send(String to, String subject, String body) {
        // In production: MimeMessage, Transport.send(), error handling
        System.out.printf("[EMAIL] To: %s | Subject: %s%n", to, subject);
        return true;
    }
}
