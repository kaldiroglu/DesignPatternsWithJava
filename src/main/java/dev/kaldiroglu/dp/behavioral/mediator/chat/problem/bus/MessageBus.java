package dev.kaldiroglu.dp.behavioral.mediator.chat.problem.bus;

import java.util.ArrayList;
import java.util.List;

/**
 * Stage three: a message bus. Clients publish to it and subscribe to it; no client knows
 * another.
 * <p>
 * The bus delivers every message to every subscriber except the sender. Each client then
 * decides what to show. The decision "who may read this" is made by the receivers — so it
 * is only as good as the least careful client.
 */
public final class MessageBus {

    private final List<Client> subscribers = new ArrayList<>();
    private final List<String> deliveries = new ArrayList<>();

    public void subscribe(Client client) {
        subscribers.add(client);
    }

    public void publish(Message message) {
        for (Client client : List.copyOf(subscribers)) {
            if (!client.name().equals(message.from())) {
                deliveries.add(client.name());
                client.onMessage(message);
            }
        }
    }

    /** The clients each message was delivered to, in order. */
    public List<String> deliveries() {
        return List.copyOf(deliveries);
    }
}
