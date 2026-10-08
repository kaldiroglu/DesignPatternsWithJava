package dev.kaldiroglu.dp.behavioral.templateMethod.hw.onboarding;

import java.util.ArrayList;
import java.util.List;

/**
 * Homework 2: the first day of a new person.
 * <p>
 * Create the accounts, hand over equipment, assign a mentor, send the welcome message.
 * Employees and contractors differ in two places. Accounts are a primitive operation:
 * each kind must say which ones. Equipment is a <b>hook</b>: by default a laptop is given,
 * and a contractor, who brings their own, overrides it to give nothing.
 * <p>
 * The homework question was whether "skip the equipment" should be a flag or a hook. A
 * flag puts the contractor's rule in this class; a hook keeps it in the contractor's.
 */
public abstract class Onboarding {

    private final List<String> steps = new ArrayList<>();

    /** The template method. */
    public final List<String> start(String person) {
        steps.clear();
        steps.addAll(accounts(person));
        handOverEquipment(person);
        steps.add("mentor for " + person);
        steps.add("welcome message to " + person);
        return List.copyOf(steps);
    }

    /** A primitive operation: which accounts this kind of person gets. */
    protected abstract List<String> accounts(String person);

    /** A hook: give a laptop. */
    protected void handOverEquipment(String person) {
        steps.add("laptop for " + person);
    }
}
