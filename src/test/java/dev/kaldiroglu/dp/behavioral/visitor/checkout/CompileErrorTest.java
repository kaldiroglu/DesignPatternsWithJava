package dev.kaldiroglu.dp.behavioral.visitor.checkout;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The compile errors the deck quotes. Each test compiles a small class against the example
 * classes and checks the compiler's message, word for word.
 */
class CompileErrorTest {

    /** Compiles one source file and returns the error messages, in English. */
    private static List<String> errorsOf(String className, String source) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        JavaFileObject file = new SimpleJavaFileObject(
                URI.create("string:///" + className + ".java"), JavaFileObject.Kind.SOURCE) {
            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                return source;
            }
        };
        Path out = Files.createTempDirectory("compile-error-test");
        List<String> options = List.of("-classpath", System.getProperty("java.class.path"),
                "-d", out.toString());
        compiler.getTask(null, null, diagnostics, options, null, List.of(file)).call();
        return diagnostics.getDiagnostics().stream()
                .filter(d -> d.getKind() == Diagnostic.Kind.ERROR)
                .map(d -> d.getMessage(Locale.ENGLISH))
                // The javac command prints simple class names; the compiler API prints
                // full names. Drop the package names to get the text the command prints.
                .map(message -> message.replaceAll("\\b(?:[a-z][a-zA-Z0-9]*\\.)+(?=[A-Z])", ""))
                .toList();
    }

    @Test
    @DisplayName("stage two: without taxOf(Item), the loop over a List<Item> does not compile")
    void stageTwoWithoutTaxOfItem() throws Exception {
        String source = """
                import dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.*;
                import java.util.List;
                class TaxWithoutItem {
                    int taxOf(Book book) { return 0; }
                    int taxOf(Food food) { return 0; }
                    int taxOf(Electronics electronics) { return 0; }
                    int total(List<Item> cart) {
                        int total = 0;
                        for (Item item : cart) {
                            total += taxOf(item);
                        }
                        return total;
                    }
                }
                """;
        List<String> errors = errorsOf("TaxWithoutItem", source);
        assertEquals(1, errors.size(), errors.toString());
        assertTrue(errors.get(0).startsWith("no suitable method found for taxOf(Item)"), errors.get(0));
    }

    @Test
    @DisplayName("a visitor without visit(GiftCard) does not compile, with the message on the slide")
    void aVisitorWithoutVisitGiftCard() throws Exception {
        String source = """
                import dev.kaldiroglu.dp.behavioral.visitor.checkout.solution.*;
                class OldTax implements ItemVisitor<Integer> {
                    public Integer visit(Book book) { return 0; }
                    public Integer visit(Food food) { return 0; }
                    public Integer visit(Electronics electronics) { return 0; }
                }
                """;
        assertEquals(List.of("OldTax is not abstract and does not override abstract method "
                + "visit(GiftCard) in ItemVisitor"), errorsOf("OldTax", source));
    }

    @Test
    @DisplayName("a switch over the sealed Item without a GiftCard case does not compile")
    void aSwitchWithoutGiftCard() throws Exception {
        String source = """
                import dev.kaldiroglu.dp.behavioral.visitor.checkout.modern.*;
                class OldSwitch {
                    static int of(Item item) {
                        return switch (item) {
                            case Book book -> 2;
                            case Food food -> 1;
                            case Electronics electronics -> 100;
                        };
                    }
                }
                """;
        assertEquals(List.of("the switch expression does not cover all possible input values"),
                errorsOf("OldSwitch", source));
    }

    @Test
    @DisplayName("stage one: a new item class without tax() does not compile")
    void stageOneNeedsEveryMethod() throws Exception {
        String source = """
                import dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.methods.Item;
                record GiftCard(String name, int price) implements Item {
                    public int shippingCost() { return 0; }
                    public String receiptLine() { return name; }
                }
                """;
        List<String> errors = errorsOf("GiftCard", source);
        assertEquals(1, errors.size());
        assertTrue(errors.getFirst().contains("does not override abstract method tax()"), errors.getFirst());
    }
}
