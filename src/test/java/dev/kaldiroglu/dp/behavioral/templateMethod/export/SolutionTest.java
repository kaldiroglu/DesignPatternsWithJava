package dev.kaldiroglu.dp.behavioral.templateMethod.export;

import dev.kaldiroglu.dp.behavioral.command.lender.Printed;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Export;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.ExportNotAllowedException;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.solution.CsvExporter;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.solution.HtmlExporter;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.solution.Main;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.solution.MarkdownExporter;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.solution.ReportExporter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.kaldiroglu.dp.behavioral.templateMethod.export.ProblemTest.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The report exporters with a template method. The audit-log table on the Part 3 slides
 * is asserted here.
 */
class SolutionTest {

    private static Set<String> declaredMethodsOf(Class<?> type) {
        return Arrays.stream(type.getDeclaredMethods())
                .filter(m -> !m.isSynthetic())
                .map(Method::getName)
                .collect(Collectors.toSet());
    }

    @Test
    @DisplayName("the promise kept: three exports, three lines in the audit log")
    void everyExportIsAudited() {
        AuditLog audit = new AuditLog();

        Export csv = new CsvExporter(audit).export(DENIZ, REPORT);
        Export html = new HtmlExporter(audit).export(DENIZ, REPORT);
        Export markdown = new MarkdownExporter(audit).export(DENIZ, REPORT);

        assertEquals(new Export("sales-october.csv", CSV), csv);
        assertEquals(new Export("sales-october.html", HTML), html);
        assertEquals(new Export("sales-october.md", MARKDOWN), markdown);
        assertEquals(3, audit.lines().size());
        assertEquals(List.of("Deniz exported sales-october.csv (2 rows)",
                "Deniz exported sales-october.html (2 rows)",
                "Deniz exported sales-october.md (2 rows)"), audit.lines());
    }

    @Test
    @DisplayName("Main prints stage three's audit log with 1 line and the template method's with 3")
    void mainPrintsBothAuditLogs() {
        List<String> lines = Printed.by(() -> Main.main(new String[0]));

        assertEquals(List.of(
                "Stage three, two exports, audit log: [Deniz exported sales-october.csv (2 rows)]",
                "--- sales-october.csv",
                "customer,amount",
                "Ayse,1200",
                "Deniz,800",
                "--- sales-october.html",
                "<table>",
                "<tr><th>customer</th><th>amount</th></tr>",
                "<tr><td>Ayse</td><td>1200</td></tr>",
                "<tr><td>Deniz</td><td>800</td></tr>",
                "</table>",
                "--- sales-october.md",
                "| customer | amount |",
                "|---|---|",
                "| Ayse | 1200 |",
                "| Deniz | 800 |",
                "Template method, three exports, audit log: [Deniz exported sales-october.csv (2 rows), "
                        + "Deniz exported sales-october.html (2 rows), Deniz exported sales-october.md (2 rows)]"),
                lines);
    }

    @Test
    @DisplayName("a user who may not export gets an exception from every format, and nothing is audited")
    void permissionComesFirst() {
        AuditLog audit = new AuditLog();

        for (ReportExporter exporter : List.of(new CsvExporter(audit), new HtmlExporter(audit),
                new MarkdownExporter(audit))) {
            assertThrows(ExportNotAllowedException.class, () -> exporter.export(GUEST, REPORT));
        }
        assertEquals(List.of(), audit.lines());
    }

    @Test
    @DisplayName("the template method export is final, and no subclass declares one")
    void theTemplateMethodIsFinal() throws Exception {
        Method export = ReportExporter.class.getDeclaredMethod("export",
                dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.User.class,
                dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Report.class);

        assertTrue(Modifier.isFinal(export.getModifiers()));
        for (Class<?> format : List.of(CsvExporter.class, HtmlExporter.class, MarkdownExporter.class)) {
            assertFalse(declaredMethodsOf(format).contains("export"), format.getSimpleName());
        }
    }

    @Test
    @DisplayName("header, row and extension are abstract; footer is a hook that returns an empty string")
    void abstractStepsAndOneHook() throws Exception {
        for (String step : List.of("header", "row", "extension")) {
            Method method = Arrays.stream(ReportExporter.class.getDeclaredMethods())
                    .filter(m -> m.getName().equals(step)).findFirst().orElseThrow();
            assertTrue(Modifier.isAbstract(method.getModifiers()), step);
            assertTrue(Modifier.isProtected(method.getModifiers()), step);
        }
        Method footer = ReportExporter.class.getDeclaredMethod("footer");
        assertFalse(Modifier.isAbstract(footer.getModifiers()));

        // A format that does not override the hook adds nothing after its rows.
        ReportExporter noFooter = new ReportExporter(new AuditLog()) {
            @Override
            protected String header() {
                return "H\n";
            }

            @Override
            protected String row(dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Sale sale) {
                return sale.customer() + "\n";
            }

            @Override
            protected String extension() {
                return ".txt";
            }
        };
        assertEquals("H\nAyse\nDeniz\n", noFooter.export(DENIZ, REPORT).content());
    }

    @Test
    @DisplayName("subclasses write only the text: three short methods, and HTML also uses the footer hook")
    void subclassesWriteOnlyTheText() {
        Set<String> threeSteps = Set.of("header", "row", "extension");

        assertEquals(threeSteps, declaredMethodsOf(CsvExporter.class));
        assertEquals(threeSteps, declaredMethodsOf(MarkdownExporter.class));
        assertEquals(Set.of("header", "row", "extension", "footer"), declaredMethodsOf(HtmlExporter.class));
    }

    @Test
    @DisplayName("no subclass mentions the permission check or the audit log")
    void subclassesNeverMentionPermissionOrAudit() throws Exception {
        for (String file : List.of("CsvExporter.java", "HtmlExporter.java", "MarkdownExporter.java")) {
            String code = codeOf(SOURCE + "solution/" + file);
            assertEquals(0, countOf(code, "mayExport"), file);
            assertEquals(0, countOf(code, "audit.record"), file);
        }
    }
}
