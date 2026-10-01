package dev.kaldiroglu.dp.behavioral.command.gof;

/** What the user last copied. One per application, shared by every document in it. */
public final class Clipboard {

    private String contents = "";

    public void put(String text) {
        contents = text;
    }

    public String contents() {
        return contents;
    }
}
