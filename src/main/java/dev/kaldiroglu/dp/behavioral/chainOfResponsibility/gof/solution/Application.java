package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.solution;

/** A <b>ConcreteHandler</b> at the end of every chain: the application's general help. */
public final class Application extends HelpHandler {

    public Application(String topic) {
        super(null, topic);
    }
}
