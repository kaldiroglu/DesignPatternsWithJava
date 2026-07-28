package dev.kaldiroglu.dp.structural.facade.hw.reporting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** A facade is judged by what its clients have to name. */
class ReportFacadeTest {

    private static final Set<Class<?>> SUBSYSTEM =
            Set.of(QueryPlan.class, ResultSetCursor.class, QueryEngine.class, PdfRenderer.class);

    private static boolean leaksSubsystemTypes(Class<?> facade) {
        for (Method m : facade.getDeclaredMethods()) {
            if (SUBSYSTEM.contains(m.getReturnType())) {
                return true;
            }
            for (Class<?> p : m.getParameterTypes()) {
                if (SUBSYSTEM.contains(p)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Test
    @DisplayName("both facades produce the same report")
    void sameOutput() {
        LeakyReportFacade leaky = new LeakyReportFacade();
        var cursor = leaky.run(leaky.planFor("orders", "month = 7", 100));
        var rows = new java.util.ArrayList<String>();
        while (cursor.advance()) {
            rows.add(cursor.current());
        }
        byte[] viaLeaky = leaky.toPdf("orders — monthly", rows);

        byte[] viaFacade = new ReportFacade(new QueryEngine(), new PdfRenderer())
                .monthlyReport("orders", "month = 7", 100);

        assertEquals(new String(viaLeaky), new String(viaFacade));
    }

    @Test
    @DisplayName("but only one of them decoupled the caller")
    void onlyOneIsAFacade() {
        assertTrue(leaksSubsystemTypes(LeakyReportFacade.class),
                "the leaky one forces callers to import the subsystem");
        assertFalse(leaksSubsystemTypes(ReportFacade.class),
                "the real one takes strings and returns bytes");
    }

    @Test
    @DisplayName("the cursor protocol is now known in one place, not every caller")
    void theProtocolIsAbsorbed() {
        byte[] report = new ReportFacade(new QueryEngine(), new PdfRenderer())
                .monthlyReport("orders", "all", 10);

        // advance-then-current was a rule every caller had to know and could get wrong.
        assertEquals(4, new String(report).lines().count(), "a title and three rows");
    }
}
