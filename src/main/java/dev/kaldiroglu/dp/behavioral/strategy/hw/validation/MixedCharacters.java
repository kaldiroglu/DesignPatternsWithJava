package dev.kaldiroglu.dp.behavioral.strategy.hw.validation;

import java.util.ArrayList;
import java.util.List;

/** Letters, digits and something else — the rule most markets settled on. */
public final class MixedCharacters implements PassphraseRule {

    @Override
    public String name() {
        return "MIXED";
    }

    @Override
    public List<String> complaints(String passphrase) {
        List<String> complaints = new ArrayList<>();
        if (passphrase.chars().noneMatch(Character::isLetter)) {
            complaints.add("no letters");
        }
        if (passphrase.chars().noneMatch(Character::isDigit)) {
            complaints.add("no digits");
        }
        if (passphrase.chars().allMatch(Character::isLetterOrDigit)) {
            complaints.add("no punctuation");
        }
        return List.copyOf(complaints);
    }
}
