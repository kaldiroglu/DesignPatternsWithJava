package dev.kaldiroglu.dp.creational.factoryMethod.gof.figure;

import java.util.List;

/** GoF's parallel hierarchies: the client asks a figure for a manipulator and never names one. */
public class Main {

    public static void main(String[] args) {
        for (Figure figure : List.of(new LineFigure(), new TextFigure()))
            figure.createManipulator().drag();
    }
}
