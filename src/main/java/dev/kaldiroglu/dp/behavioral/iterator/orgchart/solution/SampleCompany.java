package dev.kaldiroglu.dp.behavioral.iterator.orgchart.solution;

import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;

/**
 * The small company that the two orders and the change report run on.
 */
final class SampleCompany {

    private SampleCompany() {
    }

    static Department build(String salesHead) {
        return build(salesHead, "Can", "Elif");
    }

    static Department build(String salesHead, String exporter, String supporter) {
        Department export = new Department("Export")
                .add(new Employee(exporter, "export"));
        Department sales = new Department("Sales")
                .add(new Employee(salesHead, "head of sales"))
                .add(new Employee("Ali", "sales"))
                .add(export);
        Department support = new Department("Support")
                .add(new Employee(supporter, "support"));
        Department operations = new Department("Operations")
                .add(new Employee("Mert", "head of operations"))
                .add(support);
        return new Department("Head office")
                .add(new Employee("Ayse", "CEO"))
                .add(sales)
                .add(operations);
    }
}
