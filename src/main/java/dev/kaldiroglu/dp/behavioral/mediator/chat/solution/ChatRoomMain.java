package dev.kaldiroglu.dp.behavioral.mediator.chat.solution;

/**
 * The pattern on its own. Elif sends Mert a private message, then Mert blocks Burak and
 * Burak says something to everyone. The room decides who receives each message, and this
 * class prints what every participant has after each step.
 */
public final class ChatRoomMain {

    public static void main(String[] args) {
        ChatRoom room = new ChatRoom();
        Member elif = new Member("Elif", room);
        Member burak = new Member("Burak", room);
        Member mert = new Member("Mert", room);
        Guest can = new Guest("Can", room);

        elif.whisper("Mert", "Your review is late.");
        System.out.println("Elif sends Mert a private message:");
        print(room, elif, burak, mert, can);

        mert.block("Burak");
        burak.say("Lunch at one?");
        System.out.println("Mert blocks Burak; Burak says something to everyone:");
        print(room, elif, burak, mert, can);
    }

    private static void print(ChatRoom room, Member elif, Member burak, Member mert, Guest can) {
        System.out.println("delivered to " + room.deliveries() + "\nElif shows " + elif.shown()
                + "\nBurak shows " + burak.shown() + "\nMert shows " + mert.shown()
                + "\nCan shows " + can.shown() + "\n");
    }
}
