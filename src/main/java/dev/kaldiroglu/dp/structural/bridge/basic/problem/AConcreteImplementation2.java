package dev.kaldiroglu.dp.structural.bridge.basic.problem;

/**
 * One cell of the grid: the first refinement, done the 2nd way.
 * <p>
 * Read the {@code extends} clause as the claim it makes: this implementation <em>is a</em>
 * refinement. Change the implementation and you have changed the object's type, which is why
 * nothing here can switch implementation once it exists.
 */
public class AConcreteImplementation2 extends ASubAbstraction {

    @Override
    public void doIt() {
        System.out.println("ASubAbstraction, implementation 2: I am doing it!");
    }
}
