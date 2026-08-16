package dev.kaldiroglu.dp.structural.bridge.file.problem;

/**
 * One axis of the problem: who owns the document, and therefore how long it is kept.
 * <p>
 * Finance must keep an audit trail; insurance must not keep more than it needs. The two
 * rules pull in opposite directions, which is why neither can be a property of a store.
 */
public enum Department {

    /** Seven years of audit trail, which here is the last five versions. */
    FINANCE(5),

    /** Data minimization: no more than the last two versions may be retained. */
    INSURANCE(2);

    private final int retainedVersions;

    Department(int retainedVersions) {
        this.retainedVersions = retainedVersions;
    }

    public int retainedVersions() {
        return retainedVersions;
    }
}
