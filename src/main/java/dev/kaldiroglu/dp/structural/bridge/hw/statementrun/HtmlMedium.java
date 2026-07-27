package dev.kaldiroglu.dp.structural.bridge.hw.statementrun;

/** A ConcreteImplementor: HTML. */
public final class HtmlMedium implements Medium {

    private final StringBuilder out = new StringBuilder();

    @Override
    public void heading(int level, String text) {
        out.append("<h").append(level).append('>').append(text)
           .append("</h").append(level).append(">\n");
    }

    @Override
    public void field(String label, String value) {
        out.append("<p><b>").append(label).append(":</b> ").append(value).append("</p>\n");
    }

    @Override
    public void row(String... cells) {
        out.append("<tr>");
        for (String cell : cells) {
            out.append("<td>").append(cell).append("</td>");
        }
        out.append("</tr>\n");
    }

    @Override
    public void total(String label, String amount) {
        out.append("<p class=\"total\">").append(label).append(": ").append(amount).append("</p>\n");
    }

    @Override
    public String output() {
        return out.toString();
    }
}
