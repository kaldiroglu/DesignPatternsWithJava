package dev.kaldiroglu.dp.behavioral.command.hw.remote;

/**
 * Shows a remote whose undo puts back exactly what was there, even when a press changed
 * nothing.
 */
public class Main {

    public static void main(String[] args) {
        Television tv = new Television();
        RemoteControl remote = RemoteControl.standardFor(tv);

        remote.press("on");
        remote.press("7");
        System.out.println("On, then channel 7. Channel: " + tv.channel());
        remote.undo();
        System.out.println("Undo. Channel: " + tv.channel()
                + " (the command remembered the channel before)");

        for (int i = 0; i < 6; i++) {
            remote.press("volume+");
        }
        System.out.println("Volume up six times from 5. Volume: " + tv.volume()
                + " (the top is " + Television.MAX_VOLUME + ")");
        remote.undo();
        System.out.println("Undo the last press, which changed nothing. Volume: " + tv.volume());
        remote.undo();
        System.out.println("Undo once more. Volume: " + tv.volume());

        remote.press("off");
        remote.undo();
        System.out.println("Off, then undo. The television is on: " + tv.isOn());
    }
}
