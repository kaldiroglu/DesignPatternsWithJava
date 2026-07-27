package dev.kaldiroglu.dp.structural.facade.notification.solution1;

/**
 * The Facade — a single, unified interface over four notification subsystems
 * plus audit logging.
 *
 * Responsibilities the Facade absorbs from every client:
 *   1. Subsystem construction & credential management
 *   2. Channel-selection logic (email present? SMS fits 160 chars? DND mode?)
 *   3. Per-channel logging
 *   4. Error isolation (one channel failing doesn't crash the others)
 */
public class NotificationFacade {
    private final EmailService          email;
    private final SMSService            sms;
    private final SlackService          slack;
    private final PushNotificationService push;
    private final NotificationLogger    log;

    /**
     * One place to wire up every subsystem. Clients never touch
     * EmailService, SMSService, etc. — they only see this Facade.
     */
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

    /**
     * The single method every client calls.  One line replaces 20.
     *
     * @param user  the recipient — channels are chosen based on user profile
     * @param title subject line for email & push; SMS/Slack use body-only
     * @param body  message content; SMS capped at 160, push truncated to 120
     * @return which channels succeeded or failed
     */
    public NotificationResult notify(User user, String title, String body) {
        NotificationResult result = new NotificationResult();

        if (user.getEmail() != null) {
            try {
                boolean ok = email.send(user.getEmail(), title, body);
                result.record("email", ok);
                log.record(user.getId(), "email", ok);
            } catch (Exception e) {
                result.record("email", false);
            }
        }

        if (user.getPhone() != null && body.length() <= 160) {
            try {
                boolean ok = sms.send(user.getPhone(), body);
                result.record("sms", ok);
                log.record(user.getId(), "sms", ok);
            } catch (Exception e) {
                result.record("sms", false);
            }
        }

        if (user.getSlackChannel() != null && !user.isDoNotDisturb()) {
            try {
                boolean ok = slack.send(user.getSlackChannel(), body);
                result.record("slack", ok);
                log.record(user.getId(), "slack", ok);
            } catch (Exception e) {
                result.record("slack", false);
            }
        }

        if (user.getDeviceToken() != null) {
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
