package dev.kaldiroglu.dp.behavioral.iterator.orgchart.solution;

/**
 * HR compares two charts. The report moves one iterator in each chart, side by side.
 */
public final class ChangeReportMain {

    public static void main(String[] args) {
        Department company = SampleCompany.build("Deniz");
        // three people are new after the reorganization
        Department reorganized = SampleCompany.build("Zeynep", "Burak", "Ece");
        ChangeReport report = new ChangeReport();
        System.out.println("First change: "
                + report.firstDifference(company, reorganized).orElse("none"));
        System.out.println("All changes:");
        for (String change : report.allDifferences(company, reorganized)) {
            System.out.println("  " + change);
        }
    }
}
