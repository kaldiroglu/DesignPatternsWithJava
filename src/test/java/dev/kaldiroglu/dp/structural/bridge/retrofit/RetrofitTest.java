package dev.kaldiroglu.dp.structural.bridge.retrofit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A standard arrives over a system that already works. Nothing is rewritten.
 * <p>
 * These tests assert the three things the slides claim: the engine's own callers are
 * untouched, the required interface works over an engine it was never designed for, and the
 * second engine costs one class and no report.
 */
class RetrofitTest {

    @Test
    @DisplayName("the engine's own callers keep working, untouched")
    void legacyCallersAreUndisturbed() {
        LegacyEngine engine = new LegacyEngine();

        // A caller that predates the regulation entirely, calling the engine directly.
        String row = engine.reportDirectly("select headcount");

        assertTrue(row.contains("SELECT HEADCOUNT"));
        assertEquals(0, engine.openSessions(), "the engine cleaned up after itself, as always");
    }

    @Test
    @DisplayName("the required interface works over an engine it was never designed for")
    void theStandardIsSatisfied() {
        LegacyEngine engine = new LegacyEngine();
        RegulatoryReport report = new QuarterlyReport(engine);

        List<String> rows = report.submit("2026-Q1");

        assertEquals(1, rows.size());
        assertTrue(rows.getFirst().contains("QUARTER 2026-Q1".toUpperCase()));
        assertEquals("legacy", report.engineName());

        // submit() is composed from the engine's own primitives, and released its session.
        assertEquals(0, engine.openSessions());
        assertEquals(List.of("select ledger for quarter 2026-Q1"), engine.statementsSeen());
    }

    @Test
    @DisplayName("a second engine costs one class, and no report is touched")
    void theSecondEngineChangesNoReport() {
        // Written for the legacy engine, and never edited since.
        RegulatoryReport quarterly = new QuarterlyReport(new PurchasedEngine());
        RegulatoryReport audited = new AuditedReport(new PurchasedEngine());

        assertEquals("purchased", quarterly.engineName());
        assertTrue(quarterly.submit("2026-Q2").getFirst().contains("conn[ledger]"));
        assertTrue(audited.submit("2026-Q2").getFirst().endsWith("[audited]"));
    }

    @Test
    @DisplayName("the engine can be swapped on a report that already exists")
    void theEngineIsAField() {
        RegulatoryReport report = new AuditedReport(new LegacyEngine());
        assertEquals("legacy", report.engineName());

        report.setEngine(new PurchasedEngine());          // the same report object

        assertEquals("purchased", report.engineName());
        assertTrue(report.submit("2026-Q3").getFirst().endsWith("[audited]"));
    }

    @Test
    @DisplayName("the implementor does not mirror the abstraction — no report operations on it")
    void theTwoInterfacesAreDifferent() {
        List<String> vendorMethods = java.util.Arrays.stream(VendorClient.class.getMethods())
                .map(m -> m.getName()).sorted().toList();

        assertEquals(List.of("name", "open", "pull", "release"), vendorMethods);

        // The word the regulation uses appears nowhere on the engine's interface. If it did,
        // the two interfaces would be the same interface twice and there would be no bridge.
        assertFalse(vendorMethods.contains("submit"));
    }

    @Test
    @DisplayName("both sides are hierarchies, which is what makes it a Bridge and not an Adapter")
    void bothSidesAreFamilies() {
        assertTrue(Modifier.isAbstract(RegulatoryReport.class.getModifiers()));
        assertTrue(RegulatoryReport.class.isAssignableFrom(QuarterlyReport.class));
        assertTrue(RegulatoryReport.class.isAssignableFrom(AuditedReport.class));

        assertTrue(VendorClient.class.isInterface());
        assertTrue(VendorClient.class.isAssignableFrom(LegacyEngine.class));
        assertTrue(VendorClient.class.isAssignableFrom(PurchasedEngine.class));

        // Counted from the package rather than asserted as arithmetic: two refinements and
        // two engines, which is 2 + 2 classes where a wrapper per pair would have written 4
        // and would write 6 the day a third report arrives.
        long reports = typesIn().stream()
                .filter(RegulatoryReport.class::isAssignableFrom)
                .filter(t -> t != RegulatoryReport.class).count();
        long engines = typesIn().stream()
                .filter(VendorClient.class::isAssignableFrom)
                .filter(t -> t != VendorClient.class).count();

        assertEquals(2, reports, "refined abstractions");
        assertEquals(2, engines, "engines behind the standard");
        assertEquals(4, reports + engines, "m + n");
        assertEquals(4, reports * engines, "m x n — equal at two by two, and never again");
    }

    /** Every top-level type in this package. */
    private static List<Class<?>> typesIn() {
        String pkg = VendorClient.class.getPackageName();
        try {
            List<java.net.URL> roots = java.util.Collections.list(
                    RetrofitTest.class.getClassLoader().getResources(pkg.replace('.', '/')));
            List<Class<?>> types = new java.util.ArrayList<>();
            for (java.net.URL root : roots) {
                try (var files = java.nio.file.Files.list(java.nio.file.Path.of(root.toURI()))) {
                    for (java.nio.file.Path f : files.sorted().toList()) {
                        String n = f.getFileName().toString();
                        if (n.endsWith(".class") && !n.contains("$")) {
                            Class<?> t = Class.forName(pkg + '.' + n.substring(0, n.length() - 6));
                            if (!types.contains(t)) {
                                types.add(t);
                            }
                        }
                    }
                }
            }
            return types;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
