package dev.kaldiroglu.dp.structural.composite.hw.orgchart;

/**
 * The Component: anyone on the org chart, whether or not they have reports.
 * <p>
 * Both operations are roll-ups. A client asks one person and gets an answer for the whole
 * organization beneath them — which is the only reason to build a Composite at all.
 */
public interface Employee {

    String getName();

    /** Salary cost of this person and everyone below them, in whole liras. */
    long totalCost();

    /** This person and everyone below them. */
    int headcount();

    String render(String indent);
}
