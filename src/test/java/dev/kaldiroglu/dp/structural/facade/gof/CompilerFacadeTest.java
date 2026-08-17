package dev.kaldiroglu.dp.structural.facade.gof;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * GoF's own example. The compiler subsystem has a scanner, a parser, a node builder and a
 * family of code generators; most clients want none of that and one method. These tests
 * measure both halves of the solution's promise — the simple path, and the fact that it does
 * not block the direct one.
 */
class CompilerFacadeTest {

    /** The grammar is assignment, addition, subtraction and return — nothing else. */
    private static final String SOURCE = """
            x = 3
            y = 4
            z = x + y - 1
            return z
            """;

    @Test
    @DisplayName("one call replaces the whole subsystem")
    void oneCall() {
        BytecodeStream output = new Compiler().compile(SOURCE);

        assertNotNull(output);
        assertTrue(!output.instructions().isEmpty(), "something was generated");
    }

    @Test
    @DisplayName("the facade's own interface is small — that is the whole idea")
    void theFacadeIsSmall() {
        long publicMethods = Arrays.stream(Compiler.class.getDeclaredMethods())
                .filter(m -> java.lang.reflect.Modifier.isPublic(m.getModifiers()))
                .count();

        assertEquals(2, publicMethods, "compile(source), and compile(source, generator)");
    }

    @Test
    @DisplayName("a client that needs more can still reach the subsystem directly")
    void theSubsystemStaysReachable() {
        // GoF, p. 185: a facade "doesn't prevent applications from using subsystem classes
        // if they need to". This is the difference between a facade and a wall.
        BytecodeStream output = new BytecodeStream();
        CodeGenerator risc = new RiscCodeGenerator(output);

        Scanner scanner = new Scanner(SOURCE);
        ProgramNodeBuilder builder = new ProgramNodeBuilder();
        new Parser().parse(scanner, builder);
        builder.getRootNode().traverse(risc);

        assertTrue(!output.instructions().isEmpty());
    }

    @Test
    @DisplayName("and the facade itself offers the same choice, one level up")
    void theFacadeExposesTheChoiceItself() {
        BytecodeStream stack = new BytecodeStream();
        BytecodeStream risc = new BytecodeStream();

        new Compiler().compile(SOURCE, new StackMachineCodeGenerator(stack));
        new Compiler().compile(SOURCE, new RiscCodeGenerator(risc));

        // Same source, two back ends — the facade widened rather than blocked.
        assertTrue(!stack.instructions().isEmpty());
        assertTrue(!risc.instructions().isEmpty());
    }

    @Test
    @DisplayName("the subsystem classes know nothing about the facade")
    void theSubsystemDoesNotDependOnTheFacade() {
        // A facade depends on its subsystem, never the other way round. If it were mutual,
        // the subsystem could not be used without it.
        for (Class<?> type : new Class<?>[]{Scanner.class, Parser.class,
                ProgramNodeBuilder.class, RiscCodeGenerator.class}) {
            for (Method m : type.getDeclaredMethods()) {
                assertTrue(m.getReturnType() != Compiler.class,
                        type.getSimpleName() + " refers back to the facade");
                assertTrue(Arrays.stream(m.getParameterTypes()).noneMatch(p -> p == Compiler.class),
                        type.getSimpleName() + " takes the facade as a parameter");
            }
        }
    }
}
