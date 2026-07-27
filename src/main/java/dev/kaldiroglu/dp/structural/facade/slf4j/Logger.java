package dev.kaldiroglu.dp.structural.facade.slf4j;

/**
 * <b>Facade</b> participant -- the single, simple interface an application logs
 * through. It corresponds to {@code org.slf4j.Logger}.
 *
 * <p>Application code depends only on this type (obtained from
 * {@link LoggerFactory}) and never on a concrete logging backend. That is the
 * whole point of the "Simple Logging <em>Facade</em> for Java": the messy
 * subsystem -- levels, <code>{}</code> formatting, event routing, backend I/O --
 * is hidden behind five familiar level methods.</p>
 *
 * <p>Each level offers a plain-message form, a parameterized form that expands
 * <code>{}</code> placeholders only when the level is enabled, and an
 * {@code isXxxEnabled()} guard for messages that are expensive to build.</p>
 */
public interface Logger {

    void trace(String message);
    void trace(String format, Object... arguments);
    boolean isTraceEnabled();

    void debug(String message);
    void debug(String format, Object... arguments);
    boolean isDebugEnabled();

    void info(String message);
    void info(String format, Object... arguments);
    boolean isInfoEnabled();

    void warn(String message);
    void warn(String format, Object... arguments);
    boolean isWarnEnabled();

    void error(String message);
    void error(String format, Object... arguments);
    boolean isErrorEnabled();
}
