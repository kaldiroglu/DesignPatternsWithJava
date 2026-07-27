package dev.kaldiroglu.dp.structural.bridge.file;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * The Abstraction: what a department does with its documents.
 * <p>
 * It holds a {@link FileProvider} and never asks which one. The operations here are composed
 * from the provider's primitives, and each department's retention rule lives in a subclass
 * rather than in a provider — which is why adding a fourth store costs one class and touches
 * no rule.
 */
public abstract class FileManager {

    protected FileProvider provider;

    protected FileManager(FileProvider provider) {
        this.provider = Objects.requireNonNull(provider);
    }

    /** Bridge, not Strategy: the store can be changed on a manager that already exists. */
    public void setProvider(FileProvider provider) {
        this.provider = Objects.requireNonNull(provider);
    }

    public String read(String path) {
        return new String(provider.read(provider.open(path)), StandardCharsets.UTF_8);
    }

    /** Stores a new version and then applies this department's retention rule. */
    public int save(String path, String content) {
        String handle = provider.open(path);
        int version = provider.write(handle, content.getBytes(StandardCharsets.UTF_8));
        applyRetention(handle);
        return version;
    }

    public List<Integer> versions(String path) {
        return provider.versions(provider.open(path));
    }

    /** How long this department is required to keep old versions. */
    protected abstract void applyRetention(String handle);

    /** How many versions this department keeps, for the demo to print. */
    public abstract int retainedVersions();
}
