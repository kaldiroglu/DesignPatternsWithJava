package dev.kaldiroglu.dp.creational.prototype.gof.graphic;

public class MusicalNote extends Graphic{

    private final String note;
    private final int octave;
    private final int duration;

    public MusicalNote(String name, String note, int octave, int duration) {
        super(name);
        this.note = note;
        this.octave = octave;
        this.duration = duration;
    }


    @Override
    public void draw(Position position) {

    }

    @Override
    public Graphic clone() {
        return new MusicalNote(name, note, octave, duration);
    }
}
