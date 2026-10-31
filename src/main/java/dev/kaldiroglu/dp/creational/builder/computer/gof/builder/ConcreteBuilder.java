package dev.kaldiroglu.dp.creational.ch04.computer.gof.builder;

public class ConcreteBuilder implements Builder{
    Product product;
    @Override
    public void buildPart() {
        product = new Product();
    }

    @Override
    public Product getResult() {
        return product;
    }
}
