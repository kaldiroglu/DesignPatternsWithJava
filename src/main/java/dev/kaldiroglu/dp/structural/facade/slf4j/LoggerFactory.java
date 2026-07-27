package dev.kaldiroglu.dp.structural.facade.slf4j;

/**
 * <b>Facade entry point</b> -- the factory that hands out {@link Logger}s, exactly
 * like {@code org.slf4j.LoggerFactory}. It also hides the <em>binding</em>: which
 * {@link LoggingEngine} backend is in use. Real SLF4J discovers that backend from
 * the classpath via {@code ServiceLoader}; here {@link #useEngine} stands in for
 * that wiring, normally fixed once at application start-up.
 */
public final class LoggerFactory {

    // Default binding, used if the application never configures one -- analogous to
    // slf4j-simple. A backend is normally selected just once, at start-up.
    private static volatile LoggingEngine engine = new ConsoleLoggingEngine(LogLevel.INFO);

    private LoggerFactory() {
    }

    /** Selects the subsystem backend that newly created loggers will use. */
    public static void useEngine(LoggingEngine backend) {
        engine = backend;
    }

    /** Returns a facade logger with the given name. */
    public static Logger getLogger(String name) {
        return new FacadeLogger(name, engine);
    }

    /** Returns a facade logger named after the given class -- the common idiom. */
    public static Logger getLogger(Class<?> type) {
        return getLogger(type.getName());
    }
}
