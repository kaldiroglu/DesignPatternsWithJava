package dev.kaldiroglu.dp.structural.facade.notification.problem;

/**
 * BEFORE the Facade pattern — every client class that needs to send
 * a notification carries five subsystem dependencies, duplicates
 * channel-selection logic, and repeats the same try-catch ceremony.
 *
 * This file represents what OrderService, PaymentService, UserService,
 * AlertService, and every other class in the system looks like.
 */
public class Client {

    // ── Five dependencies for a single responsibility ──
    private final EmailService emailService;
    private final SMSService smsService;
    private final SlackService slackService;
    private final PushNotificationService pushService;
    private final NotificationLogger logger;

    public Client(
            EmailService emailService,
            SMSService smsService,
            SlackService slackService,
            PushNotificationService pushService,
            NotificationLogger logger) {
        this.emailService = emailService;
        this.smsService   = smsService;
        this.slackService = slackService;
        this.pushService  = pushService;
        this.logger       = logger;
    }

    /**
     * A typical business method: confirm an order and notify the user.
     * Every business method that wants to send a notification duplicates
     * this 25-line block.
     */
    public void confirmOrder(String userId, String userEmail, String userPhone,
                              String userSlack, String userDeviceToken,
                              boolean dnd, String orderId) {
        // -- business logic (save to DB, etc.) --
        System.out.println("Order " + orderId + " confirmed.");

        // -- notification block: 4 channels × ~5 lines each = 20+ lines --
        String subject = "Order #" + orderId + " Confirmed";
        String body    = "Your order #" + orderId + " has been confirmed.";

        if (userEmail != null && !userEmail.isEmpty()) {
            boolean ok = emailService.send(userEmail, subject, body);
            logger.record(userId, "email", ok);
        }
        if (userPhone != null && !userPhone.isEmpty() && body.length() <= 160) {
            boolean ok = smsService.send(userPhone, body);
            logger.record(userId, "sms", ok);
        }
        if (userSlack != null && !userSlack.isEmpty() && !dnd) {
            boolean ok = slackService.send(userSlack, body);
            logger.record(userId, "slack", ok);
        }
        if (userDeviceToken != null && !userDeviceToken.isEmpty()) {
            boolean ok = pushService.send(userDeviceToken, subject, body);
            logger.record(userId, "push", ok);
        }
    }

    // ── Demo entry point ──
    public static void main(String[] args) {
        // Wiring: every client repeats this construction
        var email = new EmailService("smtp.company.com", 587, "noreply", "***");
        var sms   = new SMSService("AC123", "token", "+1555000");
        var slack = new SlackService("https://hooks.slack.com/...");
        var push  = new PushNotificationService("/secrets/firebase.json");
        var log   = new NotificationLogger("/var/log/notifications.log");

        var client = new Client(email, sms, slack, push, log);

        client.confirmOrder("user_42", "alice@example.com", "+15551234567",
                "#orders", "dev_token_abc123", false, "ORD-9876");
    }
}
