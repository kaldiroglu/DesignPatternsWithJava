package dev.kaldiroglu.dp.structural.bridge.retrofit;

import java.util.List;
import java.util.Objects;

/**
 * The Abstraction: the interface the regulation requires, which we do not get to design.
 * <p>
 * The whole retrofit is the field below. The required shape sits on top; the engine we
 * already have sits behind a reference; and {@code submit} is a higher-level operation
 * composed from the engine's primitives — open, pull, release — none of which the regulator
 * has ever heard of.
 * <p>
 * The engine is not rewritten and not recompiled. {@link LegacyEngine#reportDirectly} still
 * works, and its callers never learn that any of this happened.
 */
public abstract class RegulatoryReport {

    protected VendorClient engine;

    protected RegulatoryReport(VendorClient engine) {
        this.engine = Objects.requireNonNull(engine);
    }

    /** Bridge, not Adapter: the engine can be swapped on a report that already exists. */
    public void setEngine(VendorClient engine) {
        this.engine = Objects.requireNonNull(engine);
    }

    /** The one operation the regulation names. Everything else here is ours. */
    public final List<String> submit(String period) {
        String handle = engine.open("ledger");
        try {
            return decorateRows(engine.pull(handle, statementFor(period)));
        } finally {
            engine.release(handle);
        }
    }

    /** Which engine answered, for the audit trail the regulation also wants. */
    public String engineName() {
        return engine.name();
    }

    protected abstract String statementFor(String period);

    protected List<String> decorateRows(List<String> rows) {
        return rows;
    }
}
