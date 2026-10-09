package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.hw.maintenance;

/** Takes projects, and improvements too large for one developer. */
public final class ProjectTeam extends Developer {

    public ProjectTeam(String name) {
        super(name);
    }

    @Override
    protected boolean suits(Request request) {
        return request.kind() == Request.Kind.PROJECT || request.kind() == Request.Kind.IMPROVEMENT;
    }
}
