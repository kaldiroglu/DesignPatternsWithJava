package dev.kaldiroglu.dp.creational.introduction.employee.better;

import java.math.BigDecimal;
import java.util.Objects;

public class Compensation {
    private final BigDecimal salary;

    public Compensation(BigDecimal salary) {
        this.salary = (salary == null) ? BigDecimal.ZERO : salary;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Compensation)) return false;
        Compensation that = (Compensation) o;
        return Objects.equals(salary, that.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(salary);
    }

}