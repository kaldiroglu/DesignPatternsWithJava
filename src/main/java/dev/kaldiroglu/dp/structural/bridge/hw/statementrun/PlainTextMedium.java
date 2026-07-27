package dev.kaldiroglu.dp.structural.bridge.hw.statementrun;

/** A ConcreteImplementor: fixed-width text, for the body of an email. */
public final class PlainTextMedium implements Medium {

    private final StringBuilder out = new StringBuilder();

    @Override
    public void heading(int level, String text) {
        String underline = level == 1 ? "=" : "-";
        out.append(text).append('\n').append(underline.repeat(text.length())).append("\n");
    }

    @Override
    public void field(String label, String value) {
        out.append(String.format("%-18s %s%n", label + ":", value));
    }

    @Override
    public void row(String... cells) {
        out.append("  ").append(String.join("   ", cells)).append('\n');
    }

    @Override
    public void total(String label, String amount) {
        out.append(String.format("%-18s %s%n", label.toUpperCase() + ":", amount));
    }

    @Override
    public String output() {
        return out.toString();
    }
}
