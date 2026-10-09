package dev.kaldiroglu.dp.behavioral.mediator.chat.problem.directory;

/**
 * Three members share one directory, and Mert blocks Burak. The block works, but only
 * because every sender checks it in its own sending code.
 */
public final class Main {

    public static void main(String[] args) {
        Directory directory = new Directory();
        Member elif = new Member("Elif", directory);
        Member burak = new Member("Burak", directory);
        Member mert = new Member("Mert", directory);
        mert.block("Burak");

        burak.say("Lunch at one?");
        burak.whisper("Mert", "Are you there?");
        elif.whisper("Mert", "Your review is late.");

        System.out.println("Members in the directory: " + directory.members().size());
        System.out.println("Elif's inbox: " + elif.inbox());
        System.out.println("Mert's inbox: " + mert.inbox() + "  (Burak is blocked)");
        System.out.println("The block is checked in Member.say and Member.whisper, by the sender.");
    }
}
