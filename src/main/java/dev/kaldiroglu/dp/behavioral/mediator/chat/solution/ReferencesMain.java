package dev.kaldiroglu.dp.behavioral.mediator.chat.solution;

import dev.kaldiroglu.dp.behavioral.mediator.chat.problem.direct.Member;

import java.util.List;

/**
 * Counts the references of stage one: four members who all hold each other.
 * <p>
 * In the chat room, a member holds one reference, to the room.
 */
public final class ReferencesMain {

    public static void main(String[] args) {
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
    }
}
