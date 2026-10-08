package dev.kaldiroglu.dp.behavioral.observer.hw.inbox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Homework 3: an inbox shown in several places — a badge with the unread count, a list of
 * subjects, and a desktop notification.
 * <p>
 * The homework question: should the inbox push the new message, or only say "something
 * changed" and let each view pull what it needs? Here it pushes the message, because every
 * view needs it and the message is small. The badge pulls the count, because the count is
 * not in the message. Both styles in one subject: GoF implementation issue 6 (avoiding
 * observer-specific update protocols: the push and pull models).
 */
public final class Inbox {

    public record Message(String from, String subject) {
    }

    private final List<Message> messages = new ArrayList<>();
    private int unread;
    private final List<Consumer<Message>> views = new ArrayList<>();

    /** Any function that takes a message can be a view: the observer is a lambda. */
    public void onNewMessage(Consumer<Message> view) {
        views.add(view);
    }

    public void receive(Message message) {
        messages.add(message);
        unread++;
        for (Consumer<Message> view : List.copyOf(views)) {
            view.accept(message);
        }
    }

    public void readAll() {
        unread = 0;
    }

    public int unread() {
        return unread;
    }
}
