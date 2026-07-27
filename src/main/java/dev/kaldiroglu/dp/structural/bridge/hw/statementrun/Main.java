package dev.kaldiroglu.dp.structural.bridge.hw.statementrun;

import java.util.List;

/**
 * Homework 1 — the statement run.
 * <p>
 * One {@link Invoice} class, written once, rendered onto three media. The screen reader is
 * the one that mattered: it is what forced the Implementor to describe meaning rather than
 * ink.
 */
public class Main {

    private static final List<String[]> LINES = List.of(
            new String[]{"Consultancy, March", "8 days", "24,000.00"},
            new String[]{"Travel", "1 trip", "1,450.00"});

    public static void main(String[] args) {
        System.out.println("--- HTML ---");
        System.out.println(invoice(new HtmlMedium()).render());

        System.out.println("--- plain text ---");
        System.out.println(invoice(new PlainTextMedium()).render());

        System.out.println("--- spoken ---");
        System.out.println(invoice(new SpokenMedium()).render());

        System.out.println("\n--- a different document, the same three media ---");
        System.out.println(new DunningLetter(new SpokenMedium(),
                "Bora Yilmaz", "4417", "25,450.00", 34).render());

        System.out.println("\n3 documents x 3 media = 6 classes, not 9.");
        System.out.println("A fourth medium is one class, and no document is touched.");
    }

    private static Document invoice(Medium medium) {
        return new Invoice(medium, "4417", "Bora Yilmaz", LINES, "25,450.00");
    }
}
