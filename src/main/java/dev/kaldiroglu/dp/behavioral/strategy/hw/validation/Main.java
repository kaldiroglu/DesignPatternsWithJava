package dev.kaldiroglu.dp.behavioral.strategy.hw.validation;

/** Checks the same passphrases on a relaxed form with one rule and a strict form with three. */
public class Main {

    public static void main(String[] args) {
        SignUpForm relaxed = new SignUpForm(new MinimumLength(8));
        SignUpForm strict = new SignUpForm(new MinimumLength(12), new MixedCharacters(),
                NoCommonWords.theUsualSuspects());
        System.out.println("Relaxed form: " + relaxed.ruleCount() + " rule. Strict form: "
                + strict.ruleCount() + " rules.");

        for (String passphrase : new String[] {"hunter2024", "password", "kedi-42-balkon!"}) {
            System.out.println("'" + passphrase + "'");
            System.out.println("  relaxed accepts: " + relaxed.accepts(passphrase));
            System.out.println("  strict accepts: " + strict.accepts(passphrase)
                    + " " + strict.complaints(passphrase));
        }
    }
}
