package dev.kaldiroglu.dp.structural.bridge.file.solution;

import java.util.List;

/**
 * A RefinedAbstraction: finance keeps seven years, which here is the last five versions.
 * <p>
 * The rule is written once and is correct on every store, present and future.
 */
public class FinanceFileManager extends FileManager {

    private static final int KEEP = 5;

    public FinanceFileManager(FileProvider provider) {
        super(provider);
    }

    @Override
    public int retainedVersions() {
        return KEEP;
    }

    @Override
    protected void applyRetention(String handle) {
        List<Integer> versions = provider.versions(handle);
        for (int i = 0; i < versions.size() - KEEP; i++) {
            provider.deleteVersion(handle, versions.get(i));
        }
    }
}
