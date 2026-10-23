package dev.kaldiroglu.dp.behavioral.iterator.gof.solution;

import dev.kaldiroglu.dp.behavioral.iterator.gof.Employee;

import java.util.ArrayList;

/**
 * Pairs every employee with every other one: a loop inside a loop, with two iterators.
 */
public final class TwoIteratorsMain {

    public static void main(String[] args) {
        List<Employee> list = SampleLists.list();

        java.util.List<String> pairs = new ArrayList<>();
        Iterator<Employee> outer = list.createIterator();
        for (outer.first(); !outer.isDone(); outer.next()) {
            Iterator<Employee> inner = list.createIterator();
            for (inner.first(); !inner.isDone(); inner.next()) {
                pairs.add(outer.currentItem() + "-" + inner.currentItem());
            }
        }
        System.out.println("Two iterators, " + pairs.size() + " pairs: " + pairs);
    }
}
