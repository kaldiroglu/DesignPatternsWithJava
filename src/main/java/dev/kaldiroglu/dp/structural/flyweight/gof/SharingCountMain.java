package dev.kaldiroglu.dp.structural.flyweight.gof;

/**
 * Created by Claude Opus 4.8 on June 22nd 2026
 * Client demo (GoF, p. 204).
 *
 * <p>Reports how many character objects were saved by sharing.</p>
 */
public class SharingCountMain {

    public static void main(String[] args) {
        SampleDocument sample = new SampleDocument();

        System.out.println("Character occurrences in document : " + sample.occurrences);
        System.out.println("Distinct Character flyweights     : "
                + sample.factory.createdCharacterCount());
        System.out.println("Objects saved by sharing          : "
                + (sample.occurrences - sample.factory.createdCharacterCount()));
    }
}
