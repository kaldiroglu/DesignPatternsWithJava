package dev.kaldiroglu.dp.creational.factoryMethod.report;

public class DailySalesReport extends Report{

    public DailySalesReport(String name) {
        super(name);
    }

    @Override
    public void open() {

    }

    @Override
    public void print() {
        System.out.println("Printing " + getName());
    }

    @Override
    public void save() {

    }

    @Override
    public void revert() {

    }

    @Override
    public void close() {

    }
}
