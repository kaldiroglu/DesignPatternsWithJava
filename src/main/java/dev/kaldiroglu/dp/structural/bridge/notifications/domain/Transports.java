package dev.kaldiroglu.dp.structural.bridge.notifications.domain;

/**
 * The three vendor SDKs, as they were handed to us.
 * <p>
 * This is the part nobody gets to redesign. Each one has its own name for "send", its own
 * idea of an address, and its own limits. They have nothing in common but the fact that a
 * message comes out the other end.
 * <p>
 * Both the naive designs and the Bridge design end up here. That is deliberate: the two
 * are then doing identical work on identical infrastructure, and every remaining
 * difference is a difference of design.
 */
public final class Transports {

    /** An SMS is 160 characters. This is not negotiable and never has been. */
    public static final int SMS_LIMIT = 160;

    /** Push payloads are capped by the platform. */
    public static final int PUSH_LIMIT = 120;

    /** Mail servers will take far more than we will ever send. */
    public static final int EMAIL_LIMIT = 10_000;

    private final TransportLog log;

    public Transports(TransportLog log) {
        this.log = log;
    }

    /** The mail vendor's API: a subject and a body, to an address. */
    public boolean smtpSend(String emailAddress, String subject, String body) {
        log.openConnection();
        return log.record("email", emailAddress, body);
    }

    /** The SMS vendor's API: one string, to a number. No subject. Hard 160-char limit. */
    public boolean smsSubmit(String phoneNumber, String text) {
        if (text.length() > SMS_LIMIT) {
            throw new IllegalArgumentException("SMS over " + SMS_LIMIT + " characters");
        }
        log.openConnection();
        return log.record("sms", phoneNumber, text);
    }

    /** The push vendor's API: a payload, to a device token. */
    public boolean pushNotify(String deviceToken, String payload) {
        if (payload.length() > PUSH_LIMIT) {
            throw new IllegalArgumentException("push payload over " + PUSH_LIMIT);
        }
        log.openConnection();
        return log.record("push", deviceToken, payload);
    }
}
