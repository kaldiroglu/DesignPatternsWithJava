package dev.kaldiroglu.dp.behavioral.mediator.chat.problem.bus;

import java.util.ArrayList;
import java.util.List;

/** The team's own client. It shows a private message only if it is addressed to it. */
public final class ChatClient implements Client {

    private final String name;
    private final List<String> shown = new ArrayList<>();

    public ChatClient(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void onMessage(Message message) {
        if (message.isPrivate() && !message.to().equals(name)) {
            return;                       // not for me
        }
        shown.add(message.from() + (message.isPrivate() ? " (private)" : "") + ": " + message.text());
    }

    public List<String> shown() {
        return List.copyOf(shown);
    }
}
