package dev.kaldiroglu.dp.behavioral.iterator.orgchart.solution;

import dev.kaldiroglu.dp.behavioral.command.lender.Printed;
import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The department that gives out iterators. Every output the Part 3 slides quote from
 * orgchart.solution.Main is asserted here.
 */
class SolutionTest {

    private static List<String> walk(Iterable<Employee> employees) {
        List<String> names = new ArrayList<>();
        for (Employee employee : employees) {
            names.add(employee.toString());
        }
        return names;
    }

    @Test
    @DisplayName("Main prints the two orders and the first change, as the Part 3 slides quote them")
    void mainPrintsWhatTheSlidesQuote() {
        List<String> lines = Printed.by(() -> Main.main(new String[0]));

        assertEquals(List.of(
                "Department by department:",
                "  Ayse (CEO)",
                "  Deniz (head of sales)",
                "  Ali (sales)",
                "  Can (export)",
                "  Mert (head of operations)",
                "  Elif (support)",
                "Level by level:",
                "  Ayse (CEO)",
                "  Deniz (head of sales)",
                "  Ali (sales)",
                "  Mert (head of operations)",
                "  Can (export)",
                "  Elif (support)",
                "First change: Deniz (head of sales) -> Zeynep (head of sales)",
                "All changes:",
                "  Deniz (head of sales) -> Zeynep (head of sales)",
                "  Can (export) -> Burak (export)",
                "  Elif (support) -> Ece (support)"), lines);
    }

    @Test
    @DisplayName("the two orders differ only in rows 4 and 5: Can is one level lower than Mert")
    void theTwoOrdersDifferInRowsFourAndFive() {
        Department company = Main.company("Deniz");

        List<String> byDepartment = walk(company);
        List<String> byLevel = walk(company.byLevel());

        assertEquals(6, byDepartment.size());
        assertEquals(6, byLevel.size());
        for (int row = 1; row <= 6; row++) {
            boolean same = byDepartment.get(row - 1).equals(byLevel.get(row - 1));
            assertEquals(row != 4 && row != 5, same, "row " + row);
        }
        assertEquals("Can (export)", byDepartment.get(3));
        assertEquals("Mert (head of operations)", byLevel.get(3));
    }

    @Test
    @DisplayName("the change report moves two iterators together and stops at the first difference")
    void theReportFindsTheNewHeadOfSales() {
        Optional<String> change = new ChangeReport().firstDifference(Main.company("Deniz"),
                Main.company("Zeynep"));

        assertEquals(Optional.of("Deniz (head of sales) -> Zeynep (head of sales)"), change);
    }

    @Test
    @DisplayName("with three people new, the first difference is still only the head of sales")
    void theFirstDifferenceStopsAtTheFirstOfThree() {
        assertEquals(Optional.of("Deniz (head of sales) -> Zeynep (head of sales)"),
                new ChangeReport().firstDifference(Main.company("Deniz"),
                        Main.company("Zeynep", "Burak", "Ece")));
    }

    @Test
    @DisplayName("all differences walks both charts to the end and finds the three new people")
    void allDifferencesFindsAllThree() {
        ChangeReport report = new ChangeReport();

        assertEquals(List.of(
                "Deniz (head of sales) -> Zeynep (head of sales)",
                "Can (export) -> Burak (export)",
                "Elif (support) -> Ece (support)"),
                report.allDifferences(Main.company("Deniz"), Main.company("Zeynep", "Burak", "Ece")));
        assertEquals(List.of(),
                report.allDifferences(Main.company("Deniz"), Main.company("Deniz")));
        assertEquals(List.of("the charts have different sizes"),
                report.allDifferences(Main.company("Deniz"),
                        new Department("Head office").add(new Employee("Ayse", "CEO"))));
    }

    @Test
    @DisplayName("the change report answers none for equal charts and sees a size change")
    void theReportHandlesEqualAndShorterCharts() {
        Department shorter = new Department("Head office").add(new Employee("Ayse", "CEO"));

        assertEquals(Optional.empty(),
                new ChangeReport().firstDifference(Main.company("Deniz"), Main.company("Deniz")));
        assertEquals(Optional.of("the charts have different sizes"),
                new ChangeReport().firstDifference(Main.company("Deniz"), shorter));
        assertEquals(Optional.of("the charts have different sizes"),
                new ChangeReport().firstDifference(shorter, Main.company("Deniz")));
    }

    @Test
    @DisplayName("two iterators over the same department keep their own positions")
    void twoIteratorsDoNotDisturbEachOther() {
        Department company = Main.company("Deniz");
        Iterator<Employee> first = company.iterator();
        Iterator<Employee> second = company.iterator();

        first.next();
        first.next();
        first.next();

        assertEquals("Ayse (CEO)", second.next().toString());
        assertEquals("Can (export)", first.next().toString());
    }

    @Test
    @DisplayName("the department has no public getter for its members or sub-departments")
    void theDepartmentGivesOutNoLists() {
        List<String> listGetters = Arrays.stream(Department.class.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .filter(m -> List.class.isAssignableFrom(m.getReturnType()))
                .map(Method::getName)
                .toList();

        assertEquals(List.of(), listGetters);
    }

    @Test
    @DisplayName("nothing is copied: a person added to a department the walk has not reached yet is seen")
    void theIteratorReadsTheLiveDepartment() {
        Department later = new Department("Later").add(new Employee("Elif", "support"));
        Department company = new Department("Head office")
                .add(new Employee("Ayse", "CEO"))
                .add(later);
        Iterator<Employee> walk = company.iterator();

        assertEquals("Ayse (CEO)", walk.next().toString());
        later.add(new Employee("Mert", "support"));

        assertEquals("Elif (support)", walk.next().toString());
        assertEquals("Mert (support)", walk.next().toString());
        assertFalse(walk.hasNext());
    }

    @Test
    @DisplayName("adding to the list the iterator is walking throws ConcurrentModificationException on the next step")
    void addingDuringTheWalkThrows() {
        Department sales = new Department("Sales")
                .add(new Employee("Deniz", "head of sales"))
                .add(new Employee("Ali", "sales"));
        Iterator<Employee> walk = sales.iterator();
        walk.next();

        sales.add(new Employee("Can", "sales"));

        assertThrows(ConcurrentModificationException.class, walk::next);
    }

    @Test
    @DisplayName("an iterator past the last person throws NoSuchElementException, in both orders")
    void bothIteratorsEndCleanly() {
        Department empty = new Department("Empty").add(new Department("Also empty"));

        assertFalse(empty.iterator().hasNext());
        assertThrows(NoSuchElementException.class, () -> empty.iterator().next());
        assertThrows(NoSuchElementException.class, () -> empty.byLevel().iterator().next());
    }
}
