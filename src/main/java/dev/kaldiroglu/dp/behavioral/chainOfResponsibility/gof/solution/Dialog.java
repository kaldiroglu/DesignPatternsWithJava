package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.solution;

/**
 * A <b>ConcreteHandler</b>: a dialog. A dialog is not inside another widget, so GoF give it
 * the application as its successor.
 */
public final class Dialog extends Widget {

    public Dialog(Application application, String topic) {
        super(application, topic);
    }
}
