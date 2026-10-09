package dev.kaldiroglu.dp.behavioral.command.hw.macro;

/** Shows edits recorded in one editor and replayed in another, through a copy of each command. */
public class Main {

    public static void main(String[] args) {
        Editor first = new Editor();
        MacroRecorder recorder = new MacroRecorder();

        recorder.start();
        recorder.run(new TypeText(first, "hello world"));
        recorder.run(new UpperCaseLastWord(first));
        recorder.run(new TypeText(first, "!!"));
        recorder.run(new DeleteLast(first, 1));
        recorder.stop();
        System.out.println("Recorded " + recorder.size() + " edits. First editor: " + first.text());

        Editor second = new Editor();
        second.type("goodbye ");
        recorder.replayOn(second);
        System.out.println("Replayed on the second editor: " + second.text());
        System.out.println("The first editor did not change: " + first.text());
    }
}
