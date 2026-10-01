package dev.kaldiroglu.dp.behavioral.command.hw.remote;

/**
 * Looks as if its undo is simply "volume down", and it is not. At the top of the range
 * the press changes nothing, and undoing it must change nothing either — so the command
 * remembers whether it actually moved the volume.
 */
public final class VolumeUp implements Command {

    private final Television tv;
    private boolean changed;

    public VolumeUp(Television tv) { this.tv = tv; }

    @Override
    public void execute() { changed = tv.volumeUp(); }

    @Override
    public void undo() {
        if (changed) {
            tv.volumeDown();
        }
    }
}
