package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.hw.maintenance;

/** Takes improvements of up to ten days. A larger one goes on to the project team. */
public final class FeatureDeveloper extends Developer {

    public FeatureDeveloper(String name) {
        super(name);
    }

    @Override
    protected boolean suits(Request request) {
        return request.kind() == Request.Kind.IMPROVEMENT && request.days() <= 10;
    }
}
