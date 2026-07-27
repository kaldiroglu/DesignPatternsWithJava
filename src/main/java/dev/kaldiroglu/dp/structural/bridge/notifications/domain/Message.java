package dev.kaldiroglu.dp.structural.bridge.notifications.domain;

/** What we want to say. Nothing here knows how it will be delivered. */
public record Message(String subject, String body) {
}
