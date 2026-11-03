package dev.kaldiroglu.dp.creational.factoryMethod.report;

import java.time.LocalDate;

/** Two services, one for each report factory. The date is fixed so the output never changes. */
public class Main {

    public static void main(String[] args) {
        LocalDate date = LocalDate.of(2026, 11, 1);

        new ReportService(new DailySalesReportFactory(date)).serveReport().print();
        new ReportService(new DailyEarningsReportFactory(date)).serveReport().print();
    }
}
