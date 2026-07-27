package dev.kaldiroglu.dp.structural.facade.gof;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Subsystem class (GoF, p. 185): generates code for a <b>RISC</b> (register)
 * machine. Values are held in registers rather than on a stack; each result is
 * placed in a freshly allocated register.
 *
 * <p>This is the generator GoF's {@code Compiler::Compile} actually uses. Having
 * two interchangeable generators is exactly the kind of subsystem flexibility the
 * {@link Compiler} facade keeps out of the client's way.</p>
 */
public class RiscCodeGenerator extends CodeGenerator {

    private final Deque<String> registers = new ArrayDeque<>();
    private int nextRegister;

    public RiscCodeGenerator(BytecodeStream output) {
        super(output);
    }

    private String allocate() {
        String register = "R" + nextRegister++;
        registers.push(register);
        return register;
    }

    @Override
    public void emitPushConstant(int value) {
        output.put("LOADI " + allocate() + ", " + value);
    }

    @Override
    public void emitLoad(String variable) {
        output.put("LOAD " + allocate() + ", " + variable);
    }

    @Override
    public void emitStore(String variable) {
        output.put("STORE " + variable + ", " + registers.pop());
    }

    @Override
    public void emitAdd() {
        emitBinary("ADD");
    }

    @Override
    public void emitSubtract() {
        emitBinary("SUB");
    }

    private void emitBinary(String mnemonic) {
        String right = registers.pop();
        String left = registers.pop();
        String result = allocate();
        output.put(mnemonic + " " + result + ", " + left + ", " + right);
    }

    @Override
    public void emitReturn() {
        output.put("RET " + registers.pop());
    }
}
