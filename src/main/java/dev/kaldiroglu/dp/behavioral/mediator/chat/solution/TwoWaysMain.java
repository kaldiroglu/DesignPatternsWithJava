package dev.kaldiroglu.dp.behavioral.mediator.chat.solution;

import dev.kaldiroglu.dp.behavioral.mediator.chat.problem.bus.ChatClient;
import dev.kaldiroglu.dp.behavioral.mediator.chat.problem.bus.GuestClient;
import dev.kaldiroglu.dp.behavioral.mediator.chat.problem.bus.Message;
import dev.kaldiroglu.dp.behavioral.mediator.chat.problem.bus.MessageBus;

import java.util.List;

/**
 * Sends the same private message from Elif to Mert two ways: through the message bus of
 * stage three, and through the chat room.
 * <p>
 * The promise: a private message is seen only by the person it is sent to. Elif, Burak and
 * Mert are on the team; Can joins as a guest. Then Mert blocks Burak, and Burak says
 * something to everyone.
 */
public final class TwoWaysMain {

    public static void main(String[] args) {
        // stage three: a bus, and clients that filter
        MessageBus bus = new MessageBus();
        ChatClient elifClient = new ChatClient("Elif"), burakClient = new ChatClient("Burak"),
                mertClient = new ChatClient("Mert");
        GuestClient canClient = new GuestClient("Can");
        List.of(elifClient, burakClient, mertClient).forEach(bus::subscribe);
        bus.subscribe(canClient);
        bus.publish(new Message("Elif", "Mert", "Your review is late."));
        System.out.println("Stage three: Elif sends Mert a private message.");
        System.out.println("  delivered to: " + bus.deliveries());
        System.out.println("  Mert shows:   " + mertClient.shown());
        System.out.println("  Burak shows:  " + burakClient.shown());
        System.out.println("  Can shows:    " + canClient.shown());

        // the mediator
        ChatRoom room = new ChatRoom();
        Member elif = new Member("Elif", room);
        Member burak = new Member("Burak", room);
        Member mert = new Member("Mert", room);
        Guest can = new Guest("Can", room);
        elif.whisper("Mert", "Your review is late.");
        System.out.println("Chat room: Elif sends Mert a private message.");
        System.out.println("  delivered to: " + room.deliveries());
        System.out.println("  Mert shows:   " + mert.shown());
        System.out.println("  Can shows:    " + can.shown());

        mert.block("Burak");
        burak.say("Lunch at one?");
        System.out.println("Mert blocks Burak; Burak says something to everyone.");
        System.out.println("  Elif shows:   " + elif.shown());
        System.out.println("  Mert shows:   " + mert.shown());
        System.out.println("  Can shows:    " + can.shown());
    }
}
