package dev.kaldiroglu.dp.behavioral.mediator.chat.problem.direct;

import java.util.ArrayList;
import java.util.List;

/**
 * Stage one: every member holds a reference to every other member.
 * <p>
 * Messages go straight from sender to receiver, and a private message reaches only its
 * receiver. But each member keeps its own list of the others. Four members hold twelve
 * references between them, and a fifth member must be added to four lists — a member who
 * was not told about the new one never sends to them.
 */
public final class Member {

    private final String name;
    private final List<Member> others = new ArrayList<>();
    private final List<String> inbox = new ArrayList<>();

    public Member(String name) {
        this.name = name;
    }

    /** Both members add each other. */
    public void meet(Member other) {
        others.add(other);
        other.others.add(this);
    }

    public void say(String text) {
        for (Member other : others) {
            other.receive(name, text);
        }
    }

    public void whisper(Member to, String text) {
        to.receive(name + " (private)", text);
    }

    void receive(String from, String text) {
        inbox.add(from + ": " + text);
    }

    public int references() {
        return others.size();
    }

    public List<String> inbox() {
        return List.copyOf(inbox);
    }
}
