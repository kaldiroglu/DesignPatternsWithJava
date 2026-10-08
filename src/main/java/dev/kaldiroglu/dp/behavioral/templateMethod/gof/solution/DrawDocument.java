package dev.kaldiroglu.dp.behavioral.templateMethod.gof.solution;

import java.util.List;

/** A <b>ConcreteClass</b> for documents: a drawing reads shapes. */
public final class DrawDocument extends Document {

    public DrawDocument(String name) {
        super(name);
    }

    @Override
    protected void doRead(List<String> events) {
        events.add("read shapes from " + name());
    }
}
