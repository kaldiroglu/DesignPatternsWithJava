package dev.kaldiroglu.dp.behavioral.mediator.chat.problem.bus;

/** A message on the bus. {@code to} is {@code null} for a message to everyone. */
public record Message(String from, String to, String text) {

    public boolean isPrivate() {
        return to != null;
    }
}
