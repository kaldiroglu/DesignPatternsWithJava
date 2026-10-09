package dev.kaldiroglu.dp.behavioral.observer.hw.inbox;

import java.util.ArrayList;
import java.util.List;

/**
 * Three views of one inbox, each a lambda. The inbox pushes the new message; the badge
 * also pulls the unread count, which is not in the message.
 */
public final class Main {

    public static void main(String[] args) {
        Inbox inbox = new Inbox();
        List<String> subjects = new ArrayList<>();
        inbox.onNewMessage(message -> System.out.println("Badge: " + inbox.unread() + " unread"));
        inbox.onNewMessage(message -> subjects.add(message.subject()));
        inbox.onNewMessage(message -> System.out.println("Pop-up: new message from " + message.from()));

        inbox.receive(new Inbox.Message("Ayse", "Meeting at ten"));
        inbox.receive(new Inbox.Message("Deniz", "October sales"));
        System.out.println("List: " + subjects);
        inbox.readAll();
        System.out.println("After reading all: " + inbox.unread() + " unread");
    }
}
