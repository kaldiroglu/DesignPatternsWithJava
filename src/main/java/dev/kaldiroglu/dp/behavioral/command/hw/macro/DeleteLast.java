package dev.kaldiroglu.dp.behavioral.command.hw.macro;

public record DeleteLast(Editor editor, int count) implements EditorCommand {

    @Override
    public void execute() { editor.deleteLast(count); }

    @Override
    public EditorCommand on(Editor other) { return new DeleteLast(other, count); }
}
