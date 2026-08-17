package dev.kaldiroglu.dp.structural.bridge.file.solution;

/** A ConcreteImplementor: the Evernote document store. */
public class EvernoteProvider extends InMemoryProvider {

    public EvernoteProvider() {
        super("Evernote");
    }
}
