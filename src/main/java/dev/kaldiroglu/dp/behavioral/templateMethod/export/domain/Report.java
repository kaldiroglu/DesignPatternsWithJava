package dev.kaldiroglu.dp.behavioral.templateMethod.export.domain;

import java.util.List;

/** A report: a title and its rows. */
public record Report(String title, List<Sale> sales) {

    public Report {
        sales = List.copyOf(sales);
    }
}
