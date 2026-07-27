package dev.kaldiroglu.dp.structural.facade.notification.solution1;

import java.util.HashMap;
import java.util.Map;

/**
 * Aggregated result from a notify() call — which channels succeeded or failed.
 */
public class NotificationResult {
    private final Map<String, Boolean> results;

    public NotificationResult() {
        this.results = new HashMap<>();
    }

    public void record(String channel, boolean success) {
        results.put(channel, success);
    }

    public boolean allSucceeded() {
        return results.values().stream().allMatch(Boolean::booleanValue);
    }

    public Map<String, Boolean> getResults() {
        return Map.copyOf(results);
    }

    @Override
    public String toString() {
        return "NotificationResult" + results;
    }
}
