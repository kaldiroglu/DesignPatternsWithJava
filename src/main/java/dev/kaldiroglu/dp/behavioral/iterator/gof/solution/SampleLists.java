package dev.kaldiroglu.dp.behavioral.iterator.gof.solution;

import dev.kaldiroglu.dp.behavioral.iterator.gof.Employee;

/**
 * The same three employees, in a list stored in an array and in a chain list.
 */
final class SampleLists {

    private static final String[] NAMES = {"Ayse", "Deniz", "Elif"};

    private SampleLists() {
    }

    static List<Employee> list() {
        List<Employee> list = new List<>();
        for (String name : NAMES) list.append(new Employee(name));
        return list;
    }

    static ChainList<Employee> chain() {
        ChainList<Employee> chain = new ChainList<>();
        for (String name : NAMES) chain.append(new Employee(name));
        return chain;
    }
}
