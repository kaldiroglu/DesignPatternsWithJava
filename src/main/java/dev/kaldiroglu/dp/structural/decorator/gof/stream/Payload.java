package dev.kaldiroglu.dp.structural.decorator.gof.stream;

/** The text both designs write. */
public final class Payload {

    // The "aä" is deliberate. Folding ä to a creates a run of two that compression can
    // exploit only if compression runs *after* folding — so the two orders of the same
    // two decorators produce different bytes. Order is part of the design, not a detail.
    public static final String TEXT = "Rrrésumé of a naïve café —— aä ee ccc";

    private Payload() {
    }
}
