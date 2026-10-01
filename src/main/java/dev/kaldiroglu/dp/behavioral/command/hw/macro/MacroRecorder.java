package dev.kaldiroglu.dp.behavioral.command.hw.macro;

import java.util.ArrayList;
import java.util.List;

/**
 * The <b>Invoker</b>, with a record button.
 * <p>
 * While recording, every command the user runs is kept as well as executed. Replaying on
 * another editor runs a copy of each command aimed at that editor; the recorded commands
 * themselves are never run again, so the editor the macro was recorded in is not touched.
 */
public final class MacroRecorder {

    private final List<EditorCommand> recorded = new ArrayList<>();
    private boolean recording;

    public void start() {
        recorded.clear();
        recording = true;
    }

    public void stop() {
        recording = false;
    }

    public void run(EditorCommand command) {
        command.execute();
        if (recording) {
            recorded.add(command);
        }
    }

    public int size() {
        return recorded.size();
    }

    public void replayOn(Editor other) {
        for (EditorCommand command : recorded) {
            command.on(other).execute();
        }
    }
}
