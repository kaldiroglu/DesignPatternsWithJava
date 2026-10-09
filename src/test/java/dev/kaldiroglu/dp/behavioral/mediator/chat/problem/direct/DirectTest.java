package dev.kaldiroglu.dp.behavioral.mediator.chat.problem.direct;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Stage one: every member holds every other member. */
class DirectTest {

    private static List<Member> fourWhoHaveMet() {
        List<Member> team = List.of(new Member("Elif"), new Member("Burak"), new Member("Mert"),
                new Member("Selin"));
        for (int i = 0; i < team.size(); i++) {
            for (int j = i + 1; j < team.size(); j++) {
                team.get(i).meet(team.get(j));
            }
        }
        return team;
    }

    @Test
    @DisplayName("four members hold twelve references to each other: six lines, held from both ends")
    void twelveReferences() {
        List<Member> team = fourWhoHaveMet();
        assertEquals(4, team.size());
        assertEquals(12, team.stream().mapToInt(Member::references).sum());
        team.forEach(m -> assertEquals(3, m.references()));
    }

    @Test
    @DisplayName("it works: a private message goes straight to its receiver and to nobody else")
    void aPrivateMessageReachesOnlyItsReceiver() {
        List<Member> team = fourWhoHaveMet();
        Member elif = team.get(0), burak = team.get(1), mert = team.get(2), selin = team.get(3);

        elif.whisper(mert, "Your review is late.");

        assertEquals(List.of("Elif (private): Your review is late."), mert.inbox());
        assertTrue(burak.inbox().isEmpty());
        assertTrue(selin.inbox().isEmpty());
    }

    @Test
    @DisplayName("a fifth member must be added to four lists; a member who was not told never sends to them")
    void aFifthMember() {
        List<Member> team = fourWhoHaveMet();
        Member can = new Member("Can");
        team.get(0).meet(can);                  // only Elif is told

        team.get(1).say("Lunch at one?");       // Burak does not know Can

        assertTrue(can.inbox().isEmpty());
        team.get(0).say("Hello");
        assertEquals(List.of("Elif: Hello"), can.inbox());
        assertEquals(14, team.stream().mapToInt(Member::references).sum() + can.references(),
                "one new line, held from both ends; three more lines are still missing");
    }
}
