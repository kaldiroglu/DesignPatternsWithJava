package dev.kaldiroglu.dp.structural.proxy.hw.licence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Homework 4 — the licence proxy.
 * <p>
 * The one homework where a single proxy is three of GoF's kinds at once, and the tests are
 * arranged to show each of them separately: a refusal (protection), an object that was
 * never built (virtual), and a seat that comes back (smart reference).
 */
class LicenceProxyTest {

    private LicenceServer server;

    @BeforeEach
    void setUp() {
        VlsiDesigner.resetLaunchCount();
        server = new LicenceServer("VlsiDesigner", 3);
    }

    private LicenceProxy student(String name) {
        return new LicenceProxy(name, server);
    }

    // ------------------------------------------------------------------ protection

    @Test
    @DisplayName("three seats serve three students and refuse the fourth")
    void theFourthStudentIsRefused() {
        student("Ayse").launch();
        student("Bora").launch();
        student("Cem").launch();

        NoLicenceAvailableException refused =
                assertThrows(NoLicenceAvailableException.class, () -> student("Deniz").launch());

        assertEquals(3, server.inUse());
        assertEquals(0, server.available());
        assertEquals(1, refused.position(), "and is told where in the queue she stands");
        assertTrue(refused.getMessage().contains("VlsiDesigner"));
    }

    @Test
    @DisplayName("the refusal is a throw, not a half-working application")
    void refusalIsLoud() {
        student("Ayse").launch();
        student("Bora").launch();
        student("Cem").launch();

        LicenceProxy deniz = student("Deniz");
        assertThrows(NoLicenceAvailableException.class, deniz::launch);
        assertFalse(deniz.hasLicence());
        assertThrows(IllegalStateException.class, () -> deniz.open("adder.vhd"),
                "and the application cannot be used behind the refusal's back");
    }

    // ------------------------------------------------------------------ virtual

    @Test
    @DisplayName("a refused student costs no object at all")
    void theExpensiveObjectIsNeverBuiltForARefusal() {
        student("Ayse").launch();
        student("Bora").launch();
        student("Cem").launch();
        assertEquals(3, VlsiDesigner.launchCount());

        for (String name : List.of("Deniz", "Ece", "Fatma")) {
            assertThrows(NoLicenceAvailableException.class, () -> student(name).launch());
        }

        assertEquals(3, VlsiDesigner.launchCount(),
                "three refusals, and not one application was created");
    }

    @Test
    @DisplayName("relaunching does not pay for start-up twice")
    void theRealApplicationIsCreatedOnce() {
        LicenceProxy ayse = student("Ayse");

        ayse.launch();
        ayse.close();
        ayse.launch();

        assertEquals(1, VlsiDesigner.launchCount());
    }

    // ------------------------------------------------------------------ smart reference

    @Test
    @DisplayName("closing gives the seat back and promotes whoever waited longest")
    void closingPromotesTheQueue() {
        LicenceProxy ayse = student("Ayse");
        ayse.launch();
        student("Bora").launch();
        student("Cem").launch();

        assertThrows(NoLicenceAvailableException.class, () -> student("Deniz").launch());
        assertThrows(NoLicenceAvailableException.class, () -> student("Ece").launch());
        assertEquals(List.of("Deniz", "Ece"), server.queue());

        ayse.close();

        assertEquals(List.of("Ece"), server.queue(), "Deniz was promoted, in order");
        assertTrue(server.isHolding("Deniz"));
        assertFalse(server.isHolding("Ayse"));
        assertEquals(3, server.inUse(), "the seat did not go idle");
    }

    @Test
    @DisplayName("the promoted student can then launch, and only then is her app built")
    void thePromotedStudentGetsIn() {
        LicenceProxy ayse = student("Ayse");
        student("Bora").launch();
        student("Cem").launch();
        ayse.launch();

        LicenceProxy deniz = student("Deniz");
        assertThrows(NoLicenceAvailableException.class, deniz::launch);
        assertEquals(3, VlsiDesigner.launchCount());

        ayse.close();
        deniz.launch();

        assertEquals(4, VlsiDesigner.launchCount(), "built now, and not before");
        assertEquals("VlsiDesigner[Deniz] editing adder.vhd", deniz.open("adder.vhd"));
    }

    @Test
    @DisplayName("asking twice does not consume two seats")
    void acquiringIsIdempotentPerStudent() {
        LicenceProxy ayse = student("Ayse");

        ayse.launch();
        ayse.launch();

        assertEquals(1, server.inUse());
        assertEquals(2, server.available());
    }

    // ------------------------------------------------------------------ the pattern

    @Test
    @DisplayName("the student's code names Application, never the licence machinery")
    void theClientHoldsTheSubject() {
        Application app = student("Ayse");      // <-- the whole point: this type
        app.launch();

        assertEquals("VlsiDesigner[Ayse] editing adder.vhd", app.open("adder.vhd"));

        boolean subjectMentionsLicences = java.util.Arrays.stream(Application.class.getMethods())
                .anyMatch(m -> m.getName().toLowerCase().contains("licence")
                        || m.getName().toLowerCase().contains("seat"));
        assertFalse(subjectMentionsLicences,
                "nothing on the interface tells the caller a seat had to be found");
    }

    @Test
    @DisplayName("the real application knows nothing about licences")
    void theRealSubjectIsUnaware() {
        boolean knows = java.util.Arrays.stream(VlsiDesigner.class.getDeclaredFields())
                .anyMatch(f -> f.getType() == LicenceServer.class);

        assertFalse(knows, "which is what lets the university change its seat count "
                + "without recompiling the application");
    }
}
