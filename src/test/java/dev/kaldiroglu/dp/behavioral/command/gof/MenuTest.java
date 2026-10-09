package dev.kaldiroglu.dp.behavioral.command.gof;

import dev.kaldiroglu.dp.behavioral.command.gof.solution.Command;
import dev.kaldiroglu.dp.behavioral.command.gof.solution.MacroCommand;
import dev.kaldiroglu.dp.behavioral.command.gof.solution.Menu;
import dev.kaldiroglu.dp.behavioral.command.gof.solution.MenuItem;
import dev.kaldiroglu.dp.behavioral.command.gof.solution.OpenCommand;
import dev.kaldiroglu.dp.behavioral.command.gof.solution.PasteCommand;
import dev.kaldiroglu.dp.behavioral.command.gof.solution.SimpleCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GoF's menu example, before and after the pattern. Every claim on the Part 2 slides about
 * the menu is checked here.
 */
class MenuTest {

    private static final String SOURCE = "src/main/java/dev/kaldiroglu/dp/behavioral/command/gof/";

    /** The source of a file with its comments removed, so comments cannot match a search. */
    static String codeOf(String path) throws Exception {
        String text = Files.readString(Path.of(path));
        text = text.replaceAll("(?s)/\\*.*?\\*/", "");
        return text.replaceAll("//[^\\n]*", "");
    }

    /** The import lines of a source file, comments removed. */
    static List<String> importsOf(String path) throws Exception {
        return codeOf(path).lines()
                .map(String::strip)
                .filter(line -> line.startsWith("import "))
                .toList();
    }

    // ------------------------------------------------------------ without the pattern

    @Test
    @DisplayName("before the pattern: the toolkit's menu item imports the application's classes")
    void theProblemMenuItemImportsTheApplication() throws Exception {
        List<String> imports = importsOf(SOURCE + "problem/MenuItem.java");

        assertTrue(imports.contains("import dev.kaldiroglu.dp.behavioral.command.gof.Application;"));
        assertTrue(imports.contains("import dev.kaldiroglu.dp.behavioral.command.gof.Document;"));
    }

    @Test
    @DisplayName("before the pattern: the menu item branches on its label for Open, Copy and Paste")
    void theProblemMenuItemWorksForItsThreeLabels() {
        Application application = new Application();
        new dev.kaldiroglu.dp.behavioral.command.gof.problem.MenuItem("Open", application, () -> "letter")
                .clicked();
        Document letter = application.current();
        letter.type("Dear Deniz");

        new dev.kaldiroglu.dp.behavioral.command.gof.problem.MenuItem("Copy", application, () -> "")
                .clicked();
        new dev.kaldiroglu.dp.behavioral.command.gof.problem.MenuItem("Paste", application, () -> "")
                .clicked();

        assertEquals("letter", letter.name());
        assertTrue(letter.isOpen());
        assertEquals("Dear DenizDear Deniz", letter.text());
    }

    @Test
    @DisplayName("before the pattern: a menu entry spelled paste compiles and fails on the first click")
    void aMisspelledLabelFailsOnTheFirstClick() {
        Application application = new Application();
        application.add(new Document("letter", application.clipboard()));
        var paste = new dev.kaldiroglu.dp.behavioral.command.gof.problem.MenuItem(
                "paste", application, () -> "");

        assertThrows(IllegalStateException.class, paste::clicked);
    }

    // ---------------------------------------------------------------- with the pattern

    @Test
    @DisplayName("with the pattern: the menu item imports nothing from the application")
    void theSolutionMenuItemImportsNothingFromTheApplication() throws Exception {
        for (String file : List.of("solution/MenuItem.java", "solution/Menu.java", "solution/Command.java")) {
            List<String> imports = importsOf(SOURCE + file);
            assertTrue(imports.stream().noneMatch(line -> line.contains("command.gof.Application")
                            || line.contains("command.gof.Document")
                            || line.contains("command.gof.Clipboard")),
                    file + " must not import a receiver: " + imports);
        }
    }

    @Test
    @DisplayName("with the pattern: the menu item has no switch on its label")
    void theSolutionMenuItemHasNoSwitch() throws Exception {
        String code = codeOf(SOURCE + "solution/MenuItem.java");

        assertFalse(code.contains("switch"));
        assertFalse(code.contains("\"Paste\""));
    }

    @Test
    @DisplayName("the command interface has one method, execute, with no arguments")
    void commandHasOneMethodWithNoArguments() {
        var methods = Command.class.getDeclaredMethods();

        assertEquals(1, methods.length);
        assertEquals("execute", methods[0].getName());
        assertEquals(0, methods[0].getParameterCount());
    }

    @Test
    @DisplayName("a paste command forwards to the document it was given")
    void pasteForwardsToItsDocument() {
        Application application = new Application();
        Document letter = new Document("letter", application.clipboard());
        letter.type("Hello");
        letter.copy();
        MenuItem paste = new MenuItem("Paste", new PasteCommand(letter));

        paste.clicked();

        assertEquals("HelloHello", letter.text());
    }

    @Test
    @DisplayName("an open command asks for a name, creates a document, adds it and opens it")
    void openCreatesAddsAndOpens() {
        Application application = new Application();
        MenuItem open = new MenuItem("Open", new OpenCommand(application, () -> "report"));

        open.clicked();

        assertEquals(1, application.documents().size());
        Document report = application.current();
        assertEquals("report", report.name());
        assertTrue(report.isOpen());
    }

    @Test
    @DisplayName("an open command does nothing when the user cancels the dialog")
    void openDoesNothingWhenCancelled() {
        Application application = new Application();

        new OpenCommand(application, () -> null).execute();
        new OpenCommand(application, () -> "  ").execute();

        assertTrue(application.documents().isEmpty());
    }

    @Test
    @DisplayName("a macro command runs its commands in order, and a menu item cannot tell it apart")
    void aMacroRunsItsCommandsInOrder() {
        List<String> ran = new ArrayList<>();
        MacroCommand macro = new MacroCommand()
                .add(() -> ran.add("first"))
                .add(() -> ran.add("second"))
                .add(() -> ran.add("third"));
        MenuItem item = new MenuItem("Do all", macro);

        item.clicked();

        assertEquals(List.of("first", "second", "third"), ran);
        assertEquals(3, macro.size());
    }

    @Test
    @DisplayName("a removed command is no longer run by the macro")
    void aRemovedCommandIsNotRun() {
        List<String> ran = new ArrayList<>();
        Command second = () -> ran.add("second");
        MacroCommand macro = new MacroCommand().add(() -> ran.add("first")).add(second);

        macro.remove(second);
        macro.execute();

        assertEquals(List.of("first"), ran);
    }

    @Test
    @DisplayName("SimpleCommand with Document::paste does what PasteCommand does")
    void simpleCommandIsPasteWithoutAClass() {
        Application application = new Application();
        Document byClass = new Document("a", application.clipboard());
        Document byReference = new Document("b", application.clipboard());
        application.clipboard().put("text");

        new PasteCommand(byClass).execute();
        new SimpleCommand<>(byReference, Document::paste).execute();

        assertEquals("text", byClass.text());
        assertEquals(byClass.text(), byReference.text());
    }

    @Test
    @DisplayName("the same menu item can be given a different job while the program runs")
    void setCommandChangesTheJob() {
        List<String> ran = new ArrayList<>();
        MenuItem item = new MenuItem("Action", () -> ran.add("old job"));

        item.clicked();
        item.setCommand(() -> ran.add("new job"));
        item.clicked();

        assertEquals(List.of("old job", "new job"), ran);
        assertThrows(NullPointerException.class, () -> item.setCommand(null));
    }

    @Test
    @DisplayName("a menu clicks the item with that label, and refuses a label it does not have")
    void aMenuFindsItsItemByLabel() {
        List<String> ran = new ArrayList<>();
        Menu menu = new Menu()
                .add(new MenuItem("Open", () -> ran.add("open")))
                .add(new MenuItem("Paste", () -> ran.add("paste")));

        menu.click("Paste");

        assertEquals(List.of("paste"), ran);
        assertEquals(List.of("Open", "Paste"), menu.labels());
        assertThrows(IllegalArgumentException.class, () -> menu.click("paste"));
    }
}
