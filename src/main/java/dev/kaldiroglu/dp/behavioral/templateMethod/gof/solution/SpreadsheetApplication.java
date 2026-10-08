package dev.kaldiroglu.dp.behavioral.templateMethod.gof.solution;

/**
 * A <b>ConcreteClass</b> that also uses the hook. Compare {@code problem.SpreadsheetApplication},
 * which added the same step in its own copy of the algorithm. Here the step goes where the
 * template method allows it, and nowhere else.
 */
public final class SpreadsheetApplication extends Application {

    @Override
    protected boolean canOpenDocument(String name) {
        return name.endsWith(".sheet");
    }

    @Override
    protected Document doCreateDocument(String name) {
        return new SpreadsheetDocument(name);
    }

    @Override
    protected void aboutToOpenDocument(Document document) {
        record("remember " + document.name() + " as the last file");
    }
}
