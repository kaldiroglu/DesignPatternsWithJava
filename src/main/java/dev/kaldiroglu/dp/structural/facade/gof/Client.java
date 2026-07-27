package dev.kaldiroglu.dp.structural.facade.gof;

/**
 * Client (GoF, p. 185). It wants to compile source code and uses <em>only</em>
 * the {@link Compiler} facade — it never mentions {@code Scanner},
 * {@code Parser}, {@code CodeGenerator}, or any other subsystem class.
 */
public final class Client {


    public static void main(String[] args) {
        String source = """
                x = 3
                y = 4
                z = x + y - 1
                return z
                """;

        System.out.println("== Source ==");
        System.out.println(source);

        Compiler compiler = new Compiler();

        System.out.println("== Bytecode (stack machine — the facade's default) ==");
        System.out.println(compiler.compile(source));

        System.out.println("\n== Same source, RISC target (subsystem still reachable directly) ==");
        BytecodeStream risc = new BytecodeStream();
        compiler.compile(source, new RiscCodeGenerator(risc));
        System.out.println(risc);
    }
}
