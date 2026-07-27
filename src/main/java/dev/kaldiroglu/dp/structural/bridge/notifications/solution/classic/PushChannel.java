package dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Transports;

/** A third <b>ConcreteImplementor</b>. Adding it cost one class and touched nothing else. */
public final class PushChannel implements NotificationChannel {

    private final Transports transports;

    public PushChannel(Transports transports) {
        this.transports = transports;
    }

    @Override
    public String name() {
        return "push";
    }

    @Override
    public String addressOf(Recipient recipient) {
        return recipient.deviceToken();
    }

    @Override
    public int maxBodyLength() {
        return Transports.PUSH_LIMIT;
    }

    @Override
    public boolean supportsSubject() {
        return false;
    }

    @Override
    public boolean deliver(String address, String subject, String body) {
        return transports.pushNotify(address, body);
    }
}
