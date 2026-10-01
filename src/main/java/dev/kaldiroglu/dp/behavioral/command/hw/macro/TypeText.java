package dev.kaldiroglu.dp.behavioral.command.hw.macro;

public record TypeText(Editor editor, String words) implements EditorCommand {

    @Override
    public void execute() { editor.type(words); }

    @Override
    public EditorCommand on(Editor other) { return new TypeText(other, words); }
}
