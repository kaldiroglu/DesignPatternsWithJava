package dev.kaldiroglu.dp.behavioral.visitor.file;

import dev.kaldiroglu.dp.behavioral.visitor.Printed;
import dev.kaldiroglu.dp.behavioral.visitor.file.pattern1.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static dev.kaldiroglu.dp.behavioral.visitor.Printed.codeOf;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Text files and XML files: type tests in FileOperator, then a visitor. The checks are
 * random, so these tests use a visitor that always answers the same way.
 */
class FileTest {

    private static final String SOURCE =
            "src/main/java/dev/kaldiroglu/dp/behavioral/visitor/file/";

    /** A visitor with a fixed answer for each kind of file. */
    private record Fixed(boolean text, boolean xml) implements Visitor {
        @Override
        public boolean visit(TextFile file) {
            return text;
        }

        @Override
        public boolean visit(XMLFile file) {
            return xml;
        }
    }

    @Test
    @DisplayName("each file calls the visit method for its own class and returns its answer")
    void acceptReturnsTheVisitorsAnswer() {
        File text = new TextFile("a.txt");
        File xml = new XMLFile("b.xml");

        assertTrue(text.accept(new Fixed(true, false)));
        assertFalse(xml.accept(new Fixed(true, false)));
        assertFalse(text.accept(new Fixed(false, true)));
        assertTrue(xml.accept(new Fixed(false, true)));
    }

    @Test
    @DisplayName("a file is read only when the visitor says it may be")
    void readOnlyWhenAllowed() {
        File text = new TextFile("a.txt");
        Visitor refuses = new Fixed(false, false);

        List<String> lines = Printed.by(() -> {
            text.open();
            if (text.accept(refuses)) {
                text.read();
            }
            text.close();
        });

        assertEquals(List.of("", "Opening the file: a.txt", "Closing the file: a.txt"), lines);
    }

    @Test
    @DisplayName("with the visitor, checkFormat and validate are no longer methods of the file classes")
    void theChecksMovedToTheVisitor() {
        for (Class<?> file : List.of(TextFile.class, XMLFile.class)) {
            List<String> names = Arrays.stream(file.getDeclaredMethods()).map(Method::getName).toList();
            assertFalse(names.contains("checkFormat"), file.getSimpleName());
            assertFalse(names.contains("validate"), file.getSimpleName());
        }
        assertEquals(2, Visitor.class.getDeclaredMethods().length);
        assertEquals(2, FileVisitor.class.getDeclaredMethods().length);
    }

    @Test
    @DisplayName("FileOperator tests the type and casts, and the visitor does not")
    void typeTestsAndCasts() {
        String operator = codeOf(SOURCE + "problem2/FileOperator.java");
        assertTrue(operator.contains("instanceof XMLFile"));
        assertTrue(operator.contains("(TextFile) aFile"));

        String visitor = codeOf(SOURCE + "pattern1/FileVisitor.java");
        assertFalse(visitor.contains("instanceof"));
    }

    @Test
    @DisplayName("the checks pass four times in five: both visit methods compare a random number with 0.80")
    void fourTimesInFive() {
        String visitor = codeOf(SOURCE + "pattern1/FileVisitor.java");
        assertEquals(2, visitor.split("random < 0\\.80", -1).length - 1);
    }
}
