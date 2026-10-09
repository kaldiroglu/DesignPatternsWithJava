package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.hw.maintenance;

/** A maintenance request: its kind, a title, and the estimated effort in days. */
public record Request(Kind kind, String title, int days) {

    public enum Kind { BUG, UI_CHANGE, IMPROVEMENT, PROJECT }
}
