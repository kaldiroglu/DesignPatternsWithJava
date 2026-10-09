package dev.kaldiroglu.dp.behavioral.mediator.chat.solution;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.kaldiroglu.dp.behavioral.mediator.Fields.heldBy;
import static dev.kaldiroglu.dp.behavioral.mediator.Fields.holdsAny;
import static dev.kaldiroglu.dp.behavioral.mediator.Printed.by;
import static org.junit.jupiter.api.Assertions.*;

/** The chat room as the mediator. Every figure on the Part 3 slides is asserted here. */
class SolutionTest {

    private static final List<Class<?>> PARTICIPANTS = List.of(Participant.class, Member.class, Guest.class);

    @Test
    @DisplayName("the room delivers Elif's private message to Mert only, and the guest shows nothing")
    void theRoomDeliversToMertOnly() {
        ChatRoom room = new ChatRoom();
        Member elif = new Member("Elif", room);
        new Member("Burak", room);
        Member mert = new Member("Mert", room);
        Guest can = new Guest("Can", room);

        elif.whisper("Mert", "Your review is late.");

        assertEquals(List.of("Mert"), room.deliveries());
        assertEquals(List.of("Elif (private): Your review is late."), mert.shown());
        assertEquals(List.of(), can.shown());
    }

    @Test
    @DisplayName("Mert blocks Burak: Burak's message to everyone goes to Elif and Can, not to Mert")
    void aBlockIsKeptByTheRoom() {
        ChatRoom room = new ChatRoom();
        Member elif = new Member("Elif", room);
        Member burak = new Member("Burak", room);
        Member mert = new Member("Mert", room);
        Guest can = new Guest("Can", room);

        mert.block("Burak");
        burak.say("Lunch at one?");
        burak.whisper("Mert", "Are you there?");

        assertEquals(List.of("Elif", "Can"), room.deliveries());
        assertEquals(List.of("Burak: Lunch at one?"), elif.shown());
        assertEquals(List.of("Burak: Lunch at one?"), can.shown());
        assertEquals(List.of(), mert.shown());
        assertEquals(List.of(), burak.shown(), "the sender does not receive its own message");
    }

    @Test
    @DisplayName("no participant holds a reference to another participant; a member holds one, to the room")
    void everyoneHoldsTheRoom() {
        assertFalse(holdsAny(Member.class, PARTICIPANTS));
        assertFalse(holdsAny(Guest.class, PARTICIPANTS));
        assertEquals(1, heldBy(Member.class).stream().filter(t -> t == ChatRoom.class).count());
    }

    @Test
    @DisplayName("Main runs stage one, stage three and the room, and prints the figures on the slides")
    void mainOutput() {
        assertEquals(List.of(
                "Stage one: 4 members hold 12 references to each other.",
                "Stage three: Elif sends Mert a private message.",
                "  delivered to: [Burak, Mert, Can]",
                "  Mert shows:   [Elif (private): Your review is late.]",
                "  Burak shows:  []",
                "  Can shows:    [Elif: Your review is late.]",
                "Chat room: Elif sends Mert a private message.",
                "  delivered to: [Mert]",
                "  Mert shows:   [Elif (private): Your review is late.]",
                "  Can shows:    []",
                "Mert blocks Burak; Burak says something to everyone.",
                "  Elif shows:   [Burak: Lunch at one?]",
                "  Mert shows:   [Elif (private): Your review is late.]",
                "  Can shows:    [Burak: Lunch at one?]"), by(() -> Main.main(new String[0])));
    }
}
