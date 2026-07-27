package dev.kaldiroglu.dp.structural.bridge.hw.statementrun;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * The screen reader is the assertion that matters. If a primitive on Medium had been about
 * paper, SpokenMedium would have had to fake it, and the fake is where a Bridge starts to rot.
 */
class StatementRunTest {

    private static final List<String[]> LINES = List.<String[]>of(
            new String[]{"Consultancy, March", "8 days", "24,000.00"});

    private static Document invoice(Medium medium) {
        return new Invoice(medium, "4417", "Bora Yilmaz", LINES, "25,450.00");
    }

    @Test
    @DisplayName("one invoice class renders onto three media")
    void oneAbstractionThreeImplementors() {
        assertTrue(invoice(new HtmlMedium()).render().contains("<h1>Invoice 4417</h1>"));
        assertTrue(invoice(new PlainTextMedium()).render().contains("Invoice 4417"));
        assertTrue(invoice(new SpokenMedium()).render().contains("Document: Invoice 4417."));
    }

    @Test
    @DisplayName("the spoken rendering carries no markup and no layout")
    void theVoiceHasNoPage() {
        String spoken = invoice(new SpokenMedium()).render();

        assertFalse(spoken.contains("<"));
        assertFalse(spoken.contains("="));   // no underlines
        assertFalse(spoken.contains("\n"));  // no lines: a voice has none
        assertTrue(spoken.contains("Amount due of 25,450.00."));
    }

    @Test
    @DisplayName("every medium can answer every primitive — none of them is about paper")
    void noPrimitiveIsAboutInk() {
        List<String> primitives = List.of("heading", "field", "row", "total", "output");
        assertEquals(primitives.size(), Medium.class.getDeclaredMethods().length);

        for (Method m : Medium.class.getDeclaredMethods()) {
            assertTrue(primitives.contains(m.getName()),
                    m.getName() + " sounds like a question about a page, not about meaning");
        }
    }

    @Test
    @DisplayName("three documents and three media are six classes, not nine")
    void mPlusNNotMTimesN() {
        List<Class<?>> documents = List.of(Invoice.class, AccountStatement.class, DunningLetter.class);
        List<Class<?>> media = List.of(HtmlMedium.class, PlainTextMedium.class, SpokenMedium.class);

        assertEquals(6, documents.size() + media.size());
        assertEquals(9, documents.size() * media.size());
        assertTrue(documents.stream().allMatch(Document.class::isAssignableFrom));
        assertTrue(media.stream().allMatch(Medium.class::isAssignableFrom));
    }

    @Test
    @DisplayName("a document never learns which medium it has")
    void theAbstractionHoldsOnlyTheInterface() {
        assertEquals(Medium.class,
                java.util.Arrays.stream(Document.class.getDeclaredFields())
                        .filter(f -> f.getName().equals("medium"))
                        .findFirst().orElseThrow().getType());
    }
}
