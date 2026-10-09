package dev.kaldiroglu.dp.behavioral.mediator.chat.problem.bus;

import java.util.ArrayList;
import java.util.List;

/**
 * A client for guests, written later by another team. It shows what the bus gives it.
 * <p>
 * It does not check {@code to}, so it shows private messages meant for someone else.
 * Nothing fails: the bus delivered the message, and the client displayed it.
 */
public final class GuestClient implements Client {

    private final String name;
    private final List<String> shown = new ArrayList<>();

    public GuestClient(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void onMessage(Message message) {
        shown.add(message.from() + ": " + message.text());
    }

    public List<String> shown() {
        return List.copyOf(shown);
    }
}
