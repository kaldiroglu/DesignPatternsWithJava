package dev.kaldiroglu.dp.behavioral.mediator.gof.solution;

/**
 * The <b>Mediator</b>: GoF's {@code DialogDirector}. Every widget reports its changes here,
 * and only here.
 */
public abstract class DialogDirector {

    public abstract void widgetChanged(Widget widget);
}
