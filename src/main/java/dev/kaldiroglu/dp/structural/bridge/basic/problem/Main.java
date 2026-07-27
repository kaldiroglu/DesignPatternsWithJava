package dev.kaldiroglu.dp.structural.bridge.basic.problem;

import java.util.List;

/** The grid, and the number underneath it. */
public class Main {

    public static void main(String[] args) {
        List<AnAbstraction> everyCombination = List.of(
                new AConcreteImplementation1(), new AConcreteImplementation2(),
                new AnotherConcreteImplementation1(), new AnotherConcreteImplementation2());

        everyCombination.forEach(a -> new Client(a).start());

        System.out.println();
        System.out.println("2 refinements x 2 implementations = 4 leaf classes,");
        System.out.println("plus the 2 refinements themselves = 6.");
        System.out.println("A third implementation costs 2 more. basic.pattern would cost 1.");
    }
}
