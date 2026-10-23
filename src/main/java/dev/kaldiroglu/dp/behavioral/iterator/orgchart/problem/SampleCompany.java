package dev.kaldiroglu.dp.behavioral.iterator.orgchart.problem;

import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;

/**
 * The small company that stage three and the change report run on, built from
 * {@link CallbackDepartment}s.
 */
final class SampleCompany {

    private SampleCompany() {
    }

    static CallbackDepartment build(String salesHead) {
        return build(salesHead, "Can", "Elif");
    }

    static CallbackDepartment build(String salesHead, String exporter, String supporter) {
        CallbackDepartment sales = new CallbackDepartment("Sales")
                .add(new Employee(salesHead, "head of sales"))
                .add(new Employee("Ali", "sales"))
                .add(new CallbackDepartment("Export").add(new Employee(exporter, "export")));
        CallbackDepartment operations = new CallbackDepartment("Operations")
                .add(new Employee("Mert", "head of operations"))
                .add(new CallbackDepartment("Support").add(new Employee(supporter, "support")));
        return new CallbackDepartment("Head office")
                .add(new Employee("Ayse", "CEO")).add(sales).add(operations);
    }
}
