package dev.kaldiroglu.dp.behavioral.mediator.chat.problem.bus;

/** Anything that subscribes to the bus. */
public interface Client {

    String name();

    void onMessage(Message message);
}
