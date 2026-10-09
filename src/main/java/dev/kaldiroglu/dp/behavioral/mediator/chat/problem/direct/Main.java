package dev.kaldiroglu.dp.behavioral.mediator.chat.problem.direct;

import java.util.List;

/**
 * Four members meet each other, then Can joins and only Elif is told. Burak's message never
 * reaches Can, because Burak's own list of members does not have him.
 */
public final class Main {

    public static void main(String[] args) {
        Member elif = new Member("Elif"), burak = new Member("Burak"),
                mert = new Member("Mert"), selin = new Member("Selin");
        List<Member> team = List.of(elif, burak, mert, selin);
        for (int i = 0; i < team.size(); i++) {
            for (int j = i + 1; j < team.size(); j++) {
                team.get(i).meet(team.get(j));
            }
        }
        System.out.println("References held by four members: "
                + team.stream().mapToInt(Member::references).sum());

        elif.whisper(mert, "Your review is late.");
        System.out.println("Mert's inbox:  " + mert.inbox());
        System.out.println("Burak's inbox: " + burak.inbox());

        Member can = new Member("Can");
        elif.meet(can);                          // only Elif is told about Can
        burak.say("Lunch at one?");
        elif.say("Hello");
        System.out.println("Can's inbox:   " + can.inbox() + "  (Burak's lunch message is missing)");
    }
}
