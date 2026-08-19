package dev.kaldiroglu.dp.behavioral.strategy.hw;

import dev.kaldiroglu.dp.behavioral.strategy.hw.latefee.CappedFee;
import dev.kaldiroglu.dp.behavioral.strategy.hw.latefee.FeeRule;
import dev.kaldiroglu.dp.behavioral.strategy.hw.latefee.GraceThenDouble;
import dev.kaldiroglu.dp.behavioral.strategy.hw.latefee.Loan;
import dev.kaldiroglu.dp.behavioral.strategy.hw.latefee.ReturnsDesk;
import dev.kaldiroglu.dp.behavioral.strategy.hw.latefee.StandardFee;
import dev.kaldiroglu.dp.behavioral.strategy.hw.seating.BookingDesk;
import dev.kaldiroglu.dp.behavioral.strategy.hw.seating.FirstAvailable;
import dev.kaldiroglu.dp.behavioral.strategy.hw.seating.KeepTogether;
import dev.kaldiroglu.dp.behavioral.strategy.hw.seating.SeatPlan;
import dev.kaldiroglu.dp.behavioral.strategy.hw.seating.WindowPreferred;
import dev.kaldiroglu.dp.behavioral.strategy.hw.validation.MinimumLength;
import dev.kaldiroglu.dp.behavioral.strategy.hw.validation.MixedCharacters;
import dev.kaldiroglu.dp.behavioral.strategy.hw.validation.NoCommonWords;
import dev.kaldiroglu.dp.behavioral.strategy.hw.validation.PassphraseRule;
import dev.kaldiroglu.dp.behavioral.strategy.hw.validation.SignUpForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Worked solutions for the three homework problems, so that every figure on the homework
 * slides is one a test asserts.
 */
class HomeworkTest {

    @Nested
    @DisplayName("1 - seating a booking")
    class Seating {

        private final SeatPlan cabin = SeatPlan.empty(3, 4);   // 3 rows, A to D

        @Test
        @DisplayName("three policies, three different sets of seats for the same party")
        void threePolicies() {
            BookingDesk desk = new BookingDesk(new FirstAvailable());
            assertEquals(List.of("1A", "1B"), desk.seat(cabin, 2));

            desk.setPolicy(new WindowPreferred());
            assertEquals(List.of("1A", "1D"), desk.seat(cabin, 2));

            desk.setPolicy(new KeepTogether());
            assertEquals(List.of("1A", "1B"), desk.seat(cabin, 2));
        }

        @Test
        @DisplayName("a policy may legitimately fail where the others succeed")
        void keepTogetherCanRefuse() {
            // Two free seats, but never two in the same row.
            SeatPlan scattered = cabin.withTaken(
                    List.of("1A", "1B", "1C", "2A", "2B", "2C", "3A", "3B", "3C", "3D"));

            assertEquals(2, scattered.free().size());
            assertEquals(List.of("1D", "2D"), new FirstAvailable().allocate(scattered, 2));
            assertEquals(List.of(), new KeepTogether().allocate(scattered, 2),
                    "an empty list is an answer, not an error");
        }

        @Test
        @DisplayName("window seats are the first and last letter of a row")
        void windowsAreTheEdges() {
            assertTrue(cabin.isWindow("2A"));
            assertTrue(cabin.isWindow("2D"));
            assertFalse(cabin.isWindow("2B"));
        }
    }

    @Nested
    @DisplayName("2 - what an overdue item costs")
    class LateFees {

        private final Loan tenDaysLate = new Loan("Design Patterns", 10, 50);

        @Test
        @DisplayName("three rules, three charges for the same loan")
        void threeRules() {
            ReturnsDesk desk = new ReturnsDesk(new StandardFee());
            assertEquals(500, desk.charge(tenDaysLate));

            desk.setRule(new CappedFee(300));
            assertEquals(300, desk.charge(tenDaysLate));

            desk.setRule(new GraceThenDouble(3));
            assertEquals(700, desk.charge(tenDaysLate));   // 7 chargeable days at double
        }

        @Test
        @DisplayName("the grace rule charges nothing inside the grace period")
        void insideTheGrace() {
            FeeRule rule = new GraceThenDouble(3);
            assertEquals(0, rule.charge(new Loan("Refactoring", 3, 50)));
            assertEquals(100, rule.charge(new Loan("Refactoring", 4, 50)));
        }

        @Test
        @DisplayName("a rule that is not a multiplier is why the interface is a method")
        void notEveryRuleIsARate() {
            // Doubling after a grace period cannot be expressed as a rate per day: at ten
            // days the standard rule charges 500 and this one charges 700, and at two days
            // the standard charges 100 and this one nothing.
            assertEquals(500, new StandardFee().charge(tenDaysLate));
            assertEquals(700, new GraceThenDouble(3).charge(tenDaysLate));

            Loan twoDays = new Loan("Design Patterns", 2, 50);
            assertEquals(100, new StandardFee().charge(twoDays));
            assertEquals(0, new GraceThenDouble(3).charge(twoDays));
        }

        @Test
        @DisplayName("an item returned early is a mistake, and says so")
        void negativeDaysAreRejected() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Loan("Design Patterns", -1, 50));
        }
    }

    @Nested
    @DisplayName("3 - what a market requires of a passphrase")
    class Validation {

        @Test
        @DisplayName("the market decides the rules, and that is configuration")
        void marketsDiffer() {
            SignUpForm relaxed = new SignUpForm(new MinimumLength(8));
            SignUpForm strict = new SignUpForm(new MinimumLength(12), new MixedCharacters(),
                    NoCommonWords.theUsualSuspects());

            assertTrue(relaxed.accepts("hunter2024"));
            assertFalse(strict.accepts("hunter2024"));
            assertEquals(1, relaxed.ruleCount());
            assertEquals(3, strict.ruleCount());
        }

        @Test
        @DisplayName("every complaint is collected, not just the first")
        void allComplaints() {
            SignUpForm strict = new SignUpForm(new MinimumLength(12), new MixedCharacters(),
                    NoCommonWords.theUsualSuspects());

            List<String> complaints = strict.complaints("password");

            assertEquals(4, complaints.size());
            assertTrue(complaints.contains("shorter than 12 characters"));
            assertTrue(complaints.contains("no digits"));
            assertTrue(complaints.contains("no punctuation"));
            assertTrue(complaints.contains("contains a word from the banned list"));
        }

        @Test
        @DisplayName("a good passphrase satisfies every rule at once")
        void oneThatPasses() {
            SignUpForm strict = new SignUpForm(new MinimumLength(12), new MixedCharacters(),
                    NoCommonWords.theUsualSuspects());

            assertTrue(strict.accepts("kedi-42-balkon!"));
            assertEquals(List.of(), strict.complaints("kedi-42-balkon!"));
        }

        @Test
        @DisplayName("and the question the exercise really asks")
        void isAClassWorthIt() {
            // MinimumLength is one comparison. NoCommonWords carries data, will grow, and is
            // the one somebody will want to swap for a service call. Both are strategies
            // here; only the second earns the class. That is the judgement the exercise is
            // for, and there is no test that settles it — only this one, which shows the
            // difference in what they hold.
            PassphraseRule trivial = new MinimumLength(8);
            PassphraseRule substantial = NoCommonWords.theUsualSuspects();

            assertEquals(0, trivial.complaints("longenough").size());
            assertEquals(1, substantial.complaints("my-password-1").size());
            assertEquals(1, trivial.getClass().getDeclaredFields().length);
            assertEquals(1, substantial.getClass().getDeclaredFields().length);
        }
    }
}
