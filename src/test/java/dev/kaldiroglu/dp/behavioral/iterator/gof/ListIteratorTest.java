package dev.kaldiroglu.dp.behavioral.iterator.gof;

import dev.kaldiroglu.dp.behavioral.command.lender.Printed;
import dev.kaldiroglu.dp.behavioral.iterator.gof.problem.CursorList;
import dev.kaldiroglu.dp.behavioral.iterator.gof.solution.AbstractList;
import dev.kaldiroglu.dp.behavioral.iterator.gof.solution.ChainList;
import dev.kaldiroglu.dp.behavioral.iterator.gof.solution.Iterator;
import dev.kaldiroglu.dp.behavioral.iterator.gof.solution.List;
import dev.kaldiroglu.dp.behavioral.iterator.gof.solution.ListTraverser;
import dev.kaldiroglu.dp.behavioral.iterator.gof.solution.PrintEmployees;
import dev.kaldiroglu.dp.behavioral.iterator.gof.solution.PrintNEmployees;
import dev.kaldiroglu.dp.behavioral.iterator.gof.solution.ReverseListIterator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GoF's list example. The 3 pairs and the 9 pairs on the Part 2 slides are asserted here,
 * from the lists themselves and from gof.Main's output.
 */
class ListIteratorTest {

    private static <L extends AbstractList<Employee>> L filled(L list) {
        for (String name : Main.NAMES) {
            list.append(new Employee(name));
        }
        return list;
    }

    @Test
    @DisplayName("a list that walks itself: a loop inside a loop gives 3 pairs, not 9")
    void oneCursorGivesThreePairs() {
        CursorList<Employee> list = new CursorList<>();
        for (String name : Main.NAMES) {
            list.append(new Employee(name));
        }

        java.util.List<String> pairs = new ArrayList<>();
        for (list.first(); !list.isDone(); list.next()) {
            Employee a = list.currentItem();
            for (list.first(); !list.isDone(); list.next()) {
                pairs.add(a + "-" + list.currentItem());
            }
        }

        assertEquals(3, Main.NAMES.length);
        assertEquals(3, pairs.size());
        assertEquals(java.util.List.of("Ayse-Ayse", "Ayse-Deniz", "Ayse-Elif"), pairs);
    }

    @Test
    @DisplayName("with two iterators, the loop inside a loop gives all 9 pairs")
    void twoIteratorsGiveNinePairs() {
        List<Employee> list = filled(new List<>());

        java.util.List<String> pairs = new ArrayList<>();
        Iterator<Employee> outer = list.createIterator();
        for (outer.first(); !outer.isDone(); outer.next()) {
            Iterator<Employee> inner = list.createIterator();
            for (inner.first(); !inner.isDone(); inner.next()) {
                pairs.add(outer.currentItem() + "-" + inner.currentItem());
            }
        }

        assertEquals(Main.NAMES.length * Main.NAMES.length, pairs.size());
        assertEquals(9, pairs.size());
    }

    @Test
    @DisplayName("gof.Main prints the pairs from both loops and the other iterators")
    void mainPrintsBothResults() {
        java.util.List<String> lines = Printed.by(() -> Main.main(new String[0]));

        assertEquals(java.util.List.of(
                "One cursor:      [Ayse-Ayse, Ayse-Deniz, Ayse-Elif]",
                "Two iterators:   [Ayse-Ayse, Ayse-Deniz, Ayse-Elif, Deniz-Ayse, Deniz-Deniz, "
                        + "Deniz-Elif, Elif-Ayse, Elif-Deniz, Elif-Elif]",
                "Forward:         [Ayse, Deniz, Elif]",
                "Backward:        [Elif, Deniz, Ayse]",
                "Chain list:      [Ayse, Deniz, Elif]",
                "First two:       [Ayse, Deniz], walked to the end: false"), lines);
    }

    @Test
    @DisplayName("the same client walks an array list and a chain list without knowing which it has")
    void polymorphicIteration() {
        java.util.List<AbstractList<Employee>> lists =
                java.util.List.of(filled(new List<>()), filled(new ChainList<>()));

        for (AbstractList<Employee> list : lists) {
            assertEquals(java.util.List.of("Ayse", "Deniz", "Elif"),
                    PrintEmployees.print(list.createIterator()));
        }
    }

    @Test
    @DisplayName("a reverse iterator walks the same list backwards, and the list did not change")
    void reverseIterator() {
        List<Employee> list = filled(new List<>());

        assertEquals(java.util.List.of("Elif", "Deniz", "Ayse"),
                PrintEmployees.print(new ReverseListIterator<>(list)));
        assertEquals(java.util.List.of("Ayse", "Deniz", "Elif"),
                PrintEmployees.print(list.createIterator()));
    }

    @Test
    @DisplayName("an iterator past the end refuses to give an item")
    void currentItemAfterTheEnd() {
        List<Employee> list = filled(new List<>());
        Iterator<Employee> forward = list.createIterator();
        Iterator<Employee> chain = filled(new ChainList<>()).createIterator();
        Iterator<Employee> backward = new ReverseListIterator<>(list);

        for (Iterator<Employee> iterator : java.util.List.of(forward, chain, backward)) {
            for (iterator.first(); !iterator.isDone(); iterator.next()) {
                iterator.currentItem();
            }
            assertThrows(IllegalStateException.class, iterator::currentItem);
        }
    }

    @Test
    @DisplayName("the internal iterator stops early when processItem returns false")
    void printNEmployeesStopsAfterN() {
        PrintNEmployees firstTwo = new PrintNEmployees(filled(new List<>()), 2);

        assertFalse(firstTwo.traverse(), "the walk stopped early");
        assertEquals(java.util.List.of("Ayse", "Deniz"), firstTwo.lines());
    }

    @Test
    @DisplayName("the internal iterator answers true when it processed every item")
    void aTraverserThatNeverStops() {
        java.util.List<String> seen = new ArrayList<>();
        ListTraverser<Employee> all = new ListTraverser<>(filled(new ChainList<>())) {
            @Override
            protected boolean processItem(Employee item) {
                seen.add(item.name());
                return true;
            }
        };

        assertTrue(all.traverse());
        assertEquals(java.util.List.of("Ayse", "Deniz", "Elif"), seen);
    }

    @Test
    @DisplayName("lists grow past their first four slots")
    void listsGrow() {
        List<Employee> list = new List<>();
        CursorList<Employee> cursorList = new CursorList<>();
        for (int i = 0; i < 9; i++) {
            list.append(new Employee("E" + i));
            cursorList.append(new Employee("E" + i));
        }

        assertEquals(9, list.count());
        assertEquals(9, cursorList.count());
        assertEquals("E8", list.get(8).name());
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(9));
    }
}
