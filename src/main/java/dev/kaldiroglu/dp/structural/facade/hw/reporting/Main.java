package dev.kaldiroglu.dp.structural.facade.hw.reporting;

/** Homework 2 — two facades over one subsystem, and only one of them helped. */
public class Main {

    public static void main(String[] args) {
        // The leaky one. Count the subsystem types the caller had to name.
        LeakyReportFacade leaky = new LeakyReportFacade();
        QueryPlan plan = leaky.planFor("orders", "month = 7", 100);   // <- subsystem type
        ResultSetCursor cursor = leaky.run(plan);                     // <- subsystem type
        java.util.List<String> rows = new java.util.ArrayList<>();
        while (cursor.advance()) {                                    // <- subsystem protocol
            rows.add(cursor.current());
        }
        byte[] viaLeaky = leaky.toPdf("orders — monthly", rows);

        // The real one.
        byte[] viaFacade = new ReportFacade(new QueryEngine(), new PdfRenderer())
                .monthlyReport("orders", "month = 7", 100);

        System.out.println("same bytes: " + java.util.Arrays.equals(viaLeaky, viaFacade));
        System.out.println("""

                Both produce the same report. Only one of them let the caller be
                written without importing QueryPlan, ResultSetCursor, or the rule
                that advance() comes before current().

                A facade is judged by what its clients have to name.""");
    }
}
