package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.hw.maintenance;

/**
 * Puts five requests in the queue and processes them. The queue knows only the first
 * developer; each request goes along the chain to the one who takes it.
 */
public final class Main {

    public static void main(String[] args) {
        Developer first = new BugFixer("Ali");
        first.then(new UiDesigner("Zeynep"))
                .then(new FeatureDeveloper("Kerem"))
                .then(new ProjectTeam("the project team"));

        RequestQueue queue = new RequestQueue(first);
        queue.add(new Request(Request.Kind.BUG, "Login fails", 1));
        queue.add(new Request(Request.Kind.IMPROVEMENT, "Faster search", 4));
        queue.add(new Request(Request.Kind.UI_CHANGE, "New logo", 2));
        queue.add(new Request(Request.Kind.IMPROVEMENT, "New report engine", 30));
        queue.add(new Request(Request.Kind.PROJECT, "Mobile app", 120));

        queue.processAll().forEach(System.out::println);
    }
}
