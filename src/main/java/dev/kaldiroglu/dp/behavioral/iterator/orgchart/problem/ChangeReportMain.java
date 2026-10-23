package dev.kaldiroglu.dp.behavioral.iterator.orgchart.problem;

/**
 * HR compares two charts. A callback walks one chart at a time, so the report copies both
 * charts first.
 */
public final class ChangeReportMain {

    public static void main(String[] args) {
        CallbackDepartment before = SampleCompany.build("Deniz");
        // three people are new after the reorganization
        CallbackDepartment after = SampleCompany.build("Zeynep", "Burak", "Ece");
        ChangeReport report = new ChangeReport();
        System.out.println("HR compares two charts: "
                + report.firstDifference(before, after).orElse("none"));
        System.out.println("All changes: " + report.allDifferences(before, after));
        System.out.println("A callback walks one chart at a time,"
                + " so the report first copied both charts whole.");
    }
}
