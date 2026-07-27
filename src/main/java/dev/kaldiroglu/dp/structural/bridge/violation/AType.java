package dev.kaldiroglu.dp.structural.bridge.violation;

/**
 * A supertype with a published contract: <em>calling {@code doIt} prints something.</em>
 * <p>
 * This package is not a Bridge. It is the argument <em>for</em> one — the thing that goes
 * wrong when an implementation is supplied by subclassing instead of by delegation. Read it
 * before {@code bridge.basic.problem} or straight after it.
 */
public class AType {

    protected int anIntVariable;
    protected boolean aBoolVariable;

    public AType(int anIntVariable, boolean aBoolVariable) {
        this.anIntVariable = anIntVariable;
        this.aBoolVariable = aBoolVariable;
    }

    public int getAnIntVariable() {
        return anIntVariable;
    }

    public void setAnIntVariable(int anIntVariable) {
        this.anIntVariable = anIntVariable;
    }

    public boolean isABoolVariable() {
        return aBoolVariable;
    }

    public void setABoolVariable(boolean aBoolVariable) {
        this.aBoolVariable = aBoolVariable;
    }

    /** Prints. That is the contract every caller of this type is entitled to rely on. */
    public void doIt() {
        if (aBoolVariable) {
            System.out.println("My variable: " + anIntVariable);
        } else {
            System.out.println("Nothing happened!");
        }
    }
}
