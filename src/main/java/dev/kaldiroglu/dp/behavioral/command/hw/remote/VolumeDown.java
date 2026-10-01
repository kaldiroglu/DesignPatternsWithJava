package dev.kaldiroglu.dp.behavioral.command.hw.remote;

/** The mirror of {@link VolumeUp}: it remembers whether the press did anything. */
public final class VolumeDown implements Command {

    private final Television tv;
    private boolean changed;

    public VolumeDown(Television tv) { this.tv = tv; }

    @Override
    public void execute() { changed = tv.volumeDown(); }

    @Override
    public void undo() {
        if (changed) {
            tv.volumeUp();
        }
    }
}
