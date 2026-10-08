package dev.kaldiroglu.dp.behavioral.templateMethod.hw.onboarding;

import java.util.List;

/** A contractor gets e-mail only, and brings their own laptop. */
public final class ContractorOnboarding extends Onboarding {

    @Override
    protected List<String> accounts(String person) {
        return List.of("e-mail for " + person);
    }

    @Override
    protected void handOverEquipment(String person) {
        // a contractor brings their own laptop
    }
}
