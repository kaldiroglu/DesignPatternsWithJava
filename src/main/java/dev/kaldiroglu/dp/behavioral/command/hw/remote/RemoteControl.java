package dev.kaldiroglu.dp.behavioral.command.hw.remote;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * The <b>Invoker</b>: buttons, and an undo button.
 * <p>
 * Each button holds a way to <em>make</em> a command rather than a command. A command that
 * remembers what it did cannot be pressed twice and undone twice if it is the same object —
 * the second press would overwrite what the first remembered. So every press makes a fresh
 * one, and the history holds them all. This is GoF implementation issue 2 (supporting undo
 * and redo): a command with state may have to be copied before it goes on the history.
 */
public final class RemoteControl {

    private final Map<String, Supplier<Command>> buttons = new HashMap<>();
    private final Deque<Command> history = new ArrayDeque<>();

    public void assign(String button, Supplier<Command> command) {
        buttons.put(button, command);
    }

    public void press(String button) {
        Supplier<Command> maker = buttons.get(button);
        if (maker == null) {
            throw new IllegalArgumentException("nothing assigned to " + button);
        }
        Command command = maker.get();
        command.execute();
        history.push(command);
    }

    public void undo() {
        if (!history.isEmpty()) {
            history.pop().undo();
        }
    }

    /** A remote set up the way the homework describes it. */
    public static RemoteControl standardFor(Television tv) {
        RemoteControl remote = new RemoteControl();
        remote.assign("on", () -> new TurnOn(tv));
        remote.assign("off", () -> new TurnOff(tv));
        remote.assign("volume+", () -> new VolumeUp(tv));
        remote.assign("volume-", () -> new VolumeDown(tv));
        for (int channel = 0; channel <= 9; channel++) {
            int selected = channel;
            remote.assign(String.valueOf(channel), () -> new SelectChannel(tv, selected));
        }
        return remote;
    }
}
