package dev.kaldiroglu.dp.behavioral.mediator.chat.problem.directory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.kaldiroglu.dp.behavioral.mediator.Printed.codeOf;
import static dev.kaldiroglu.dp.behavioral.mediator.Printed.countOf;
import static org.junit.jupiter.api.Assertions.*;

/** Stage two: the members share one directory, and every sender applies the rules. */
class DirectoryTest {

    @Test
    @DisplayName("a new member is added once, to the directory, and everyone can reach them")
    void aNewMemberIsAddedOnce() {
        Directory directory = new Directory();
        Member elif = new Member("Elif", directory);
        Member burak = new Member("Burak", directory);
        Member can = new Member("Can", directory);

        burak.say("Lunch at one?");

        assertEquals(3, directory.members().size());
        assertEquals(List.of("Burak: Lunch at one?"), elif.inbox());
        assertEquals(List.of("Burak: Lunch at one?"), can.inbox());
        assertTrue(burak.inbox().isEmpty(), "the sender skips itself");
    }

    @Test
    @DisplayName("the sender skips anyone who blocked it, for messages to everyone and for private ones")
    void theSenderAppliesTheBlock() {
        Directory directory = new Directory();
        Member elif = new Member("Elif", directory);
        Member burak = new Member("Burak", directory);
        Member mert = new Member("Mert", directory);
        mert.block("Burak");

        burak.say("Lunch at one?");
        burak.whisper("Mert", "Are you there?");
        elif.whisper("Mert", "Your review is late.");

        assertEquals(List.of("Burak: Lunch at one?"), elif.inbox());
        assertEquals(List.of("Elif (private): Your review is late."), mert.inbox());
    }

    @Test
    @DisplayName("the block rule is written in the sending code, once for say and once for whisper")
    void theRulesAreInTheSender() {
        String member = codeOf("mediator/chat/problem/directory/Member.java");
        assertEquals(2, countOf(member, "blocked.contains(name)"));
        assertEquals(0, countOf(codeOf("mediator/chat/problem/directory/Directory.java"), "blocked"));
    }
}
