package dev.kaldiroglu.dp.behavioral.mediator.chat.problem.directory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Stage two: members find each other through a shared {@link Directory}.
 * <p>
 * A new member is added in one place. But every member still sends to the others itself,
 * so every sender must apply the rules: skip itself, skip anyone who has blocked it. The
 * rules are in the sending code of every kind of member.
 */
public final class Member {

    private final String name;
    private final Directory directory;
    private final Set<String> blocked = new HashSet<>();
    private final List<String> inbox = new ArrayList<>();

    public Member(String name, Directory directory) {
        this.name = name;
        this.directory = directory;
        directory.add(this);
    }

    public String name() {
        return name;
    }

    public void block(String other) {
        blocked.add(other);
    }

    public void say(String text) {
        for (Member other : directory.members()) {
            if (other != this && !other.blocked.contains(name)) {     // the rules, in the sender
                other.receive(name, text);
            }
        }
    }

    public void whisper(String to, String text) {
        directory.find(to)
                .filter(other -> !other.blocked.contains(name))
                .ifPresent(other -> other.receive(name + " (private)", text));
    }

    void receive(String from, String text) {
        inbox.add(from + ": " + text);
    }

    public List<String> inbox() {
        return List.copyOf(inbox);
    }
}
