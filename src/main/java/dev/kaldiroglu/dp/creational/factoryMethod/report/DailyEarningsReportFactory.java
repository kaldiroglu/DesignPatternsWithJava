package dev.kaldiroglu.dp.creational.factoryMethod.report;

import java.time.LocalDate;

public class DailyEarningsReportFactory implements ReportFactory{
    private final LocalDate date;

    public DailyEarningsReportFactory(LocalDate date) {
        this.date = date;
    }
    @Override
    public Report generateReport() {
        return new DailyEarningsReport("Daily Earnings Report for " + date);
    }
}
