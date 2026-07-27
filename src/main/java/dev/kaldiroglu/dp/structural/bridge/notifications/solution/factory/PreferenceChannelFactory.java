package dev.kaldiroglu.dp.structural.bridge.notifications.solution.factory;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.ChannelKind;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Transports;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.EmailChannel;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.NotificationChannel;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.PushChannel;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.SmsChannel;

import java.util.EnumMap;
import java.util.Map;

/**
 * Picks the channel from what the user asked for — a value that lives in a database and
 * is not known until the program runs.
 * <p>
 * This is the whole argument for Bridge over inheritance, reduced to one method. Under any
 * design where the channel is a base class, this method cannot exist: you cannot choose a
 * superclass at run time.
 * <p>
 * The channels are built once and shared, which is GoF's implementation issue 3 in its
 * simplest form — see {@code solution.shared} for what that costs when a channel holds
 * state.
 */
public final class PreferenceChannelFactory implements ChannelFactory {

    private final Map<ChannelKind, NotificationChannel> channels = new EnumMap<>(ChannelKind.class);

    public PreferenceChannelFactory(Transports transports) {
        channels.put(ChannelKind.EMAIL, new EmailChannel(transports));
        channels.put(ChannelKind.SMS, new SmsChannel(transports));
        channels.put(ChannelKind.PUSH, new PushChannel(transports));
    }

    @Override
    public NotificationChannel channelFor(Recipient recipient) {
        return channels.get(recipient.preferred());
    }

    /** Registering a fourth channel is one line, and no notification kind is touched. */
    public PreferenceChannelFactory register(ChannelKind kind, NotificationChannel channel) {
        channels.put(kind, channel);
        return this;
    }
}
