package dev.kaldiroglu.dp.structural.facade.hw.reporting;

import java.util.List;

/** A subsystem class. */
public class QueryEngine {

    public QueryPlan plan(String table, String filter, int limit) {
        return new QueryPlan(table, filter, limit);
    }

    public ResultSetCursor execute(QueryPlan plan) {
        return new ResultSetCursor(List.of(
                plan.table() + " row 1", plan.table() + " row 2", plan.table() + " row 3"));
    }
}
