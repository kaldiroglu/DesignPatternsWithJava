package dev.kaldiroglu.dp.behavioral.templateMethod.export;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Export;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.ExportNotAllowedException;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Report;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Sale;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.User;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.CsvExport;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.Format;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.MarkdownExport;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.StandaloneCsvExport;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.StandaloneHtmlExport;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.SwitchingExporter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** The three attempts of Part 1, and the reversal: a Markdown export that skips the audit. */
class ProblemTest {

    static final String SOURCE =
            "src/main/java/dev/kaldiroglu/dp/behavioral/templateMethod/export/";

    static final Report REPORT = new Report("sales-october",
            List.of(new Sale("Ayse", 1200), new Sale("Deniz", 800)));
    static final User DENIZ = new User("Deniz", true);
    static final User GUEST = new User("Guest", false);

    static final String CSV = "customer,amount\nAyse,1200\nDeniz,800\n";
    static final String HTML = "<table>\n<tr><th>customer</th><th>amount</th></tr>\n"
            + "<tr><td>Ayse</td><td>1200</td></tr>\n<tr><td>Deniz</td><td>800</td></tr>\n</table>\n";
    static final String MARKDOWN = "| customer | amount |\n|---|---|\n| Ayse | 1200 |\n| Deniz | 800 |\n";

    /** The source of a file with its comments removed, so comments cannot match a search. */
    static String codeOf(String path) throws Exception {
        String text = Files.readString(Path.of(path));
        text = text.replaceAll("(?s)/\\*.*?\\*/", "");
        return text.replaceAll("//[^\\n]*", "");
    }

    static int countOf(String text, String needle) {
        int count = 0;
        for (int i = text.indexOf(needle); i >= 0; i = text.indexOf(needle, i + needle.length())) {
            count++;
        }
        return count;
    }

    // ------------------------------------------------------------------ stage one

    @Test
    @DisplayName("stage one works: the CSV file is right, and the audit log has the export")
    void stageOneWorks() {
        AuditLog audit = new AuditLog();

        Export csv = new StandaloneCsvExport(audit).export(DENIZ, REPORT);
        Export html = new StandaloneHtmlExport(audit).export(DENIZ, REPORT);

        assertEquals(new Export("sales-october.csv", CSV), csv);
        assertEquals(new Export("sales-october.html", HTML), html);
        assertEquals(List.of("Deniz exported sales-october.csv (2 rows)",
                "Deniz exported sales-october.html (2 rows)"), audit.lines());
    }

    @Test
    @DisplayName("stage one: the permission check and the audit line are written again in every copy")
    void stageOneCopiesTheSharedSteps() throws Exception {
        for (String file : List.of("StandaloneCsvExport.java", "StandaloneHtmlExport.java")) {
            String code = codeOf(SOURCE + "problem/" + file);
            assertEquals(1, countOf(code, "user.mayExport()"), file);
            assertEquals(1, countOf(code, "audit.record("), file);
        }
    }

    // ------------------------------------------------------------------ stage two

    @Test
    @DisplayName("stage two: one class makes both formats, permission first and audit last")
    void stageTwoWorks() {
        AuditLog audit = new AuditLog();
        SwitchingExporter exporter = new SwitchingExporter(audit);

        assertEquals(new Export("sales-october.csv", CSV), exporter.export(DENIZ, REPORT, Format.CSV));
        assertEquals(new Export("sales-october.html", HTML), exporter.export(DENIZ, REPORT, Format.HTML));
        assertEquals(2, audit.lines().size());
    }

    @Test
    @DisplayName("stage two: every format is a branch in this class, in three switches")
    void stageTwoHasThreeSwitches() throws Exception {
        String code = codeOf(SOURCE + "problem/SwitchingExporter.java");

        assertEquals(3, countOf(code, "switch (format)"));
        assertEquals(3 * Format.values().length, countOf(code, "case "),
                "each format is a branch in all three switches");
    }

    // ------------------------------------------------------------------ stage three

    @Test
    @DisplayName("stage three: the Markdown export makes a correct file and checks the permission")
    void markdownMakesACorrectFile() {
        AuditLog audit = new AuditLog();

        assertEquals(new Export("sales-october.md", MARKDOWN),
                new MarkdownExport(audit).export(DENIZ, REPORT));
        assertThrows(ExportNotAllowedException.class,
                () -> new MarkdownExport(audit).export(GUEST, REPORT));
    }

    @Test
    @DisplayName("the reversal: after a CSV and a Markdown export, the audit log has one line, the CSV one")
    void theMarkdownExportIsMissingFromTheAudit() {
        AuditLog audit = new AuditLog();

        new CsvExport(audit).export(DENIZ, REPORT);
        new MarkdownExport(audit).export(DENIZ, REPORT);

        assertEquals(1, audit.lines().size());
        assertEquals(List.of("Deniz exported sales-october.csv (2 rows)"), audit.lines());
    }

    @Test
    @DisplayName("stage three: MarkdownExport never calls recordAudit, and CsvExport does")
    void markdownExportNeverCallsRecordAudit() throws Exception {
        assertEquals(0, countOf(codeOf(SOURCE + "problem/MarkdownExport.java"), "recordAudit("));
        assertEquals(1, countOf(codeOf(SOURCE + "problem/CsvExport.java"), "recordAudit("));
    }

    @Test
    @DisplayName("a user who may not export gets an exception, and nothing reaches the audit log")
    void aRefusedExportIsNotAudited() {
        AuditLog audit = new AuditLog();

        assertThrows(ExportNotAllowedException.class,
                () -> new StandaloneCsvExport(audit).export(GUEST, REPORT));
        assertThrows(ExportNotAllowedException.class,
                () -> new StandaloneHtmlExport(audit).export(GUEST, REPORT));
        assertThrows(ExportNotAllowedException.class,
                () -> new SwitchingExporter(audit).export(GUEST, REPORT, Format.CSV));
        assertThrows(ExportNotAllowedException.class,
                () -> new CsvExport(audit).export(GUEST, REPORT));

        assertEquals(List.of(), audit.lines());
    }
}
