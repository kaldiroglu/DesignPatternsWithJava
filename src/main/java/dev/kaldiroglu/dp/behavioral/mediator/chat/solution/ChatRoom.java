package dev.kaldiroglu.dp.behavioral.mediator.chat.solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The <b>Mediator</b>: the only way one participant reaches another.
 * <p>
 * Every rule about who receives what is here, in one class: a message to everyone goes to
 * all but the sender, a private message goes to its receiver only, and nobody receives
 * from someone they blocked. A participant written later — a guest client — gets only what
 * the room sends it, so it cannot show what was not meant for it.
 */
public final class ChatRoom {

    private final Map<String, Participant> participants = new LinkedHashMap<>();
    private final Map<String, Set<String>> blocks = new HashMap<>();
    private final List<String> deliveries = new ArrayList<>();

    public void join(Participant participant) {
        participants.put(participant.name(), participant);
    }

    public void block(String blocker, String blocked) {
        blocks.computeIfAbsent(blocker, k -> new HashSet<>()).add(blocked);
    }

    public void say(String from, String text) {
        for (Participant p : participants.values()) {
            if (!p.name().equals(from) && !hasBlocked(p.name(), from)) {
                deliver(p, from, text);
            }
        }
    }

    public void whisper(String from, String to, String text) {
        Participant p = participants.get(to);
        if (p != null && !hasBlocked(to, from)) {
            deliver(p, from + " (private)", text);
        }
    }

    private boolean hasBlocked(String blocker, String sender) {
        return blocks.getOrDefault(blocker, Set.of()).contains(sender);
    }

    private void deliver(Participant p, String from, String text) {
        deliveries.add(p.name());
        p.receive(from, text);
    }

    /** The participants each message was delivered to, in order. */
    public List<String> deliveries() {
        return List.copyOf(deliveries);
    }
}
