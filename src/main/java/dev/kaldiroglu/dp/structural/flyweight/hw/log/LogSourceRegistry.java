package dev.kaldiroglu.dp.structural.flyweight.hw.log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <b>FlyweightFactory</b> for call sites. Thread-safe, because logging is.
 *
 * <p>The intrinsic state is the whole of the call site, so the whole of it is in the key. A
 * registry keyed on the class name alone would hand a line from {@code save()} the identity
 * of {@code load()} and the stack in the log would be a lie.</p>
 */
public class LogSourceRegistry {

    private final Map<String, LogSource> sources = new ConcurrentHashMap<>();

    public LogSource get(String logger, String className, String methodName, String fileName) {
        String key = logger + "|" + className + "|" + methodName + "|" + fileName;
        return sources.computeIfAbsent(key,
                k -> new LogSource(logger, className, methodName, fileName));
    }

    public int size() {
        return sources.size();
    }
}
