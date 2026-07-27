package dev.kaldiroglu.dp.structural.bridge.notifications.domain;

/**
 * Somebody we need to reach, and the ways we are allowed to reach them.
 * <p>
 * The {@code preferred} field is where the pressure comes from: it is chosen by the user,
 * stored in a database, and known only while the program is running.
 */
public record Recipient(String name, String email, String phone, String deviceToken,
                        ChannelKind preferred) {

    public static Recipient of(String name, ChannelKind preferred) {
        String handle = name.toLowerCase();
        return new Recipient(name, handle + "@example.com", "+90-555-0100",
                "device-" + handle, preferred);
    }
}
