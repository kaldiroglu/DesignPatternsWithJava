package dev.kaldiroglu.dp.creational.factoryMethod.report;

import java.time.LocalDate;

public class DailySalesReportFactory implements ReportFactory{
    private final LocalDate date;

    public DailySalesReportFactory(LocalDate date) {
        this.date = date;
    }

    @Override
    public Report generateReport() {
        return new DailySalesReport("Daily Sales Report for " + date);
    }
}
