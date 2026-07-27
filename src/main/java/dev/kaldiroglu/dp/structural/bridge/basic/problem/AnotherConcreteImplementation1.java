package dev.kaldiroglu.dp.structural.bridge.basic.problem;

/** The other cell: the second refinement, done the same 1st way — and duplicated for it. */
public class AnotherConcreteImplementation1 extends AnotherSubAbstraction {

    @Override
    public void doIt() {
        System.out.println("AnotherSubAbstraction, implementation 1: I am doing it!");
    }
}
