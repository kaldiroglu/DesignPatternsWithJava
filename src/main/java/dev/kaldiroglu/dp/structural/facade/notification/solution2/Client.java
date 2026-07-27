package dev.kaldiroglu.dp.structural.facade.notification.solution2;

import java.util.EnumSet;

/**
 * AFTER v2 — each client type picks its own channel subset.
 * The Facade still owns dispatch, constraints, logging, and error isolation.
 */
public class Client {

    private final NotificationFacade notifier;

    public Client(NotificationFacade notifier) {
        this.notifier = notifier;
    }

    // ── OrderService: email + SMS (customer-facing) ──

    public void confirmOrder(User user, String orderId) {
        System.out.println("Order " + orderId + " confirmed.");

        String title = "Order #" + orderId + " Confirmed";
        String body  = "Your order #" + orderId + " has been confirmed.";

        var result = notifier.notify(user,
                EnumSet.of(Channel.EMAIL, Channel.SMS),
                title, body);
        System.out.println("  → " + result);
    }

    // ── AlertService: Slack + Push only (ops + mobile) ──

    public void fireAlert(User user, String alertMessage) {
        System.out.println("ALERT: " + alertMessage);

        var result = notifier.notify(user,
                EnumSet.of(Channel.SLACK, Channel.PUSH),
                "[ALERT] " + alertMessage, alertMessage);
        System.out.println("  → " + result);
    }

    // ── PasswordResetService: email ONLY (security policy) ──

    public void sendResetLink(User user, String token) {
        System.out.println("Password reset for " + user.getId());

        String body = "Click here to reset: https://app.com/reset?t=" + token;

        var result = notifier.notify(user,
                EnumSet.of(Channel.EMAIL),
                "Password Reset", body);
        System.out.println("  → " + result);
    }

    // ── Demo ──

    public static void main(String[] args) {
        NotificationConfig config = new NotificationConfig(
                "smtp.company.com", 587, "noreply", "***",
                "AC123", "token", "+1555000",
                "https://hooks.slack.com/...",
                "/secrets/firebase.json",
                "/var/log/notifications.log"
        );

        NotificationFacade notifier = new NotificationFacade(config);
        Client client = new Client(notifier);

        User user = new User("user_42")
                .email("alice@example.com")
                .phone("+15551234567")
                .slack("#alerts")
                .deviceToken("dev_token_abc123");

        System.out.println("=== Order: email + SMS ===");
        client.confirmOrder(user, "ORD-9876");

        System.out.println("\n=== Alert: slack + push ===");
        client.fireAlert(user, "CPU at 95% on prod-03");

        System.out.println("\n=== Password reset: email only ===");
        client.sendResetLink(user, "aB3xZ9kL");
    }
}
