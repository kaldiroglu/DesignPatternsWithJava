package dev.kaldiroglu.dp.behavioral.command.hw.remote;

/** Undo needs nothing remembered: the television keeps its own channel and volume while off. */
public final class TurnOff implements Command {

    private final Television tv;

    public TurnOff(Television tv) { this.tv = tv; }

    @Override
    public void execute() { tv.turnOff(); }

    @Override
    public void undo() { tv.turnOn(); }
}
