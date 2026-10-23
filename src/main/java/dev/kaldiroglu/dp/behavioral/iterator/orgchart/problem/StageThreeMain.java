package dev.kaldiroglu.dp.behavioral.iterator.orgchart.problem;

import dev.kaldiroglu.dp.behavioral.iterator.orgchart.domain.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Stage three: the department walks itself and calls the caller back for each person.
 */
public final class StageThreeMain {

    public static void main(String[] args) {
        List<Employee> byLevel = new ArrayList<>();
        SampleCompany.build("Deniz").forEachMemberByLevel(byLevel::add);
        System.out.println("Stage three, level by level: " + byLevel);
    }
}
