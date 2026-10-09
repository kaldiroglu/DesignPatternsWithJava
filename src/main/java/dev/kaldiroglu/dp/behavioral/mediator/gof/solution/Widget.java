package dev.kaldiroglu.dp.behavioral.mediator.gof.solution;

/**
 * The <b>Colleague</b>: a widget knows its director and nothing else. When something
 * happens to it, it says so: {@code changed()}.
 */
public abstract class Widget {

    private final DialogDirector director;

    protected Widget(DialogDirector director) {
        this.director = director;
    }

    protected void changed() {
        director.widgetChanged(this);
    }
}
