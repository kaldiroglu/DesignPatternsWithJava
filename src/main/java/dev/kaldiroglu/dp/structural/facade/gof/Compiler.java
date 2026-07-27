package dev.kaldiroglu.dp.structural.facade.gof;

/**
 * <b>Facade</b> participant (GoF, p. 185).
 *
 * <p>The compiler subsystem is made of many classes — {@link Scanner},
 * {@link Parser}, {@link ProgramNodeBuilder}, the {@link ProgramNode} tree,
 * {@link CodeGenerator}s, and {@link BytecodeStream}. Most clients just want to
 * turn source into bytecode and do not care about those parts. {@code Compiler}
 * offers that single, simple entry point and orchestrates the subsystem behind
 * it.</p>
 *
 * <p>The facade does not hide the subsystem: a client that needs finer control
 * (say, a different {@link CodeGenerator}) can still use the subsystem classes
 * directly.</p>
 */
public class Compiler {

    /** Compiles source text into bytecode using the default (stack machine) target. */
    public BytecodeStream compile(String sourceCode) {
        BytecodeStream output = new BytecodeStream();
        compile(sourceCode, new StackMachineCodeGenerator(output));
        return output;
    }

    /**
     * Compiles source text, emitting into the given generator's stream. Lets a
     * client pick the target machine (e.g. {@link RiscCodeGenerator}) while still
     * leaving the scanning/parsing wiring to the facade.
     */
    public void compile(String sourceCode, CodeGenerator generator) {
        Scanner scanner = new Scanner(sourceCode);
        ProgramNodeBuilder builder = new ProgramNodeBuilder();

        Parser parser = new Parser();
        parser.parse(scanner, builder);

        ProgramNode parseTree = builder.getRootNode();
        parseTree.traverse(generator);
    }
}
