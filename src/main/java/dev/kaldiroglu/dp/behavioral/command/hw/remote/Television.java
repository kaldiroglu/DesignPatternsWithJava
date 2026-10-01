package dev.kaldiroglu.dp.behavioral.command.hw.remote;

/** The <b>Receiver</b>. It keeps its channel and volume while it is off. */
public final class Television {

    public static final int MAX_VOLUME = 10;

    private boolean on;
    private int channel = 1;
    private int volume = 5;

    public void turnOn() { on = true; }

    public void turnOff() { on = false; }

    public boolean isOn() { return on; }

    public int channel() { return channel; }

    public void selectChannel(int channel) { this.channel = channel; }

    public int volume() { return volume; }

    /** Answers whether the volume actually changed: at the top it does not. */
    public boolean volumeUp() {
        if (volume == MAX_VOLUME) {
            return false;
        }
        volume++;
        return true;
    }

    /** Answers whether the volume actually changed: at zero it does not. */
    public boolean volumeDown() {
        if (volume == 0) {
            return false;
        }
        volume--;
        return true;
    }
}
