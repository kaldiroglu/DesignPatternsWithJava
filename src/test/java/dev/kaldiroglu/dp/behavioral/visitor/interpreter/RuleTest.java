package dev.kaldiroglu.dp.behavioral.visitor.interpreter;

import dev.kaldiroglu.dp.behavioral.visitor.Printed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** The shop's rule language, taught as the Interpreter section of the Visitor deck. */
class RuleTest {

    /** (category is books and price below 50) or (category is food and not price below 20) */
    private static Rule discount() {
        return new Or(
                new And(new CategoryIs("books"), new PriceBelow(50)),
                new And(new CategoryIs("food"), new Not(new PriceBelow(20))));
    }

    @Test
    @DisplayName("the Novel and the Coffee get a discount, and the Atlas and the Tea do not")
    void fourProducts() {
        Rule rule = discount();

        assertTrue(rule.interpret(new Product("Novel", "books", 40)));
        assertFalse(rule.interpret(new Product("Atlas", "books", 80)));
        assertTrue(rule.interpret(new Product("Coffee", "food", 30)));
        assertFalse(rule.interpret(new Product("Tea", "food", 10)));
    }

    @Test
    @DisplayName("the limits are strict below 50 and inclusive at 20")
    void theLimits() {
        Rule rule = discount();

        assertFalse(rule.interpret(new Product("Book", "books", 50)), "50 is not below 50");
        assertTrue(rule.interpret(new Product("Rice", "food", 20)), "food at 20 or more");
        assertFalse(rule.interpret(new Product("Lamp", "home", 5)), "another category");
    }

    @Test
    @DisplayName("the rule describes itself as it is written")
    void describe() {
        assertEquals("((category is books and price below 50) or "
                + "(category is food and not price below 20))", discount().describe());
    }

    @Test
    @DisplayName("Rule is sealed and permits exactly five classes, all records")
    void fiveRuleClasses() {
        assertTrue(Rule.class.isSealed());
        List<Class<?>> permitted = List.of(Rule.class.getPermittedSubclasses());

        assertEquals(List.of(CategoryIs.class, PriceBelow.class, And.class, Or.class, Not.class), permitted);
        assertTrue(permitted.stream().allMatch(Class::isRecord));
    }

    @Test
    @DisplayName("Main prints the rule and the result for four products")
    void mainOutput() {
        List<String> lines = Printed.by(() -> Main.main(new String[0]));

        assertEquals(List.of(
                "Rule: ((category is books and price below 50) or (category is food and not price below 20))",
                "  Novel, books, 40: discount",
                "  Atlas, books, 80: no discount",
                "  Coffee, food, 30: discount",
                "  Tea, food, 10: no discount"), lines);
    }
}
