package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.solution;

/**
 * A control on the screen. Its successor is usually its parent widget, but any
 * {@link HelpHandler} can be the next link.
 */
public abstract class Widget extends HelpHandler {

    protected Widget(HelpHandler successor, String topic) {
        super(successor, topic);
    }
}
