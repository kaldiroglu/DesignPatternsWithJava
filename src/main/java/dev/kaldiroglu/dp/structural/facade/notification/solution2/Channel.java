package dev.kaldiroglu.dp.structural.facade.notification.solution2;

/**
 * Channels the notification facade can dispatch to.
 * Clients pick which subset to use; the Facade still owns the dispatch logic.
 */
public enum Channel {
    EMAIL,
    SMS,
    SLACK,
    PUSH
}
