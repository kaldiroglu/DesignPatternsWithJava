package dev.kaldiroglu.dp.behavioral.mediator.chat.solution;

/**
 * The <b>Colleague</b>: someone in the chat. It knows the room, never another participant.
 * The room calls {@link #receive}; nothing else does.
 */
public interface Participant {

    String name();

    void receive(String from, String text);
}
