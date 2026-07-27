package dev.kaldiroglu.dp.structural.facade.gof;

/**
 * Subsystem class (GoF, p. 185): abstract base for code generators. GoF lists
 * {@code StackMachineCodeGenerator} and {@code RISCCodeGenerator} as subclasses;
 * both are provided here ({@link StackMachineCodeGenerator},
 * {@link RiscCodeGenerator}).
 *
 * <p>Nodes call these {@code emit*} operations while being traversed; each
 * concrete generator writes instructions for a different target machine into the
 * shared {@link BytecodeStream}. The {@link Compiler} facade chooses a generator,
 * so clients never have to.</p>
 */
public abstract class CodeGenerator {

    protected final BytecodeStream output;

    protected CodeGenerator(BytecodeStream output) {
        this.output = output;
    }

    public abstract void emitPushConstant(int value);

    public abstract void emitLoad(String variable);

    public abstract void emitStore(String variable);

    public abstract void emitAdd();

    public abstract void emitSubtract();

    public abstract void emitReturn();
}
