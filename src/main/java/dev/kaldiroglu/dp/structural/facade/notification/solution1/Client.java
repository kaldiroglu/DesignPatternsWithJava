package dev.kaldiroglu.dp.structural.facade.notification.solution1;

/**
 * AFTER the Facade pattern — every client depends on ONE class,
 * calls ONE method, and gets a structured result back.
 *
 * Compare this file to before/Client.java. Same business outcome,
 * but the notification logic moved from every client into the Facade.
 */
public class Client {

    // ── One dependency instead of five ──
    private final NotificationFacade notifier;

    public Client(NotificationFacade notifier) {
        this.notifier = notifier;
    }

    /**
     * Same confirmOrder, but the notification block is one line.
     * Adding a fifth channel (WhatsApp, WeChat, Teams) changes
     * zero lines in this class.
     */
    public void confirmOrder(User user, String orderId) {
        // -- business logic --
        System.out.println("Order " + orderId + " confirmed.");

        String title = "Order #" + orderId + " Confirmed";
        String body  = "Your order #" + orderId + " has been confirmed.";

        NotificationResult result = notifier.notify(user, title, body);
        System.out.println("Notification result: " + result);
    }

    // ── Demo entry point ──
    public static void main(String[] args) {
        NotificationConfig config = new NotificationConfig(
                "smtp.company.com", 587, "noreply", "***",        // email
                "AC123", "token", "+1555000",                      // SMS
                "https://hooks.slack.com/...",                     // Slack
                "/secrets/firebase.json",                          // push
                "/var/log/notifications.log"                       // log
        );

        NotificationFacade notifier = new NotificationFacade(config);
        Client client = new Client(notifier);

        User user = new User("user_42")
                .email("alice@example.com")
                .phone("+15551234567")
                .slack("#orders")
                .deviceToken("dev_token_abc123");

        client.confirmOrder(user, "ORD-9876");
    }
}
