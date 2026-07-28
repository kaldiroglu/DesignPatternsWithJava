package dev.kaldiroglu.dp.structural.proxy.pm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * Three stages, and the tests measure what changes between them: where the screening lives,
 * what type the client holds, and whether the real subject is reached.
 */
class PrimeMinisterTest {

    private static final String REAL = "the bridge on the coast road is closed";
    private static final String FAVOR = "my cousin needs a job at the ministry";

    @Test
    @DisplayName("stage 1 — the PM screens his own calls, and hears every one of them")
    void stageOne() {
        var pm = new dev.kaldiroglu.dp.structural.proxy.pm.pm1.PM();
        var citizen = new dev.kaldiroglu.dp.structural.proxy.pm.pm1.Citizen("Ayse", pm);

        citizen.tellProblem(REAL);
        citizen.tellProblem(FAVOR);

        assertEquals(2, pm.problemsHeard(), "both calls reached him");
        assertEquals(1, pm.problemsResolved(), "and he screened them himself");
    }

    @Test
    @DisplayName("stage 2 — the screening moved, but the client had to be rewritten")
    void stageTwo() {
        var pm = new dev.kaldiroglu.dp.structural.proxy.pm.pm2.PM();
        var proxy = new dev.kaldiroglu.dp.structural.proxy.pm.pm2.Proxy(pm);
        var citizen = new dev.kaldiroglu.dp.structural.proxy.pm.pm2.Citizen("Bora", proxy);

        citizen.tellProblem(REAL);
        citizen.tellProblem(FAVOR);

        assertEquals(2, proxy.callsScreened());
        assertEquals(1, proxy.callsPassedOn());
        assertEquals(1, pm.problemsHeard(), "one call never reached him");

        // The defect of this stage, in the type system: the two classes are unrelated, so
        // Citizen's field had to change from PM to Proxy.
        Class<?> proxyType = dev.kaldiroglu.dp.structural.proxy.pm.pm2.Proxy.class;
        Class<?> pmType = dev.kaldiroglu.dp.structural.proxy.pm.pm2.PM.class;
        assertFalse(pmType.isAssignableFrom(proxyType), "no shared supertype");
        assertEquals(0, proxyType.getInterfaces().length);

        Field field = Arrays.stream(
                        dev.kaldiroglu.dp.structural.proxy.pm.pm2.Citizen.class.getDeclaredFields())
                .filter(f -> f.getName().equals("proxy")).findFirst().orElseThrow();
        assertSame(proxyType, field.getType(), "the client names the stand-in");
    }

    @Test
    @DisplayName("stage 3 — one interface, and the client is back to holding a PM")
    void stageThree() throws Exception {
        var secretary = new dev.kaldiroglu.dp.structural.proxy.pm.pm3.PMSecretary();
        var citizen = new dev.kaldiroglu.dp.structural.proxy.pm.pm3.Citizen("Ayse", secretary);

        citizen.tellProblem(REAL);
        citizen.tellProblem(FAVOR);
        citizen.askForJob();

        var proxy = (dev.kaldiroglu.dp.structural.proxy.pm.pm3.ProxyPM) secretary.getPM();
        assertEquals(2, proxy.callsScreened());
        assertEquals(1, proxy.callsPassedOn());
        assertEquals(1, proxy.callsRefused());

        // The fix, in the type system: the client's field is the Subject type again.
        Field field = Arrays.stream(
                        dev.kaldiroglu.dp.structural.proxy.pm.pm3.Citizen.class.getDeclaredFields())
                .filter(f -> f.getName().equals("pm")).findFirst().orElseThrow();
        assertSame(dev.kaldiroglu.dp.structural.proxy.pm.pm3.PM.class, field.getType());
    }

    @Test
    @DisplayName("the proxy is substitutable and the real subject is unreachable")
    void substitutable() {
        var secretary = new dev.kaldiroglu.dp.structural.proxy.pm.pm3.PMSecretary();

        assertTrue(dev.kaldiroglu.dp.structural.proxy.pm.pm3.PM.class
                .isAssignableFrom(dev.kaldiroglu.dp.structural.proxy.pm.pm3.ProxyPM.class));
        assertTrue(dev.kaldiroglu.dp.structural.proxy.pm.pm3.PM.class
                .isAssignableFrom(dev.kaldiroglu.dp.structural.proxy.pm.pm3.RealPM.class));

        // The secretary hands out the proxy, never the real subject.
        assertTrue(secretary.getPM() instanceof dev.kaldiroglu.dp.structural.proxy.pm.pm3.ProxyPM);
    }

    @Test
    @DisplayName("the Prime Minister is not created until somebody asks — a virtual proxy too")
    void createdLazily() {
        var secretary = new dev.kaldiroglu.dp.structural.proxy.pm.pm3.PMSecretary();

        assertFalse(secretary.hasBeenAskedForThePM());
        secretary.getPM();
        assertTrue(secretary.hasBeenAskedForThePM());
        assertSame(secretary.getPM(), secretary.getPM(), "and only once");
    }

    @Test
    @DisplayName("a proxy may answer entirely by itself — findJob never reaches the PM")
    void answeredWithoutForwarding() {
        var real = new dev.kaldiroglu.dp.structural.proxy.pm.pm3.RealPM();
        var proxy = new dev.kaldiroglu.dp.structural.proxy.pm.pm3.ProxyPM(real);

        proxy.findJob("Bora");

        // No decorator would do this: the subject was not consulted at all.
        assertEquals(0, real.problemsHeard());
        assertEquals(0, proxy.callsPassedOn());
    }

    @Test
    @DisplayName("a proxy must stand in front of something")
    void nullSubjectIsRejected() {
        assertThrows(NullPointerException.class,
                () -> new dev.kaldiroglu.dp.structural.proxy.pm.pm3.ProxyPM(null));
    }
}
