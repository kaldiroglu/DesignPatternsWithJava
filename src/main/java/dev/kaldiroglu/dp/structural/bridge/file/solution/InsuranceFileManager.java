package dev.kaldiroglu.dp.structural.bridge.file.solution;

import java.util.List;

/**
 * A RefinedAbstraction: insurance keeps only the current version and the one before it.
 * <p>
 * Named to match {@link FinanceFileManager}; it was previously {@code InsuranceManager},
 * which broke the solution of the name and made the two look like different kinds of thing.
 */
public class InsuranceFileManager extends FileManager {

    private static final int KEEP = 2;

    public InsuranceFileManager(FileProvider provider) {
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
