package dev.kaldiroglu.dp.structural.bridge.file.solution;

/** A ConcreteImplementor: the FileNet document store. */
public class FileNetProvider extends InMemoryProvider {

    public FileNetProvider() {
        super("FileNet");
    }
}
