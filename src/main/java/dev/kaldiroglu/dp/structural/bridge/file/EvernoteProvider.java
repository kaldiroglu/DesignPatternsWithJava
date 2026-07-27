package dev.kaldiroglu.dp.structural.bridge.file;

/** A ConcreteImplementor: the Evernote document store. */
public class EvernoteProvider extends InMemoryProvider {

    public EvernoteProvider() {
        super("Evernote");
    }
}
