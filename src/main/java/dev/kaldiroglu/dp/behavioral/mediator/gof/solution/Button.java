package dev.kaldiroglu.dp.behavioral.mediator.gof.solution;

/** A <b>ConcreteColleague</b>: a button. */
public final class Button extends Widget {

    private boolean enabled;

    public Button(DialogDirector director) {
        super(director);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean enabled() {
        return enabled;
    }

    public void click() {
        if (enabled) {
            changed();
        }
    }
}
