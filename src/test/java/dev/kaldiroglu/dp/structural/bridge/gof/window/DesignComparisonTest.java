package dev.kaldiroglu.dp.structural.bridge.gof.window;

import dev.kaldiroglu.dp.structural.bridge.gof.problem.PMIconWindow;
import dev.kaldiroglu.dp.structural.bridge.gof.problem.XIconWindow;
import dev.kaldiroglu.dp.structural.bridge.gof.solution.IconWindow;
import dev.kaldiroglu.dp.structural.bridge.gof.solution.PMWindowImp;
import dev.kaldiroglu.dp.structural.bridge.gof.solution.Window;
import dev.kaldiroglu.dp.structural.bridge.gof.solution.XWindowImp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * The two designs are worth comparing only if they do the same thing. These tests prove
 * they draw identical windows, so every remaining difference is a difference of design.
 */
class DesignComparisonTest {

    @Test
    @DisplayName("both designs draw the same icon window on X")
    void sameOutputOnX() {
        assertEquals(
                dev.kaldiroglu.dp.structural.bridge.gof.problem.Window.render(new XIconWindow(14, 5, "a.txt")),
                Window.render(new IconWindow(14, 5, "a.txt", new XWindowImp())));
    }

    @Test
    @DisplayName("both designs draw the same icon window on Presentation Manager")
    void sameOutputOnPm() {
        assertEquals(
                dev.kaldiroglu.dp.structural.bridge.gof.problem.Window.render(new PMIconWindow(14, 5, "a.txt")),
                Window.render(new IconWindow(14, 5, "a.txt", new PMWindowImp())));
    }

    /**
     * The class counts the slides quote for GoF's own example, counted from the two packages
     * rather than asserted as arithmetic on literals.
     * <p>
     * The version this replaced said {@code assertEquals(5, kinds + platforms)} while its own
     * comment described six classes — Window, two refinements, WindowImp and two
     * implementations. The comment was right and the assertion was counting leaves only, and
     * neither could fail. Both diagrams in the deck draw every type, so the numbers on the
     * slides have to be the numbers a reader can count in the picture.
     */
    @Test
    @DisplayName("nine types become six, and a third platform costs three against one")
    void theCostOfGrowth() throws Exception {
        List<Class<?>> naive = typesIn(XIconWindow.class.getPackageName());
        List<Class<?>> bridged = typesIn(Window.class.getPackageName());

        assertEquals(9, runnable(naive), "every type on the problem diagram");
        assertEquals(6, runnable(bridged), "every type on the solution diagram");

        // Of the nine, six are leaves — one per (kind, platform) pair — and three are the
        // abstract kinds above them. That product is what grows.
        long leaves = naive.stream()
                .filter(t -> !Modifier.isAbstract(t.getModifiers()))
                .filter(t -> !t.getSimpleName().equals("Main"))
                .count();
        assertEquals(6, leaves, "leaf classes, one per pair");

        // Two platforms and six leaves means three kinds, so the leaf count really is the
        // product and a third platform adds one leaf per kind. Everything below is derived
        // from the counts above rather than restated as literals.
        long kinds = runnable(naive) - leaves;
        assertEquals(3, kinds, "abstract kinds above the leaves");
        assertEquals(leaves, kinds * 2, "leaves are kinds x platforms");
        assertEquals(9, kinds * 3, "leaves after a third platform");

        // The bridged side grows by one class instead of three. That is not arithmetic here:
        // WindowSolutionTest writes an implementor inside the test method and draws every
        // window kind through it without touching any of them.
        assertEquals(runnable(bridged) + 1, 7, "types after a third platform");
    }

    /** Top-level types in a package, excluding the runnable demo. */
    private static long runnable(List<Class<?>> types) {
        return types.stream().filter(t -> !t.getSimpleName().equals("Main")).count();
    }

    private static List<Class<?>> typesIn(String packageName) throws Exception {
        List<URL> roots = Collections.list(DesignComparisonTest.class.getClassLoader()
                .getResources(packageName.replace('.', '/')));
        assertFalse(roots.isEmpty(), "package not on the test classpath: " + packageName);
        List<Class<?>> types = new ArrayList<>();
        for (URL root : roots) {
            try (Stream<Path> files = Files.list(Path.of(root.toURI()))) {
                for (Path file : files.sorted().toList()) {
                    String name = file.getFileName().toString();
                    if (name.endsWith(".class") && !name.contains("$")) {
                        Class<?> type = Class.forName(
                                packageName + '.' + name.substring(0, name.length() - 6));
                        if (!types.contains(type)) {
                            types.add(type);
                        }
                    }
                }
            }
        }
        return types;
    }
}
