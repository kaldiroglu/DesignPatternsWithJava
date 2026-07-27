package dev.kaldiroglu.dp.structural.bridge.hw.statementrun;

/**
 * A ConcreteImplementor: what a screen reader will say.
 * <p>
 * This is the medium that decides the shape of {@link Medium}. It has no page, no column and
 * no font, so any primitive that mentioned one would have to be faked here — and a faked
 * primitive is the beginning of the end of a Bridge. Because the interface asks only about
 * meaning, this class is as short as the other two.
 */
public final class SpokenMedium implements Medium {

    private final StringBuilder out = new StringBuilder();

    @Override
    public void heading(int level, String text) {
        out.append(level == 1 ? "Document: " : "Section: ").append(text).append(". ");
    }

    @Override
    public void field(String label, String value) {
        out.append(label).append(", ").append(value).append(". ");
    }

    @Override
    public void row(String... cells) {
        out.append("Line: ").append(String.join(", ", cells)).append(". ");
    }

    @Override
    public void total(String label, String amount) {
        out.append(label).append(" of ").append(amount).append(". ");
    }

    @Override
    public String output() {
        return out.toString().trim();
    }
}
