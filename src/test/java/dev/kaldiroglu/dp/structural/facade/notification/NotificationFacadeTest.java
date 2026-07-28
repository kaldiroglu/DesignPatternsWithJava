package dev.kaldiroglu.dp.structural.facade.notification;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * A facade is worth having when the client's dependency list shrinks and the same rules stop
 * being re-typed. Both are countable, so both are counted.
 */
class NotificationFacadeTest {

    private static dev.kaldiroglu.dp.structural.facade.notification.solution1.NotificationConfig config1() {
        return new dev.kaldiroglu.dp.structural.facade.notification.solution1.NotificationConfig(
                "smtp.example.com", 587, "user", "pass",
                "sid", "token", "+900000000",
                "https://hooks.example.com/x", "/tmp/firebase.json", "/tmp/notify.log");
    }

    private static dev.kaldiroglu.dp.structural.facade.notification.solution2.NotificationConfig config2() {
        return new dev.kaldiroglu.dp.structural.facade.notification.solution2.NotificationConfig(
                "smtp.example.com", 587, "user", "pass",
                "sid", "token", "+900000000",
                "https://hooks.example.com/x", "/tmp/firebase.json", "/tmp/notify.log");
    }

    private static dev.kaldiroglu.dp.structural.facade.notification.solution1.User reachable1() {
        return new dev.kaldiroglu.dp.structural.facade.notification.solution1.User("u-1")
                .email("a@example.com").phone("+905550000").slack("#ops").deviceToken("tok");
    }

    private static dev.kaldiroglu.dp.structural.facade.notification.solution2.User reachable2() {
        return new dev.kaldiroglu.dp.structural.facade.notification.solution2.User("u-1")
                .email("a@example.com").phone("+905550000").slack("#ops").deviceToken("tok");
    }

    // ------------------------------------------------------------------ the problem

    @Test
    @DisplayName("without the facade a client carries five subsystem dependencies")
    void theClientCarriesTheSubsystem() {
        long fields = java.util.Arrays.stream(dev.kaldiroglu.dp.structural.facade.notification.problem.Client.class.getDeclaredFields())
                .filter(f -> !f.isSynthetic()).count();

        assertEquals(5, fields, "email, sms, slack, push, logger — in every such class");
        assertEquals(5, dev.kaldiroglu.dp.structural.facade.notification.problem.Client.class.getDeclaredConstructors()[0].getParameterCount());
    }

    @Test
    @DisplayName("and with it, one")
    void theFacadeIsTheOnlyDependency() {
        long fields = java.util.Arrays.stream(dev.kaldiroglu.dp.structural.facade.notification.solution1.Client.class.getDeclaredFields())
                .filter(f -> !f.isSynthetic()).count();

        assertEquals(1, fields, "the facade, and nothing else");
    }

    // ------------------------------------------------------------------ solution 1

    @Test
    @DisplayName("one call reaches every channel the user can be reached on")
    void oneCallReachesEveryChannel() {
        var result = new dev.kaldiroglu.dp.structural.facade.notification.solution1.NotificationFacade(config1())
                .notify(reachable1(), "Order confirmed", "Your order is on its way");

        assertEquals(4, result.getResults().size());
        assertTrue(result.allSucceeded());
    }

    @Test
    @DisplayName("the rules live in the facade, so no client can forget them")
    void theRulesLiveInOnePlace() {
        var facade = new dev.kaldiroglu.dp.structural.facade.notification.solution1.NotificationFacade(config1());

        // Do-not-disturb suppresses Slack ...
        var quiet = new dev.kaldiroglu.dp.structural.facade.notification.solution1.User("u-2")
                .email("a@example.com").slack("#ops").dnd(true);
        assertFalse(facade.notify(quiet, "t", "b").getResults().containsKey("slack"));

        // ... and a body over 160 characters is not sent as an SMS.
        var texter = new dev.kaldiroglu.dp.structural.facade.notification.solution1.User("u-3").phone("+905550000");
        assertFalse(facade.notify(texter, "t", "x".repeat(161)).getResults().containsKey("sms"));
        assertTrue(facade.notify(texter, "t", "short").getResults().containsKey("sms"));
    }

    @Test
    @DisplayName("a user with no contact details reaches nothing, and does not throw")
    void nothingToDo() {
        var result = new dev.kaldiroglu.dp.structural.facade.notification.solution1.NotificationFacade(config1())
                .notify(new dev.kaldiroglu.dp.structural.facade.notification.solution1.User("u-4"), "t", "b");

        assertEquals(0, result.getResults().size());
    }

    // ------------------------------------------------------------------ solution 2

    @Test
    @DisplayName("solution 2 lets the caller choose a subset of channels")
    void channelSubset() {
        var facade = new dev.kaldiroglu.dp.structural.facade.notification.solution2.NotificationFacade(config2());

        var only = facade.notify(reachable2(),
                EnumSet.of(dev.kaldiroglu.dp.structural.facade.notification.solution2.Channel.EMAIL, dev.kaldiroglu.dp.structural.facade.notification.solution2.Channel.SLACK), "t", "b");

        assertEquals(2, only.getResults().size());
        assertTrue(only.getResults().containsKey("email"));
        assertTrue(only.getResults().containsKey("slack"));
    }

    @Test
    @DisplayName("and the convenience overload still means all of them")
    void convenienceOverloadIsAllChannels() {
        var facade = new dev.kaldiroglu.dp.structural.facade.notification.solution2.NotificationFacade(config2());

        assertEquals(4, facade.notify(reachable2(), "t", "b").getResults().size());
    }

    @Test
    @DisplayName("choosing a channel the user cannot receive on is not an error")
    void anImpossibleChannelIsSkipped() {
        var facade = new dev.kaldiroglu.dp.structural.facade.notification.solution2.NotificationFacade(config2());
        var emailOnly = new dev.kaldiroglu.dp.structural.facade.notification.solution2.User("u-5").email("a@example.com");

        var result = facade.notify(emailOnly,
                EnumSet.of(dev.kaldiroglu.dp.structural.facade.notification.solution2.Channel.EMAIL, dev.kaldiroglu.dp.structural.facade.notification.solution2.Channel.SMS), "t", "b");

        assertEquals(1, result.getResults().size());
    }

    // ------------------------------------------------------------------ the shape

    @Test
    @DisplayName("the subsystem knows nothing about the facade")
    void theSubsystemDoesNotDependOnTheFacade() {
        // A facade depends on its subsystem, never the reverse — otherwise the subsystem
        // could not be used without it, and it would be a wall rather than a front door.
        for (Class<?> type : new Class<?>[]{dev.kaldiroglu.dp.structural.facade.notification.solution1.EmailService.class,
                dev.kaldiroglu.dp.structural.facade.notification.solution1.SMSService.class, dev.kaldiroglu.dp.structural.facade.notification.solution1.SlackService.class,
                dev.kaldiroglu.dp.structural.facade.notification.solution1.PushNotificationService.class}) {
            for (var m : type.getDeclaredMethods()) {
                assertTrue(java.util.Arrays.stream(m.getParameterTypes())
                                .noneMatch(p -> p == dev.kaldiroglu.dp.structural.facade.notification.solution1.NotificationFacade.class),
                        type.getSimpleName() + " depends on the facade");
            }
        }
    }
}
