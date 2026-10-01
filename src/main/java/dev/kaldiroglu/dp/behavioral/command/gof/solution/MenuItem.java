package dev.kaldiroglu.dp.behavioral.command.gof.solution;

import java.util.Objects;

/**
 * The <b>Invoker</b>: GoF's {@code MenuItem}, which "asks the command to carry out the
 * request".
 * <p>
 * Compare {@code problem.MenuItem}. That one imported the application and branched on its
 * own label. This one imports nothing from any application — it is toolkit code, and it
 * could ship in a library compiled years before the application that uses it.
 */
public final class MenuItem {

    private final String label;
    private Command command;

    public MenuItem(String label, Command command) {
        this.label = label;
        this.command = Objects.requireNonNull(command);
    }

    public String label() {
        return label;
    }

    /** The same item can be given a different job while the program runs. */
    public void setCommand(Command command) {
        this.command = Objects.requireNonNull(command);
    }

    public void clicked() {
        command.execute();
    }
}
