package dev.kaldiroglu.dp.structural.facade.slf4j;

/**
 * Default {@link Logger} facade implementation. Every named level method is a thin
 * call onto {@link #log}, which asks the {@link LoggingEngine} subsystem whether
 * the level is enabled <em>before</em> formatting -- so a disabled level costs
 * nothing beyond the check.
 */
final class FacadeLogger implements Logger {

    private final String name;
    private final LoggingEngine engine;

    FacadeLogger(String name, LoggingEngine engine) {
        this.name = name;
        this.engine = engine;
    }

    private void log(LogLevel level, String message) {
        if (engine.isEnabled(name, level)) {
            engine.log(new LogEvent(name, level, message));
        }
    }

    private void log(LogLevel level, String format, Object[] arguments) {
        if (engine.isEnabled(name, level)) {
            // Formatting happens only when the level is on -- the SLF4J efficiency win.
            engine.log(new LogEvent(name, level, MessageFormatter.format(format, arguments)));
        }
    }

    @Override public void trace(String message) { log(LogLevel.TRACE, message); }
    @Override public void trace(String format, Object... arguments) { log(LogLevel.TRACE, format, arguments); }
    @Override public boolean isTraceEnabled() { return engine.isEnabled(name, LogLevel.TRACE); }

    @Override public void debug(String message) { log(LogLevel.DEBUG, message); }
    @Override public void debug(String format, Object... arguments) { log(LogLevel.DEBUG, format, arguments); }
    @Override public boolean isDebugEnabled() { return engine.isEnabled(name, LogLevel.DEBUG); }

    @Override public void info(String message) { log(LogLevel.INFO, message); }
    @Override public void info(String format, Object... arguments) { log(LogLevel.INFO, format, arguments); }
    @Override public boolean isInfoEnabled() { return engine.isEnabled(name, LogLevel.INFO); }

    @Override public void warn(String message) { log(LogLevel.WARN, message); }
    @Override public void warn(String format, Object... arguments) { log(LogLevel.WARN, format, arguments); }
    @Override public boolean isWarnEnabled() { return engine.isEnabled(name, LogLevel.WARN); }

    @Override public void error(String message) { log(LogLevel.ERROR, message); }
    @Override public void error(String format, Object... arguments) { log(LogLevel.ERROR, format, arguments); }
    @Override public boolean isErrorEnabled() { return engine.isEnabled(name, LogLevel.ERROR); }
}
