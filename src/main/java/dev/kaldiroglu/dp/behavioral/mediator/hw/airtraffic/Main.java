package dev.kaldiroglu.dp.behavioral.mediator.hw.airtraffic;

/**
 * Three aircraft ask the tower for the runway. The tower gives it to one at a time and
 * calls the next one when the runway is clear.
 */
public final class Main {

    public static void main(String[] args) {
        ControlTower tower = new ControlTower();
        Aircraft tk1 = new Aircraft("TK1", "land", tower);
        Aircraft pc2 = new Aircraft("PC2", "land", tower);
        Aircraft aj3 = new Aircraft("AJ3", "take off", tower);

        tk1.request();
        pc2.request();
        aj3.request();
        tk1.clear();
        pc2.clear();

        tower.log().forEach(System.out::println);
    }
}
