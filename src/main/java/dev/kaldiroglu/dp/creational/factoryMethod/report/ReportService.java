package dev.kaldiroglu.dp.creational.factoryMethod.report;

public class ReportService {
    ReportFactory reportFactory;

    public ReportService(ReportFactory reportFactory) {
        this.reportFactory = reportFactory;
    }
    Report serveReport(){
        return reportFactory.generateReport();
    }
}
