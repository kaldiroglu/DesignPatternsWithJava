package dev.kaldiroglu.dp.behavioral.command.gof.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * A <b>ConcreteCommand</b> made of commands: GoF's {@code MacroCommand}.
 * <p>
 * "A MacroCommand has no explicit receiver, because the commands it sequences define their
 * own receiver" (p. 235). It is a Composite of commands, and a menu item holding one
 * cannot tell it from any other command.
 */
public final class MacroCommand implements Command {

    private final List<Command> commands = new ArrayList<>();

    public MacroCommand add(Command command) {
        commands.add(command);
        return this;
    }

    public void remove(Command command) {
        commands.remove(command);
    }

    public int size() {
        return commands.size();
    }

    @Override
    public void execute() {
        for (Command command : commands) {
            command.execute();
        }
    }
}
