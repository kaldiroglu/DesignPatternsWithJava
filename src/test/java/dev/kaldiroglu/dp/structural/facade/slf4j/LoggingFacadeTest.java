package dev.kaldiroglu.dp.structural.facade.slf4j;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * SLF4J is the facade everybody has already used, and it is worth showing because it answers
 * the question the notification example raises: what does a facade look like when the
 * subsystem behind it is chosen by somebody else, at deployment?
 */
class LoggingFacadeTest {

    @AfterEach
    void restoreDefaultBinding() {
        LoggerFactory.useEngine(new ConsoleLoggingEngine(LogLevel.INFO));
    }

    @Test
    @DisplayName("application code names only the facade")
    void applicationCodeSeesOneType() {
        RecordingLoggingEngine engine = new RecordingLoggingEngine(LogLevel.DEBUG);
        LoggerFactory.useEngine(engine);

        Logger log = LoggerFactory.getLogger(LoggingFacadeTest.class);
        log.info("started");

        assertEquals(1, engine.events().size());
        assertEquals(LogLevel.INFO, engine.events().getFirst().level());
        assertEquals(LoggingFacadeTest.class.getName(), engine.events().getFirst().loggerName());
    }

    @Test
    @DisplayName("the backend is chosen once, and every logger follows")
    void oneChoiceForTheWholeApplication() {
        RecordingLoggingEngine engine = new RecordingLoggingEngine(LogLevel.TRACE);
        LoggerFactory.useEngine(engine);

        LoggerFactory.getLogger("a").warn("one");
        LoggerFactory.getLogger("b").error("two");

        assertEquals(2, engine.events().size());
        assertEquals(List.of("a", "b"),
                engine.events().stream().map(LogEvent::loggerName).toList());
    }

    @Test
    @DisplayName("the threshold is the subsystem's business, not the caller's")
    void levelFiltering() {
        RecordingLoggingEngine engine = new RecordingLoggingEngine(LogLevel.WARN);
        LoggerFactory.useEngine(engine);
        Logger log = LoggerFactory.getLogger("filtered");

        log.debug("invisible");
        log.info("also invisible");
        log.warn("kept");
        log.error("kept");

        assertEquals(2, engine.events().size());
        assertFalse(log.isDebugEnabled());
        assertTrue(log.isWarnEnabled());
    }

    @Test
    @DisplayName("placeholders are formatted only when the level is enabled")
    void parameterizedMessages() {
        RecordingLoggingEngine engine = new RecordingLoggingEngine(LogLevel.INFO);
        LoggerFactory.useEngine(engine);

        LoggerFactory.getLogger("fmt").info("order {} for {}", "4417", "Bora");

        assertEquals("order 4417 for Bora", engine.events().getFirst().message());
    }

    @Test
    @DisplayName("the guard exists so an expensive message is never built for nothing")
    void guardedLogging() {
        RecordingLoggingEngine engine = new RecordingLoggingEngine(LogLevel.ERROR);
        LoggerFactory.useEngine(engine);
        Logger log = LoggerFactory.getLogger("guard");

        // The reason isXxxEnabled is on the facade at all: the caller can skip work the
        // subsystem would only throw away.
        assertFalse(log.isInfoEnabled());
        if (log.isInfoEnabled()) {
            log.info(expensive());
        }
        assertEquals(0, engine.events().size());
    }

    private static String expensive() {
        throw new AssertionError("must not be evaluated");
    }

    @Test
    @DisplayName("swapping the backend changes nothing in the calling code")
    void theBackendIsInterchangeable() {
        Logger log = LoggerFactory.getLogger("swap");   // obtained before either binding

        RecordingLoggingEngine first = new RecordingLoggingEngine(LogLevel.INFO);
        LoggerFactory.useEngine(first);
        LoggerFactory.getLogger("swap").info("to the recorder");
        assertEquals(1, first.events().size());

        LoggerFactory.useEngine(new JulLoggingEngine());
        LoggerFactory.getLogger("swap").info("to java.util.logging");
        assertEquals(1, first.events().size(), "the old backend saw nothing more");

        assertTrue(Logger.class.isInterface(), "and the caller only ever named this");
    }

    @Test
    @DisplayName("the facade adds no logging of its own — it only routes")
    void theFacadeIsThin() {
        // Unlike the notification facade, this one holds no policy: every decision belongs
        // to the engine behind it. That is what makes the binding swappable at deployment.
        assertEquals(0, java.util.Arrays.stream(FacadeLogger.class.getDeclaredFields())
                .filter(f -> f.getType() == LogLevel.class).count());
    }
}
