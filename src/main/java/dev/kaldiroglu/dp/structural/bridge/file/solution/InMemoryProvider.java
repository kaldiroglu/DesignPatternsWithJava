package dev.kaldiroglu.dp.structural.bridge.file.solution;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Shared behavior for the three providers in this package.
 * <p>
 * Each vendor really would call its own SDK here. They are simulated in memory so the example
 * runs anywhere and so a test can assert what was stored — the same reason
 * {@code notifications.domain.Transports} exists.
 */
public abstract class InMemoryProvider implements FileProvider {

    private final String name;
    private final Map<String, NavigableMap<Integer, byte[]>> documents = new LinkedHashMap<>();
    private final Map<String, Integer> lastVersion = new LinkedHashMap<>();

    protected InMemoryProvider(String name) {
        this.name = name;
    }

    @Override
    public final String name() {
        return name;
    }

    @Override
    public String open(String path) {
        String handle = name + ":" + path;
        documents.computeIfAbsent(handle, key -> new TreeMap<>());
        return handle;
    }

    @Override
    public byte[] read(String handle) {
        NavigableMap<Integer, byte[]> stored = contentsOf(handle);
        if (stored.isEmpty()) {
            throw new IllegalStateException("nothing stored at " + handle);
        }
        return stored.lastEntry().getValue().clone();
    }

    @Override
    public int write(String handle, byte[] content) {
        int version = lastVersion.merge(handle, 1, Integer::sum);
        contentsOf(handle).put(version, content.clone());
        return version;
    }

    @Override
    public List<Integer> versions(String handle) {
        return new ArrayList<>(contentsOf(handle).keySet());
    }

    @Override
    public void deleteVersion(String handle, int version) {
        if (contentsOf(handle).remove(version) == null) {
            throw new IllegalArgumentException("no version " + version + " at " + handle);
        }
    }

    private NavigableMap<Integer, byte[]> contentsOf(String handle) {
        NavigableMap<Integer, byte[]> stored = documents.get(handle);
        if (stored == null) {
            throw new IllegalStateException("open() first: " + handle);
        }
        return stored;
    }
}
