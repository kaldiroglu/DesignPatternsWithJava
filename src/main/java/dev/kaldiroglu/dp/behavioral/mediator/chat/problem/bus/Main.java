package dev.kaldiroglu.dp.behavioral.mediator.chat.problem.bus;

import java.util.List;

/**
 * Elif sends a private message to Mert over the bus. The bus gives it to every client;
 * the team clients hide it, and the guest client shows it to Can.
 */
public final class Main {

    public static void main(String[] args) {
        MessageBus bus = new MessageBus();
        ChatClient elif = new ChatClient("Elif"), burak = new ChatClient("Burak"), mert = new ChatClient("Mert");
        GuestClient can = new GuestClient("Can");
        List.of(elif, burak, mert).forEach(bus::subscribe);
        bus.subscribe(can);

        bus.publish(new Message("Elif", "Mert", "Your review is late."));

        System.out.println("Delivered to: " + bus.deliveries());
        System.out.println("Mert shows:   " + mert.shown());
        System.out.println("Burak shows:  " + burak.shown());
        System.out.println("Can shows:    " + can.shown() + "  (a private message for Mert)");
    }
}
