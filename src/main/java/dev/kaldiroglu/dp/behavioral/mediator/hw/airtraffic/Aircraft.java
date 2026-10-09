package dev.kaldiroglu.dp.behavioral.mediator.hw.airtraffic;

/** A <b>Colleague</b>: an aircraft that wants to land or take off. It knows only the tower. */
public final class Aircraft {

    private final String callSign;
    private final String intent;
    private final ControlTower tower;

    public Aircraft(String callSign, String intent, ControlTower tower) {
        this.callSign = callSign;
        this.intent = intent;
        this.tower = tower;
    }

    public void request() {
        tower.request(this);
    }

    public void clear() {
        tower.runwayClear(this);
    }

    public String callSign() {
        return callSign;
    }

    public String intent() {
        return intent;
    }
}
