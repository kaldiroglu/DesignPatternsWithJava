package dev.kaldiroglu.dp.creational.factories.factoryMethod.report;

public class ReportService {
    ReportFactory reportFactory;

    public ReportService(ReportFactory reportFactory) {
        this.reportFactory = reportFactory;
    }
    Report serveReport(){
        return reportFactory.generateReport();
    }
}
