package dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;

/**
 * The <b>Implementor</b>: what every delivery channel can do, expressed as primitives.
 * <p>
 * Everything here is a question about the <em>channel</em>, and nothing here is a question
 * about notifications. There is no {@code sendUrgent}, no {@code sendDigest} — those are
 * the abstraction's business, and if they appeared here the two hierarchies would have
 * grown back together.
 * <p>
 * GoF, p. 153: "The Implementor interface provides only primitive operations, and
 * Abstraction defines higher-level operations based on these primitives."
 */
public interface NotificationChannel {

    /** What this channel is called in logs and results. */
    String name();

    /** How this channel identifies a person. An email address, a phone number, a token. */
    String addressOf(Recipient recipient);

    /** The longest body this channel will carry. The abstraction asks; it never assumes. */
    int maxBodyLength();

    /** Whether the channel can carry a separate subject line, or only one blob of text. */
    boolean supportsSubject();

    /** Hand one message to the channel. Returns false if the channel could not deliver it. */
    boolean deliver(String address, String subject, String body);
}
