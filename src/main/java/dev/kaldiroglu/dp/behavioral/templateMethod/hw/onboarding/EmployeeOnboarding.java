package dev.kaldiroglu.dp.behavioral.templateMethod.hw.onboarding;

import java.util.List;

/** An employee gets e-mail and payroll accounts, and the default equipment. */
public final class EmployeeOnboarding extends Onboarding {

    @Override
    protected List<String> accounts(String person) {
        return List.of("e-mail for " + person, "payroll for " + person);
    }
}
