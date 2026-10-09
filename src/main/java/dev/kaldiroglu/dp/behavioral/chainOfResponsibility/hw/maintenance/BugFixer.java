package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.hw.maintenance;

/** Takes bugs. */
public final class BugFixer extends Developer {

    public BugFixer(String name) {
        super(name);
    }

    @Override
    protected boolean suits(Request request) {
        return request.kind() == Request.Kind.BUG;
    }
}
