package dev.kaldiroglu.dp.structural.bridge.notifications.problem;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Transports;

/**
 * Naive design 3, part one: a base class that IS the email implementation.
 * <p>
 * The idea is reasonable — put the SMTP details in one place and inherit them. What it
 * quietly decides is that everything derived from it is an email, permanently.
 */
public abstract class EmailSender {

    private final Transports transports;

    protected EmailSender(Transports transports) {
        this.transports = transports;
    }

    protected boolean deliver(String address, String subject, String body) {
        return transports.smtpSend(address, subject, body);
    }

    protected String channelName() {
        return "email";
    }
}
