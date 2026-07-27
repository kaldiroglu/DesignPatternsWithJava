package dev.kaldiroglu.dp.structural.facade.notification.solution2;

import java.util.EnumSet;
import java.util.Set;

/**
 * Facade with channel selection — clients pick WHICH channels,
 * the Facade still owns HOW to send, constraints, and error handling.
 *
 * Two overloads:
 *   notify(user, title, body)           → all channels (convenience)
 *   notify(user, channels, title, body) → caller picks the subset
 */
public class NotificationFacade {
    private final EmailService          email;
    private final SMSService            sms;
    private final SlackService          slack;
    private final PushNotificationService push;
    private final NotificationLogger    log;

    public NotificationFacade(NotificationConfig config) {
        this.email = new EmailService(
                config.smtpHost, config.smtpPort,
                config.smtpUsername, config.smtpPassword);
        this.sms   = new SMSService(
                config.twilioSid, config.twilioToken, config.twilioFromNumber);
        this.slack = new SlackService(config.slackWebhookUrl);
        this.push  = new PushNotificationService(config.firebaseCredentialsPath);
        this.log   = new NotificationLogger(config.logFilePath);
    }

    // ── Convenience: all channels ────────────────────────────────────

    public NotificationResult notify(User user, String title, String body) {
        return notify(user, EnumSet.allOf(Channel.class), title, body);
    }

    // ── Channel-aware: caller picks the subset ───────────────────────

    /**
     * @param channels which channels to use (e.g. EnumSet.of(EMAIL, SMS))
     */
    public NotificationResult notify(User user, Set<Channel> channels,
                                      String title, String body) {
        NotificationResult result = new NotificationResult();

        if (channels.contains(Channel.EMAIL) && user.getEmail() != null) {
            try {
                boolean ok = email.send(user.getEmail(), title, body);
                result.record("email", ok);
                log.record(user.getId(), "email", ok);
            } catch (Exception e) {
                result.record("email", false);
            }
        }

        if (channels.contains(Channel.SMS)
                && user.getPhone() != null && body.length() <= 160) {
            try {
                boolean ok = sms.send(user.getPhone(), body);
                result.record("sms", ok);
                log.record(user.getId(), "sms", ok);
            } catch (Exception e) {
                result.record("sms", false);
            }
        }

        if (channels.contains(Channel.SLACK)
                && user.getSlackChannel() != null && !user.isDoNotDisturb()) {
            try {
                boolean ok = slack.send(user.getSlackChannel(), body);
                result.record("slack", ok);
                log.record(user.getId(), "slack", ok);
            } catch (Exception e) {
                result.record("slack", false);
            }
        }

        if (channels.contains(Channel.PUSH)
                && user.getDeviceToken() != null) {
            try {
                boolean ok = push.send(user.getDeviceToken(), title, body);
                result.record("push", ok);
                log.record(user.getId(), "push", ok);
            } catch (Exception e) {
                result.record("push", false);
            }
        }

        return result;
    }
}
