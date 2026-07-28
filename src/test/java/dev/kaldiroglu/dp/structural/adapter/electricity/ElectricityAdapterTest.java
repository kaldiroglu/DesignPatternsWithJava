package dev.kaldiroglu.dp.structural.adapter.electricity;

import dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr.Appliance;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr.TurkishHomeAppliance;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr.TurkishPowerSource;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.us.USPowerProvider;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.us.USPowerSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * Adapting is not renaming. The Turkish world has two operations where the American world has
 * one toggle, and whether that translation survives depends on something the interface does
 * not mention. These tests measure it.
 */
class ElectricityAdapterTest {

    /** A US source that records the switch, so a test can see what actually happened. */
    private static final class CountingUSSource implements USPowerSource {
        private boolean live;
        private int pushes;

        @Override
        public void providePowerAt110V() {
        }

        @Override
        public void pushSwitch() {
            pushes++;
            live = !live;
        }

        boolean isLive() {
            return live;
        }

        int pushes() {
            return pushes;
        }
    }

    // ------------------------------------------------------------------ the problem

    @Test
    @DisplayName("attempt 1 loses polymorphism — the client cannot hold the interface")
    void attemptOneLosesTheInterface() {
        // setUSPowerSource is not on Appliance, so a variable of that type cannot reach it.
        boolean onInterface = Arrays.stream(Appliance.class.getMethods())
                .anyMatch(m -> m.getName().equals("setUSPowerSource"));
        assertFalse(onInterface, "the second setter is not on the interface");

        boolean onTheClass = Arrays.stream(
                        dev.kaldiroglu.dp.structural.adapter.electricity.problem1.TurkishHomeAppliance.class.getMethods())
                .anyMatch(m -> m.getName().equals("setUSPowerSource"));
        assertTrue(onTheClass, "so the client must name the concrete class to use it");
    }

    @Test
    @DisplayName("attempt 2 moves the branch into a class whose name states two things")
    void attemptTwoIsANameSmell() {
        String name = dev.kaldiroglu.dp.structural.adapter.electricity.problem2
                .TurkishHomeApplianceCompatibleWithUSPowerSource.class.getSimpleName();

        assertTrue(name.contains("TurkishHomeAppliance") && name.contains("USPowerSource"),
                "a class name stating two things is a class carrying two axes");
        assertTrue(name.length() > 40, "and it is not a name anybody enjoys typing");
    }

    // ------------------------------------------------------------------ the adapter

    @Test
    @DisplayName("the object adapter lets a Turkish appliance run on an American source")
    void objectAdapterWorks() {
        CountingUSSource source = new CountingUSSource();
        TurkishPowerSource adapter =
                new dev.kaldiroglu.dp.structural.adapter.electricity.powerAdapter1.USTurkishPowerAdapter(source);

        Appliance shaver = new TurkishHomeAppliance("Shaver");   // unchanged, unaware
        shaver.setPowerSource(adapter);                          // and holds the interface

        shaver.start();
        assertTrue(source.isLive());
        shaver.stop();
        assertFalse(source.isLive());
        assertEquals(2, source.pushes());
    }

    @Test
    @DisplayName("the stateful adapter is idempotent, as the Turkish interface implies")
    void statefulAdapterPreservesMeaning() {
        CountingUSSource source = new CountingUSSource();
        TurkishPowerSource adapter =
                new dev.kaldiroglu.dp.structural.adapter.electricity.powerAdapter1.USTurkishPowerAdapter(source);

        adapter.turnOn();
        adapter.turnOn();     // "on" twice must still mean on
        assertTrue(source.isLive());
        assertEquals(1, source.pushes(), "the second call correctly did nothing");

        adapter.turnOff();
        adapter.turnOff();
        assertFalse(source.isLive());
        assertEquals(2, source.pushes());
    }

    @Test
    @DisplayName("the richer adapter is idempotent too — it has to be")
    void richerAdapterAlsoTracksState() {
        CountingUSSource source = new CountingUSSource();
        TurkishPowerSource adapter =
                new dev.kaldiroglu.dp.structural.adapter.electricity.powerAdapter2.USTurkishPowerAdapter(source);

        // Two operations onto one toggle is only correct if somebody remembers the state.
        // Without it, "on" twice would leave the appliance off — which is what this
        // adapter used to do, and what this test now prevents coming back.
        adapter.turnOn();
        adapter.turnOn();
        assertTrue(source.isLive());
        assertEquals(1, source.pushes(), "the second call correctly did nothing");

        adapter.turnOff();
        adapter.turnOff();
        assertFalse(source.isLive());
        assertEquals(2, source.pushes());
    }

    @Test
    @DisplayName("and it does more than translate — a check and regulation, once")
    void richerAdapterDoesExtraWork() {
        Class<?> richer =
                dev.kaldiroglu.dp.structural.adapter.electricity.powerAdapter2.USTurkishPowerAdapter.class;

        // GoF's point: an adapter may carry work of its own beyond renaming methods.
        assertTrue(Arrays.stream(richer.getDeclaredMethods())
                .anyMatch(m -> m.getName().equals("check")));
        assertTrue(Arrays.stream(richer.getDeclaredMethods())
                .anyMatch(m -> m.getName().equals("regulateVoltage")));
    }

    // ------------------------------------------------------------------ the variants

    @Test
    @DisplayName("the class adapter inherits the adaptee instead of holding one")
    void classAdapterHasNoAdapteeField() {
        Class<?> adapter = dev.kaldiroglu.dp.structural.adapter.electricity.classAdapter.USTurkishPowerAdapter.class;

        assertEquals(dev.kaldiroglu.dp.structural.adapter.electricity.classAdapter.USPowerSource.class, adapter.getSuperclass());
        assertTrue(dev.kaldiroglu.dp.structural.adapter.electricity.classAdapter.TurkishPowerSource.class.isAssignableFrom(adapter));
        assertEquals(0, adapter.getDeclaredFields().length, "no adaptee field: it is one");
    }

    @Test
    @DisplayName("the class adapter can also be used as the adaptee — the object one cannot")
    void classAdapterIsAlsoTheAdaptee() {
        Object adapter = new dev.kaldiroglu.dp.structural.adapter.electricity.classAdapter.USTurkishPowerAdapter();

        assertTrue(adapter instanceof dev.kaldiroglu.dp.structural.adapter.electricity.classAdapter.USPowerSource);
        assertFalse(new dev.kaldiroglu.dp.structural.adapter.electricity.powerAdapter1.USTurkishPowerAdapter(new USPowerProvider())
                instanceof USPowerSource, "the object adapter is not a US source");
    }

    @Test
    @DisplayName("the two-way adapter is both interfaces at once")
    void twoWayAdapterIsBoth() {
        Class<?> both = dev.kaldiroglu.dp.structural.adapter.electricity.twoWayAdapter.TwoWayUSTurkishPowerAdapter.class;

        assertTrue(TurkishPowerSource.class.isAssignableFrom(both));
        assertTrue(USPowerSource.class.isAssignableFrom(both));
    }

    @Test
    @DisplayName("the parameterized pluggable adapter replaces one class per source")
    void pluggableAdapterIsOneClassForAll() {
        Class<?> pluggable =
                dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.electricity.parameterized.PluggablePowerAdapter.class;

        // Two Runnables stand in for the whole narrow interface, so US, UK, Kenya and
        // anything else need no class of their own.
        assertEquals(2, pluggable.getDeclaredFields().length);
        for (Method m : pluggable.getDeclaredMethods()) {
            assertFalse(m.getName().toLowerCase().contains("us"),
                    "no source is named in the adapter: " + m.getName());
        }
    }
}
