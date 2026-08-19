package dev.kaldiroglu.dp.behavioral.strategy.hw.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * The Context: runs whichever rules this market requires.
 * <p>
 * Note that it holds a <em>list</em> of strategies rather than one. That is a legitimate
 * shape — GoF's Strategy says nothing about how many a context may hold — and it is what
 * makes "the market decides the rules" a line of configuration.
 */
public final class SignUpForm {

    private final List<PassphraseRule> rules = new ArrayList<>();

    public SignUpForm(PassphraseRule... rules) {
        this.rules.addAll(List.of(rules));
    }

    public int ruleCount() {
        return rules.size();
    }

    public List<String> complaints(String passphrase) {
        List<String> all = new ArrayList<>();
        for (PassphraseRule rule : rules) {
            all.addAll(rule.complaints(passphrase));
        }
        return List.copyOf(all);
    }

    public boolean accepts(String passphrase) {
        return complaints(passphrase).isEmpty();
    }
}
