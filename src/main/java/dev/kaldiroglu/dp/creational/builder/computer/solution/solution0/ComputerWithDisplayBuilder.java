package dev.kaldiroglu.dp.creational.ch04.computer.solution.solution0;

import dev.kaldiroglu.dp.creational.ch04.computer.solution.solution0.domain.*;

public class ComputerWithDisplayBuilder extends BareBoneComputerBuilder{

    @Override
    public ComputerWithDisplay build() {
//        ComputerWithDisplay computer = new ComputerWithDisplay("Computer with Display");
//
//        CPU cpu = new CPU();
//        computer.setCpu(cpu);
//
//        RAM ram = new RAM();
//        computer.setRam(ram);
//
//        HardDrive hd = new HardDrive();
//        computer.setHd(hd);

        ComputerWithDisplay computer = (ComputerWithDisplay) super.build();

        GraphicCard graphicCard = new GraphicCard();
        computer.setGraphicCard(graphicCard);

        Display display = new Display();
        computer.setDisplay(display);

        return computer;
//        return new ComputerWithDisplay("Computer with Display", new CPU(), new RAM(), new HardDrive(), new GraphicCard(), new Display());
    }
}
