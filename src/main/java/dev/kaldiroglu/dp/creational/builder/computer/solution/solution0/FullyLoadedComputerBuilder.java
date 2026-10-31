package dev.kaldiroglu.dp.creational.ch04.computer.solution.solution0;

import dev.kaldiroglu.dp.creational.ch04.computer.solution.solution0.domain.*;

public class FullyLoadedComputerBuilder extends BareBoneComputerBuilder {

    @Override
    public FullyLoadedComputer build() {
//        FullyLoadedComputer computer = new FullyLoadedComputer("Fully-Loaded Computer");
//        CPU cpu = new CPU();
//        computer.setCpu(cpu);
//
//        RAM ram = new RAM();
//        computer.setRam(ram);
//
//        HardDrive hd = new HardDrive();
//        computer.setHd(hd);
//
//        GraphicCard graphicCard = new GraphicCard();
//        computer.setGraphicCard(graphicCard);
//
//        Display display = new Display();
//        computer.setDisplay(display);

        FullyLoadedComputer computer = (FullyLoadedComputer) super.build();

        Keyboard keyboard = new Keyboard();
        computer.setKeyboard(keyboard);

        Mouse mouse = new Mouse();
        computer.setMouse(mouse);

        return computer;
//        return new FullyLoadedComputer("Fully-Loaded Computer", new CPU(), new RAM(), new HardDrive(), new GraphicCard(), new Display(), new Mouse(), new Keyboard());
    }
}