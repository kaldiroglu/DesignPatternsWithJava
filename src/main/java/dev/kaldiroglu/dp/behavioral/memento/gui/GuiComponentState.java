package dev.kaldiroglu.dp.behavioral.memento.gui;

/** The state as one object. A record, so a saved state cannot be changed. */
public record GuiComponentState(int x, int y, int length, int width) {
}
