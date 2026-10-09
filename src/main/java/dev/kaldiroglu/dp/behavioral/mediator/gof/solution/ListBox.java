package dev.kaldiroglu.dp.behavioral.mediator.gof.solution;

/** A <b>ConcreteColleague</b>: a list. It reports a selection and knows no other widget. */
public final class ListBox extends Widget {

    private String selection = "";

    public ListBox(DialogDirector director) {
        super(director);
    }

    public void select(String item) {
        selection = item;
        changed();
    }

    public String selection() {
        return selection;
    }
}
