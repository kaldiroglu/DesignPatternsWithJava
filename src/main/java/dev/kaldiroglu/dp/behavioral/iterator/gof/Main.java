package dev.kaldiroglu.dp.behavioral.iterator.gof;

import dev.kaldiroglu.dp.behavioral.iterator.gof.problem.CursorList;
import dev.kaldiroglu.dp.behavioral.iterator.gof.solution.ChainList;
import dev.kaldiroglu.dp.behavioral.iterator.gof.solution.Iterator;
import dev.kaldiroglu.dp.behavioral.iterator.gof.solution.List;
import dev.kaldiroglu.dp.behavioral.iterator.gof.solution.PrintEmployees;
import dev.kaldiroglu.dp.behavioral.iterator.gof.solution.PrintNEmployees;
import dev.kaldiroglu.dp.behavioral.iterator.gof.solution.ReverseListIterator;

import java.util.ArrayList;

/**
 * Pairs every employee with every other one — a loop inside a loop — first with a list that
 * walks itself, then with iterators. Then the other iterators of GoF's sample code.
 */
public final class Main {

    static final String[] NAMES = {"Ayse", "Deniz", "Elif"};

    public static void main(String[] args) {
        CursorList<Employee> cursorList = new CursorList<>();
        for (String name : NAMES) cursorList.append(new Employee(name));

        java.util.List<String> pairs = new ArrayList<>();
        for (cursorList.first(); !cursorList.isDone(); cursorList.next()) {
            Employee a = cursorList.currentItem();
            for (cursorList.first(); !cursorList.isDone(); cursorList.next()) {
                pairs.add(a + "-" + cursorList.currentItem());
            }
        }
        System.out.println("One cursor:      " + pairs);

        List<Employee> list = new List<>();
        for (String name : NAMES) list.append(new Employee(name));

        pairs.clear();
        Iterator<Employee> outer = list.createIterator();
        for (outer.first(); !outer.isDone(); outer.next()) {
            Iterator<Employee> inner = list.createIterator();
            for (inner.first(); !inner.isDone(); inner.next()) {
                pairs.add(outer.currentItem() + "-" + inner.currentItem());
            }
        }
        System.out.println("Two iterators:   " + pairs);

        System.out.println("Forward:         " + PrintEmployees.print(list.createIterator()));
        System.out.println("Backward:        " + PrintEmployees.print(new ReverseListIterator<>(list)));

        ChainList<Employee> chain = new ChainList<>();
        for (String name : NAMES) chain.append(new Employee(name));
        System.out.println("Chain list:      " + PrintEmployees.print(chain.createIterator()));

        PrintNEmployees firstTwo = new PrintNEmployees(list, 2);
        boolean finished = firstTwo.traverse();
        System.out.println("First two:       " + firstTwo.lines() + ", walked to the end: " + finished);
    }
}
