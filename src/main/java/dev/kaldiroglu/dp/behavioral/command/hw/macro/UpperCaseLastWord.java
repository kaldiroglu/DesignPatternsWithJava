package dev.kaldiroglu.dp.behavioral.command.hw.macro;

public record UpperCaseLastWord(Editor editor) implements EditorCommand {

    @Override
    public void execute() { editor.upperCaseLastWord(); }

    @Override
    public EditorCommand on(Editor other) { return new UpperCaseLastWord(other); }
}
