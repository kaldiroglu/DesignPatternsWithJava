package dev.kaldiroglu.dp.behavioral.mediator.chat.solution;

import dev.kaldiroglu.dp.behavioral.mediator.chat.problem.bus.ChatClient;
import dev.kaldiroglu.dp.behavioral.mediator.chat.problem.bus.GuestClient;
import dev.kaldiroglu.dp.behavioral.mediator.chat.problem.bus.Message;
import dev.kaldiroglu.dp.behavioral.mediator.chat.problem.bus.MessageBus;
import dev.kaldiroglu.dp.behavioral.mediator.chat.problem.direct.Member;

import java.util.List;

/**
 * Runs the chat through stage one, stage three and the chat room.
 * <p>
 * The promise: a private message is seen only by the person it is sent to. Elif, Burak and
 * Mert are on the team; Can joins as a guest.
 */
public final class Main {

    public static void main(String[] args) {
        // stage one: everyone holds everyone
        Member elif = new Member("Elif"), burak = new Member("Burak"), mert = new Member("Mert"),
                selin = new Member("Selin");
        List<Member> team = List.of(elif, burak, mert, selin);
        for (int i = 0; i < team.size(); i++) {
            for (int j = i + 1; j < team.size(); j++) {
                team.get(i).meet(team.get(j));
            }
        }
        int references = team.stream().mapToInt(Member::references).sum();
        System.out.println("Stage one: " + team.size() + " members hold " + references
                + " references to each other.");

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
        dev.kaldiroglu.dp.behavioral.mediator.chat.solution.Member elif2 = new dev.kaldiroglu.dp.behavioral.mediator.chat.solution.Member("Elif", room);
        dev.kaldiroglu.dp.behavioral.mediator.chat.solution.Member burak2 = new dev.kaldiroglu.dp.behavioral.mediator.chat.solution.Member("Burak", room);
        dev.kaldiroglu.dp.behavioral.mediator.chat.solution.Member mert2 = new dev.kaldiroglu.dp.behavioral.mediator.chat.solution.Member("Mert", room);
        Guest can = new Guest("Can", room);
        elif2.whisper("Mert", "Your review is late.");
        System.out.println("Chat room: Elif sends Mert a private message.");
        System.out.println("  delivered to: " + room.deliveries());
        System.out.println("  Mert shows:   " + mert2.shown());
        System.out.println("  Can shows:    " + can.shown());

        mert2.block("Burak");
        burak2.say("Lunch at one?");
        System.out.println("Mert blocks Burak; Burak says something to everyone.");
        System.out.println("  Elif shows:   " + elif2.shown());
        System.out.println("  Mert shows:   " + mert2.shown());
        System.out.println("  Can shows:    " + can.shown());
    }
}
