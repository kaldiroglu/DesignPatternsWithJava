package dev.kaldiroglu.dp.structural.bridge.basic.problem;

/** The other cell: the second refinement, done the same 2nd way — and duplicated for it. */
public class AnotherConcreteImplementation2 extends AnotherSubAbstraction {

    @Override
    public void doIt() {
        System.out.println("AnotherSubAbstraction, implementation 2: I am doing it!");
    }
}
