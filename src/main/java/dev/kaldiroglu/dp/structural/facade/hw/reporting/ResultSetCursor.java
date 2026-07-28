package dev.kaldiroglu.dp.structural.facade.hw.reporting;

import java.util.List;

/** A subsystem type, with a subsystem-shaped API. */
public class ResultSetCursor {

    private final List<String> rows;
    private int position;

    public ResultSetCursor(List<String> rows) {
        this.rows = List.copyOf(rows);
    }

    public boolean advance() {
        return position++ < rows.size();
    }

    public String current() {
        return rows.get(position - 1);
    }

    public int rowCount() {
        return rows.size();
    }
}
