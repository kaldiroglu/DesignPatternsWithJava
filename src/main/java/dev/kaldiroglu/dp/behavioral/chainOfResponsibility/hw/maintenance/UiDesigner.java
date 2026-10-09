package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.hw.maintenance;

/** Takes UI changes. */
public final class UiDesigner extends Developer {

    public UiDesigner(String name) {
        super(name);
    }

    @Override
    protected boolean suits(Request request) {
        return request.kind() == Request.Kind.UI_CHANGE;
    }
}
