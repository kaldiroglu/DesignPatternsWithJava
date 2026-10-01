package dev.kaldiroglu.dp.behavioral.command.hw.remote;

/**
 * The button whose undo has to remember something: the channel that was on before.
 * The request names the new channel; only the receiver knew the old one.
 */
public final class SelectChannel implements Command {

    private final Television tv;
    private final int channel;
    private int previous;

    public SelectChannel(Television tv, int channel) {
        this.tv = tv;
        this.channel = channel;
    }

    @Override
    public void execute() {
        previous = tv.channel();
        tv.selectChannel(channel);
    }

    @Override
    public void undo() { tv.selectChannel(previous); }
}
