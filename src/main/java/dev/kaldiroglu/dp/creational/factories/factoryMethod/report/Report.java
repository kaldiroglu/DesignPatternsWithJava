package dev.kaldiroglu.dp.creational.factories.factoryMethod.report;

public abstract class Report {
    private String name;

    public Report(String name){
        this.name = name;
    }

    public abstract void open();
    public abstract void print();
    public abstract void save();
    public abstract void revert();
    public abstract void close();
}
