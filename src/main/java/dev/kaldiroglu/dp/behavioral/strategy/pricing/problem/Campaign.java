package dev.kaldiroglu.dp.behavioral.strategy.pricing.problem;

/**
 * Stage two, part one: the campaign codes become an enum.
 * <p>
 * A real improvement over the strings in {@link SwitchingCheckout}, and it should be said
 * out loud. A typo is now a compile error, the set of campaigns is written down in one
 * place, and the switch in {@link EnumCheckout} can be made exhaustive so the compiler
 * names the branch you forgot.
 */
public enum Campaign {
    NONE,
    STUDENT,
    STAFF,
    BLACK_FRIDAY,
    BUY_TWO_GET_ONE
}
