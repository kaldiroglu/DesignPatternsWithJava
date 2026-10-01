package dev.kaldiroglu.dp.behavioral.command.hw.remote;

/** The <b>Command</b>: what every button on the remote does when pressed, and how to take it back. */
public interface Command {

    void execute();

    void undo();
}
