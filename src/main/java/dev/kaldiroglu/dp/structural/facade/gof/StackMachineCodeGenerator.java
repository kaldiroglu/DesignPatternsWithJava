package dev.kaldiroglu.dp.structural.facade.gof;

/**
 * Subsystem class (GoF, p. 185): generates code for a simple <b>stack machine</b>.
 * Operands live on an evaluation stack; instructions push, load, store, and
 * combine the top-of-stack values.
 */
public class StackMachineCodeGenerator extends CodeGenerator {

    public StackMachineCodeGenerator(BytecodeStream output) {
        super(output);
    }

    @Override
    public void emitPushConstant(int value) {
        output.put("PUSH " + value);
    }

    @Override
    public void emitLoad(String variable) {
        output.put("LOAD " + variable);
    }

    @Override
    public void emitStore(String variable) {
        output.put("STORE " + variable);
    }

    @Override
    public void emitAdd() {
        output.put("ADD");
    }

    @Override
    public void emitSubtract() {
        output.put("SUB");
    }

    @Override
    public void emitReturn() {
        output.put("RETURN");
    }
}
