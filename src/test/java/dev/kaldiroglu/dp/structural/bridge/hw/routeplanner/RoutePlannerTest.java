package dev.kaldiroglu.dp.structural.bridge.hw.routeplanner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * The homework's deliverable is a diff: after the vendor swap, nothing on the abstraction side
 * was touched. A diff cannot be asserted, but the property that makes it possible can be —
 * so the last test walks the abstraction classes and fails if any concrete provider type has
 * leaked into one.
 */
class RoutePlannerTest {

    private static final List<String> HUBS = List.of("Uskudar", "Sisli");
    private static final String FROM = "Kadikoy";
    private static final String TO = "Levent";

    @Test
    @DisplayName("the same routing code gives different answers on different maps")
    void theSameRulesOverTwoProviders() {
        Route inHouse = new FastestRoute(new InHouseMaps()).plan(FROM, TO, HUBS);
        Route vendor = new FastestRoute(new VendorMaps()).plan(FROM, TO, HUBS);

        assertEquals(List.of("Kadikoy", "Uskudar", "Levent"), inHouse.stops());
        assertEquals(List.of("Kadikoy", "Uskudar", "Levent"), vendor.stops());
        assertEquals(2100, inHouse.seconds());
        assertEquals(1620, vendor.seconds());
        assertNotEquals(inHouse.seconds(), vendor.seconds());
    }

    @Test
    @DisplayName("each route kind prefers something different on the same map")
    void threeKindsThreePreferences() {
        MapProvider maps = new InHouseMaps();

        assertEquals(2100, new FastestRoute(maps).plan(FROM, TO, HUBS).seconds());
        assertEquals(700, new CheapestRoute(maps).plan(FROM, TO, HUBS).tollMinor());
        assertTrue(new StepFreeRoute(maps).plan(FROM, TO, HUBS).stepFree());
    }

    @Test
    @DisplayName("better survey data changes which route is step-free, and the planner follows")
    void theVendorKnowsAboutTheSteps() {
        // In-house believes Uskudar > Levent is step-free; the vendor surveyed it and it is not.
        assertTrue(new InHouseMaps().stepFree("Uskudar", "Levent"));
        assertTrue(!new VendorMaps().stepFree("Uskudar", "Levent"));

        Route inHouse = new StepFreeRoute(new InHouseMaps()).plan(FROM, TO, HUBS);
        Route vendor = new StepFreeRoute(new VendorMaps()).plan(FROM, TO, HUBS);

        assertEquals(List.of("Kadikoy", "Uskudar", "Levent"), inHouse.stops());
        assertEquals(List.of("Kadikoy", "Sisli", "Levent"), vendor.stops());
        assertTrue(inHouse.stepFree());
        assertTrue(vendor.stepFree());
    }

    @Test
    @DisplayName("swapping the provider is one line, and it is not in the abstraction")
    void noProviderTypeReachesTheAbstraction() {
        List<Class<?>> abstraction = List.of(
                RoutePlanner.class, FastestRoute.class, CheapestRoute.class, StepFreeRoute.class);
        List<Class<?>> concreteProviders = List.of(InHouseMaps.class, VendorMaps.class);

        for (Class<?> type : abstraction) {
            for (Field field : type.getDeclaredFields()) {
                assertTrue(!concreteProviders.contains(field.getType()),
                        type.getSimpleName() + "." + field.getName() + " names a provider");
            }
            for (var constructor : type.getDeclaredConstructors()) {
                for (Class<?> parameter : constructor.getParameterTypes()) {
                    assertTrue(!concreteProviders.contains(parameter),
                            type.getSimpleName() + " takes a concrete provider");
                }
            }
        }

        // The only thing the abstraction holds is the interface.
        assertSame(MapProvider.class,
                java.util.Arrays.stream(RoutePlanner.class.getDeclaredFields())
                        .filter(f -> f.getName().equals("maps"))
                        .findFirst().orElseThrow().getType());
    }

    @Test
    @DisplayName("a leg the map has never heard of fails loudly rather than guessing")
    void unknownLegsAreRejected() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> new FastestRoute(new InHouseMaps()).plan("Kadikoy", "Ankara", List.of()));
    }
}
