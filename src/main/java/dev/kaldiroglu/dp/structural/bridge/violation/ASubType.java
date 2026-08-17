package dev.kaldiroglu.dp.structural.bridge.violation;

/**
 * The violation.
 * <p>
 * This subclass overrides {@link AType#doIt()} to <strong>store</strong> a string instead of
 * printing one, and adds {@link #writeIt()} to print it later. Nothing here fails to compile,
 * and every test written against {@code ASubType} passes.
 * <p>
 * The damage is done to callers that hold an {@link AType}. They were promised output; given
 * one of these, they get silence, and no exception tells them so. That is a breach of the
 * Liskov Substitution Principle: a subtype must be usable wherever its supertype is, and this
 * one is not.
 * <p>
 * <strong>Why it belongs in a Bridge package.</strong> Subclassing was used here to change
 * <em>how</em> something is done, which is exactly what an implementation is for — and
 * changing behavior by overriding can break a contract the supertype made, silently.
 * Delegating to an implementor cannot: {@code ASubAbstraction} in
 * {@code bridge.basic.solution} calls {@code implementation.doingIt()} and remains responsible
 * for its own contract no matter which implementation it holds. That difference is the whole
 * argument for putting the implementation behind a reference rather than above it in a
 * hierarchy.
 * <p>
 * Note also {@code aStringVariable}: it is deliberately left uninitialized, so a caller that
 * reaches {@code writeIt()} without having called {@code doIt()} first prints {@code null}.
 * A second broken promise, produced by the same move.
 */
public class ASubType extends AType {

    private String aStringVariable;

    public ASubType(int anIntVariable, boolean aBoolVariable) {
        super(anIntVariable, aBoolVariable);
    }

    /** Stores instead of printing — and the supertype said this method prints. */
    @Override
    public void doIt() {
        if (aBoolVariable) {
            aStringVariable = "My variable: " + anIntVariable;
        } else {
            aStringVariable = "Nothing happened!";
        }
    }

    public void writeIt() {
        System.out.println("aStringVariable : " + aStringVariable);
    }

    /** Exposed so a test can show the string was stored rather than printed. */
    public String getAStringVariable() {
        return aStringVariable;
    }
}
