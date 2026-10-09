package dev.kaldiroglu.dp.behavioral.mediator.chat.problem.directory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Stage two: one shared list of members. A new member is added once, here. */
public final class Directory {

    private final List<Member> members = new ArrayList<>();

    public void add(Member member) {
        members.add(member);
    }

    public List<Member> members() {
        return List.copyOf(members);
    }

    public Optional<Member> find(String name) {
        return members.stream().filter(m -> m.name().equals(name)).findFirst();
    }
}
