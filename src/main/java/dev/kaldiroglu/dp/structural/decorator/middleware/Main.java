package dev.kaldiroglu.dp.structural.decorator.middleware;

import dev.kaldiroglu.dp.structural.decorator.middleware.problem.CopyPasteMain;
import dev.kaldiroglu.dp.structural.decorator.middleware.problem.FlagsMain;
import dev.kaldiroglu.dp.structural.decorator.middleware.problem.SubclassChainMain;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.ClassicChainMain;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.OrderingMain;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.RateLimitPlacementMain;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.VendorFeedMain;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.fluent.FluentMain;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.functional.FunctionalMain;

/**
 * Runs every design in this project against the same scenario, printing the number that
 * matters each time: how many times the supplier was actually called.
 * <p>
 * Each example also has its own {@code Main} beside the code it demonstrates, so a single
 * one can be run on its own without editing this file. This class is the composition root
 * that runs them in the order the deck presents them.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Console.heading("THE PROBLEM");
        CopyPasteMain.main(args);
        FlagsMain.main(args);
        SubclassChainMain.main(args);

        Console.heading("THE SOLUTION — classic decorators");
        ClassicChainMain.main(args);
        OrderingMain.main(args);
        RateLimitPlacementMain.main(args);
        VendorFeedMain.main(args);

        Console.heading("THE SOLUTION — variation: functional");
        FunctionalMain.main(args);

        Console.heading("THE SOLUTION — variation: fluent assembly");
        FluentMain.main(args);
    }
}
