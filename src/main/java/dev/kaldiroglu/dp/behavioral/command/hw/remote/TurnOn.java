package dev.kaldiroglu.dp.behavioral.command.hw.remote;

public final class TurnOn implements Command {

    private final Television tv;

    public TurnOn(Television tv) { this.tv = tv; }

    @Override
    public void execute() { tv.turnOn(); }

    @Override
    public void undo() { tv.turnOff(); }
}
