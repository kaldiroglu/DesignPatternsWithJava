package dev.kaldiroglu.dp.structural.facade.hw.reporting;

/**
 * A facade that did not simplify anything — and the exercise is to see why.
 * <p>
 * Every method here is a thin wrapper, which looks like progress. Read the
 * <strong>signatures</strong> instead: a caller has to build a {@link QueryPlan}, hold a
 * {@link ResultSetCursor}, and know that {@code advance} must be called before
 * {@code current}. All three are subsystem concepts.
 * <p>
 * A facade is judged by what its clients have to <em>name</em>, not by how many classes it
 * wraps. This one still forces its callers to import the subsystem, so it has added a layer
 * without removing a dependency.
 */
public class LeakyReportFacade {

    private final QueryEngine engine = new QueryEngine();
    private final PdfRenderer renderer = new PdfRenderer();

    public QueryPlan planFor(String table, String filter, int limit) {
        return engine.plan(table, filter, limit);
    }

    public ResultSetCursor run(QueryPlan plan) {
        return engine.execute(plan);
    }

    public byte[] toPdf(String title, java.util.List<String> rows) {
        return renderer.render(title, rows);
    }
}
