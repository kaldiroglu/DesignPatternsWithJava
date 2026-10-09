package dev.kaldiroglu.dp.behavioral.mediator.chat.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * A <b>ConcreteColleague</b> written later: a guest. Like the guest client of stage three,
 * it shows whatever it receives and checks nothing — but here it receives only what the
 * room decided to send it.
 */
public final class Guest implements Participant {

    private final String name;
    private final List<String> shown = new ArrayList<>();

    public Guest(String name, ChatRoom room) {
        this.name = name;
        room.join(this);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void receive(String from, String text) {
        shown.add(from + ": " + text);
    }

    public List<String> shown() {
        return List.copyOf(shown);
    }
}
