package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.solution;

/** A <b>ConcreteHandler</b>: a button, with or without its own help topic. */
public final class Button extends Widget {

    public Button(Widget parent, String topic) {
        super(parent, topic);
    }

    public Button(Widget parent) {
        this(parent, null);
    }
}
