package dev.kaldiroglu.dp.structural.bridge.violation;

import java.util.List;

/**
 * Runs the violation, so it can be seen rather than described.
 * <p>
 * The loop below is the whole point: it holds {@link AType}, which promises that {@code doIt}
 * prints. One of the two objects says nothing, and nothing anywhere reports a fault.
 */
public class Main {

    public static void main(String[] args) {
        List<AType> everything = List.of(
                new AType(42, true),
                new ASubType(42, true));

        System.out.println("A caller holding AType, expecting each doIt() to print:");
        for (AType each : everything) {
            System.out.print("  " + each.getClass().getSimpleName() + " -> ");
            each.doIt();
            System.out.println();
        }

        System.out.println("""
                One line of output, two objects. The second printed nothing, threw
                nothing, and logged nothing. A caller cannot tell, and cannot defend
                itself except by testing the type — which is the thing polymorphism
                was supposed to remove.

                And before doIt() is ever called:""");

        ASubType early = new ASubType(42, true);
        early.writeIt();

        System.out.println("""
                null, because the string is only assigned by the override.

                The fix is not a better override. It is to stop using inheritance to
                supply an implementation: see bridge.basic.pattern, where the
                refinement delegates to an implementor and keeps its own contract
                whichever implementation it is given.""");
    }
}
