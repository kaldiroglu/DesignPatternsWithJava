package dev.kaldiroglu.dp.behavioral.templateMethod.gof.solution;

/** A <b>ConcreteClass</b>: writes the two steps it must, and leaves the hook alone. */
public final class DrawApplication extends Application {

    @Override
    protected boolean canOpenDocument(String name) {
        return name.endsWith(".draw");
    }

    @Override
    protected Document doCreateDocument(String name) {
        return new DrawDocument(name);
    }
}
