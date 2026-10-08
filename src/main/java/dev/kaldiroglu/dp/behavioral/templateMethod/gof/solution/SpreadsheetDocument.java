package dev.kaldiroglu.dp.behavioral.templateMethod.gof.solution;

import java.util.List;

/** A <b>ConcreteClass</b> for documents: a spreadsheet reads cells. */
public final class SpreadsheetDocument extends Document {

    public SpreadsheetDocument(String name) {
        super(name);
    }

    @Override
    protected void doRead(List<String> events) {
        events.add("read cells from " + name());
    }
}
