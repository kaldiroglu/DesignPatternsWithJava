package dev.kaldiroglu.dp.structural.flyweight.hw.log;

import java.util.Objects;

/**
 * <b>ConcreteFlyweight</b> — where a log line came from: which logger, which class, which
 * method, which file.
 *
 * <p>An application emits millions of lines from a few hundred call sites. The line's text
 * and timestamp are different every time; the call site is not.</p>
 */
public final class LogSource {

    private final String logger;
    private final String className;
    private final String methodName;
    private final String fileName;

    LogSource(String logger, String className, String methodName, String fileName) {
        this.logger = Objects.requireNonNull(logger);
        this.className = Objects.requireNonNull(className);
        this.methodName = Objects.requireNonNull(methodName);
        this.fileName = Objects.requireNonNull(fileName);
    }

    public String logger() {
        return logger;
    }

    public String className() {
        return className;
    }

    public String methodName() {
        return methodName;
    }

    public String fileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return className + "." + methodName + "(" + fileName + ")";
    }
}
