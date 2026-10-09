package dev.kaldiroglu.dp.behavioral.visitor.factory;

import dev.kaldiroglu.dp.behavioral.visitor.Printed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The health check: everyone who has worked more than five years, every manager's
 * psychological state, and the boss if the boss is over fifty.
 */
class HealthVisitorTest {

    @Test
    @DisplayName("an employee with more than five years is checked, and one with five is not")
    void moreThanFiveYears() {
        HealthVisitor visitor = new HealthVisitor();

        assertEquals(List.of("Checking the health status of employee: 1 Ayse"),
                Printed.by(() -> new Employee(1, "Ayse", 6, "Sales").accept(visitor)));
        assertEquals(List.of(),
                Printed.by(() -> new Engineer(2, "Burhan", 5, "Production", "p").accept(visitor)));
    }

    @Test
    @DisplayName("every manager gets a psychological check, and a director is a manager")
    void managers() {
        HealthVisitor visitor = new HealthVisitor();

        assertEquals(List.of("Checking the psychological status of manager: 4 Metin",
                        "Checking the health status of employee: 4 Metin"),
                Printed.by(() -> new Manager(4, "Metin", 14, "Production", "Production").accept(visitor)));
        assertEquals(List.of("Checking the psychological status of manager: 5 Salih"),
                Printed.by(() -> new Director(5, "Salih", 3, "Management", "Management", 4500).accept(visitor)));
    }

    @Test
    @DisplayName("the boss is checked when over fifty, and not at fifty")
    void theBoss() {
        HealthVisitor visitor = new HealthVisitor();

        assertEquals(List.of("Checking the health status of boss: Memet Emmi"),
                Printed.by(() -> new Boss("Memet Emmi", 52).accept(visitor)));
        assertEquals(List.of(), Printed.by(() -> new Boss("Young", 50).accept(visitor)));
    }

    @Test
    @DisplayName("Boss shares no parent with Employee, and Employee has four subclasses but one visit method")
    void oneVisitForFourClasses() {
        assertEquals(Object.class, Boss.class.getSuperclass());
        assertFalse(Employee.class.isAssignableFrom(Boss.class));

        List<Class<?>> subclasses = List.of(Engineer.class, Secretary.class, Manager.class, Director.class);
        assertTrue(subclasses.stream().allMatch(Employee.class::isAssignableFrom));
        assertTrue(Manager.class.isAssignableFrom(Director.class));

        List<Class<?>> visited = Arrays.stream(Visitor.class.getDeclaredMethods())
                .<Class<?>>map(m -> m.getParameterTypes()[0]).toList();
        assertEquals(2, visited.size());
        assertTrue(visited.containsAll(List.of(Employee.class, Boss.class)));
        assertTrue(Arrays.stream(Visitor.class.getDeclaredMethods()).map(Method::getName)
                .allMatch("visit"::equals));
    }
}
