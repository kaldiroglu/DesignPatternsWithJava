package dev.kaldiroglu.dp.structural.facade.notification.problem;

/**
 * Subsystem: sends SMS via Twilio / Vonage / provider API.
 */
public class SMSService {
    private final String accountSid;
    private final String authToken;
    private final String fromNumber;

    public SMSService(String accountSid, String authToken, String fromNumber) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.fromNumber = fromNumber;
    }

    public boolean send(String toPhone, String body) {
        // In production: Twilio REST call, rate-limit handling, retries
        System.out.printf("[SMS]   To: %s | Body: %s%n", toPhone, body);
        return true;
    }
}
