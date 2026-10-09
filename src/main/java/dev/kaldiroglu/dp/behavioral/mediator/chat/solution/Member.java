package dev.kaldiroglu.dp.behavioral.mediator.chat.solution;

import java.util.ArrayList;
import java.util.List;

/** A <b>ConcreteColleague</b>: a team member. It talks only to the room. */
public final class Member implements Participant {

    private final String name;
    private final ChatRoom room;
    private final List<String> shown = new ArrayList<>();

    public Member(String name, ChatRoom room) {
        this.name = name;
        this.room = room;
        room.join(this);
    }

    @Override
    public String name() {
        return name;
    }

    public void say(String text) {
        room.say(name, text);
    }

    public void whisper(String to, String text) {
        room.whisper(name, to, text);
    }

    public void block(String other) {
        room.block(name, other);
    }

    @Override
    public void receive(String from, String text) {
        shown.add(from + ": " + text);
    }

    public List<String> shown() {
        return List.copyOf(shown);
    }
}
