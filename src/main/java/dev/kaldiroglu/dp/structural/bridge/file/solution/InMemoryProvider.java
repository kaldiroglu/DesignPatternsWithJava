package dev.kaldiroglu.dp.structural.bridge.file.solution;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shared behavior for the three providers in this package.
 * <p>
 * Each vendor really would call its own SDK here. They are simulated in memory so the example
 * runs anywhere and so a test can assert what was stored — the same reason
 * {@code notifications.domain.Transports} exists.
 */
public abstract class InMemoryProvider implements FileProvider {

    private final String name;
    private final Map<String, List<byte[]>> documents = new LinkedHashMap<>();

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
        documents.computeIfAbsent(handle, key -> new ArrayList<>());
        return handle;
    }

    @Override
    public byte[] read(String handle) {
        List<byte[]> stored = contentsOf(handle);
        if (stored.isEmpty()) {
            throw new IllegalStateException("nothing stored at " + handle);
        }
        return stored.getLast().clone();
    }

    @Override
    public int write(String handle, byte[] content) {
        List<byte[]> stored = contentsOf(handle);
        stored.add(content.clone());
        return stored.size();
    }

    @Override
    public List<Integer> versions(String handle) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= contentsOf(handle).size(); i++) {
            numbers.add(i);
        }
        return numbers;
    }

    @Override
    public void deleteVersion(String handle, int version) {
        List<byte[]> stored = contentsOf(handle);
        if (version < 1 || version > stored.size()) {
            throw new IllegalArgumentException("no version " + version + " at " + handle);
        }
        stored.set(version - 1, new byte[0]); // tombstoned, so later numbers do not shift
    }

    private List<byte[]> contentsOf(String handle) {
        List<byte[]> stored = documents.get(handle);
        if (stored == null) {
            throw new IllegalStateException("open() first: " + handle);
        }
        return stored;
    }
}
