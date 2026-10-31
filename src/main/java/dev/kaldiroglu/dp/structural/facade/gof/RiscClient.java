package dev.kaldiroglu.dp.structural.facade.gof;

/**
 * A client that needs more than the facade's default. It still calls {@link Compiler}, and
 * passes in a subsystem class of its own choice.
 */
public final class RiscClient {

    public static void main(String[] args) {
        Compiler compiler = new Compiler();

        System.out.println("== Same source, RISC target (subsystem still reachable directly) ==");
        BytecodeStream risc = new BytecodeStream();
        compiler.compile(Client.SOURCE, new RiscCodeGenerator(risc));
        System.out.println(risc);
    }
}
