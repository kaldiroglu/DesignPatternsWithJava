package dev.kaldiroglu.dp.structural.facade.notification.solution2;

public class PushNotificationService {
    private final String firebaseCredentialsPath;

    public PushNotificationService(String firebaseCredentialsPath) {
        this.firebaseCredentialsPath = firebaseCredentialsPath;
    }

    public boolean send(String deviceToken, String title, String body) {
        System.out.printf("[PUSH]  Token: %s*** | Title: %s%n",
                deviceToken.substring(0, Math.min(8, deviceToken.length())), title);
        return true;
    }
}
