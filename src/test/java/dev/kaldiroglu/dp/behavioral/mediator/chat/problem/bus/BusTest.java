package dev.kaldiroglu.dp.behavioral.mediator.chat.problem.bus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.kaldiroglu.dp.behavioral.mediator.Fields.holdsAny;
import static org.junit.jupiter.api.Assertions.*;

/** Stage three: a message bus, and clients that decide what to show. */
class BusTest {

    @Test
    @DisplayName("the bus delivers Elif's private message to Burak, Mert and Can, and the guest client shows it")
    void theGuestShowsThePrivateMessage() {
        MessageBus bus = new MessageBus();
        ChatClient elif = new ChatClient("Elif"), burak = new ChatClient("Burak"), mert = new ChatClient("Mert");
        GuestClient can = new GuestClient("Can");
        List.of(elif, burak, mert).forEach(bus::subscribe);
        bus.subscribe(can);

        bus.publish(new Message("Elif", "Mert", "Your review is late."));

        assertEquals(List.of("Burak", "Mert", "Can"), bus.deliveries());
        assertEquals(3, bus.deliveries().size());
        assertEquals(List.of("Elif (private): Your review is late."), mert.shown());
        assertEquals(List.of(), burak.shown(), "a team client filters");
        assertEquals(List.of("Elif: Your review is late."), can.shown(), "the guest client does not");
        assertTrue(elif.shown().isEmpty(), "the sender is not delivered to");
    }

    @Test
    @DisplayName("a message to everyone is shown by every client except the sender")
    void aMessageToEveryone() {
        MessageBus bus = new MessageBus();
        ChatClient elif = new ChatClient("Elif"), burak = new ChatClient("Burak");
        bus.subscribe(elif);
        bus.subscribe(burak);

        bus.publish(new Message("Burak", null, "Lunch at one?"));

        assertEquals(List.of("Burak: Lunch at one?"), elif.shown());
        assertTrue(burak.shown().isEmpty());
    }

    @Test
    @DisplayName("no client holds a reference to another client")
    void noClientKnowsAnother() {
        List<Class<?>> clients = List.of(Client.class, ChatClient.class, GuestClient.class);
        assertFalse(holdsAny(ChatClient.class, clients));
        assertFalse(holdsAny(GuestClient.class, clients));
    }
}
