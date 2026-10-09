package dev.kaldiroglu.dp.behavioral.iterator.gof.solution;

import dev.kaldiroglu.dp.behavioral.iterator.gof.Employee;

import java.util.ArrayList;

/**
 * Pairs every employee with every other one using two iterators, then walks backward and over
 * a chain list.
 */
public final class Main {

    public static void main(String[] args) {
        List<Employee> list = new List<>();
        ChainList<Employee> chain = new ChainList<>();
        for (String name : new String[] {"Ayse", "Deniz", "Elif"}) {
            list.append(new Employee(name));
            chain.append(new Employee(name));
        }

        java.util.List<String> pairs = new ArrayList<>();
        Iterator<Employee> outer = list.createIterator();
        for (outer.first(); !outer.isDone(); outer.next()) {
            Iterator<Employee> inner = list.createIterator();
            for (inner.first(); !inner.isDone(); inner.next()) {
                pairs.add(outer.currentItem() + "-" + inner.currentItem());
            }
        }
        System.out.println("Two iterators, " + pairs.size() + " pairs: " + pairs);

        System.out.println("Backward: " + PrintEmployees.print(new ReverseListIterator<>(list)));
        System.out.println("Chain list: " + PrintEmployees.print(chain.createIterator()));

        PrintNEmployees firstTwo = new PrintNEmployees(list, 2);
        boolean finished = firstTwo.traverse();
        System.out.println("First two: " + firstTwo.lines() + ", walked to the end: " + finished);
    }
}
