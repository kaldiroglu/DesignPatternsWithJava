package dev.kaldiroglu.dp.structural.composite.hw.surveyform;

/**
 * Homework 3 — the survey form.
 * <p>
 * Two things to watch: an operation that returns a list rather than a number, and what
 * happens when the transparent interface is taken up on its offer.
 */
public class Main {

    public static void main(String[] args) {
        FormElement form = new Section("Course feedback").with(
                new Section("About you").with(
                        new Question("Name", false).answer("Bora"),
                        new Question("Role", true).answer("engineer"),
                        new Question("Years of experience", true)),
                new Section("The session").with(
                        new Question("Which pattern was clearest?", true).answer("Composite"),
                        new Question("What should we cut?", false),
                        new Section("Exercises").with(
                                new Question("Was the homework the right length?", true),
                                new Question("Anything else?", false))));

        System.out.println(form.render(""));
        System.out.printf("%n%d of %d answered%n", form.answeredCount(), form.questionCount());

        System.out.println("\nvalidation problems, gathered from the whole tree:");
        form.validate().forEach(problem -> System.out.println("  - " + problem));

        System.out.println("""

                Every element above was handled through FormElement. Nothing asked
                whether it held a section or a question, at any depth.

                That is what transparency buys. Here is what it costs:""");
        try {
            new Question("Name", false).add(new Question("Nested", false));
        } catch (UnsupportedOperationException e) {
            System.out.println("  " + e.getMessage());
            System.out.println("  ...and that line compiled without complaint.");
        }
    }
}
