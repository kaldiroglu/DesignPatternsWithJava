package dev.kaldiroglu.dp.behavioral.iterator.gof.problem;

import dev.kaldiroglu.dp.behavioral.iterator.gof.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Pairs every employee with every other one using a list that keeps its own cursor, and shows
 * the walk breaking.
 */
public final class Main {

    public static void main(String[] args) {
        CursorList<Employee> list = new CursorList<>();
        for (String name : new String[] {"Ayse", "Deniz", "Elif"}) {
            list.append(new Employee(name));
        }

        List<String> pairs = new ArrayList<>();
        for (list.first(); !list.isDone(); list.next()) {
            Employee a = list.currentItem();
            for (list.first(); !list.isDone(); list.next()) {
                pairs.add(a + "-" + list.currentItem());
            }
        }
        System.out.println("Expected " + list.count() * list.count() + " pairs, got " + pairs.size()
                + ": " + pairs);
        System.out.println("The inner loop moved the only cursor, so the outer loop ended after Ayse.");
    }
}
