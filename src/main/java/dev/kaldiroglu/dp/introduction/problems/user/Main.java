package dev.kaldiroglu.dp.introduction.problems.user;

/**
 * Three users through {@link UserProcessor#processUser}: each answer needs all seven
 * arguments, even when the decision uses only one of them.
 */
public class Main {
    public static void main(String[] args) {
        UserProcessor processor = new UserProcessor();

        // A user with no data at all: age 0.
        User child = new User();

        // An adult on a trial whose days have run out.
        User trialUser = new User() {
            @Override
            public int getAge() {
                return 30;
            }
        };

        System.out.println("No user          : "
                + processor.processUser(null, false, "TRIAL", 1, false, "IT", true));
        System.out.println("A child          : "
                + processor.processUser(child, false, "TRIAL", 1, false, "IT", true));
        System.out.println("An expired trial : "
                + processor.processUser(trialUser, false, "TRIAL", 1, false, "IT", true));
    }
}
