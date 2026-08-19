package dev.kaldiroglu.dp.behavioral.strategy.hw.validation;

import java.util.List;
import java.util.Set;

/**
 * A rule that needs data, which is why it is worth a class where the length rule is not.
 * <p>
 * It carries a word list, it is the one that will grow, and it is the one somebody will want
 * to swap for a service call.
 */
public final class NoCommonWords implements PassphraseRule {

    private final Set<String> banned;

    public NoCommonWords(Set<String> banned) {
        this.banned = Set.copyOf(banned);
    }

    public static NoCommonWords theUsualSuspects() {
        return new NoCommonWords(Set.of("password", "123456", "qwerty", "admin", "letmein"));
    }

    @Override
    public String name() {
        return "NO_COMMON_WORDS";
    }

    @Override
    public List<String> complaints(String passphrase) {
        String lower = passphrase.toLowerCase();
        return banned.stream().anyMatch(lower::contains)
                ? List.of("contains a word from the banned list")
                : List.of();
    }
}
