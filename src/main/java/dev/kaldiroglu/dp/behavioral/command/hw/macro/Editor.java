package dev.kaldiroglu.dp.behavioral.command.hw.macro;

/** The <b>Receiver</b>: a line of text with the cursor at its end. */
public final class Editor {

    private final StringBuilder text = new StringBuilder();

    public void type(String words) {
        text.append(words);
    }

    public void deleteLast(int count) {
        text.setLength(Math.max(0, text.length() - count));
    }

    public void upperCaseLastWord() {
        int start = text.lastIndexOf(" ") + 1;
        String word = text.substring(start).toUpperCase();
        text.replace(start, text.length(), word);
    }

    public String text() {
        return text.toString();
    }
}
