package dev.kaldiroglu.dp.behavioral.mediator.gof.solution;

/** A <b>ConcreteColleague</b>: a text field. */
public final class EntryField extends Widget {

    private String text = "";

    public EntryField(DialogDirector director) {
        super(director);
    }

    /** Called by the user typing: reports the change. */
    public void type(String text) {
        this.text = text;
        changed();
    }

    /** Called by the director: sets the text without reporting it back. */
    public void setText(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }
}
