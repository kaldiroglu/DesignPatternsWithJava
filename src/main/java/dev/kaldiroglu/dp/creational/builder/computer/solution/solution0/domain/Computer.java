package dev.kaldiroglu.dp.creational.ch04.computer.solution.solution0.domain;

public abstract class Computer {
    protected String name;

    public Computer() {
    }

    public Computer(String name) {
        this.name = name;
    }

    public abstract void start();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
