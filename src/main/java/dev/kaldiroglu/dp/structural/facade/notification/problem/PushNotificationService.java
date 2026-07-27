package dev.kaldiroglu.dp.structural.facade.notification.problem;

/**
 * Subsystem: sends push notifications via FCM / APNs.
 */
public class PushNotificationService {
    private final String firebaseCredentialsPath;

    public PushNotificationService(String firebaseCredentialsPath) {
        this.firebaseCredentialsPath = firebaseCredentialsPath;
    }

    public boolean send(String deviceToken, String title, String body) {
        // In production: Firebase Admin SDK, APNs HTTP/2, token expiry handling
        System.out.printf("[PUSH]  Token: %s*** | Title: %s%n",
                deviceToken.substring(0, Math.min(8, deviceToken.length())), title);
        return true;
    }
}
