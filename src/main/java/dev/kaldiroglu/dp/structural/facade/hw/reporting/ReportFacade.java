package dev.kaldiroglu.dp.structural.facade.hw.reporting;

import java.util.ArrayList;
import java.util.List;

/**
 * The Facade, done properly: one call, and not one subsystem type in the signature.
 * <p>
 * Compare {@link LeakyReportFacade}. The difference is not effort or line count — it is that
 * a caller of this class can be written without importing anything from the subsystem, and
 * therefore survives the subsystem being replaced.
 * <p>
 * The cursor protocol — advance before current — was knowledge every caller used to need.
 * It is now here, once.
 */
public class ReportFacade {

    private final QueryEngine engine;
    private final PdfRenderer renderer;

    public ReportFacade(QueryEngine engine, PdfRenderer renderer) {
        this.engine = engine;
        this.renderer = renderer;
    }

    /** Strings in, bytes out. Nothing in this signature belongs to the subsystem. */
    public byte[] monthlyReport(String table, String filter, int limit) {
        ResultSetCursor cursor = engine.execute(engine.plan(table, filter, limit));

        List<String> rows = new ArrayList<>();
        while (cursor.advance()) {
            rows.add(cursor.current());
        }
        return renderer.render(table + " — monthly", rows);
    }
}
