package dev.kaldiroglu.dp.creational.ch04.computer.solution.solution0;

import dev.kaldiroglu.dp.creational.ch04.computer.solution.solution0.domain.*;

public class BareBoneComputerBuilder implements ComputerBuilder{

    @Override
    public BareBoneComputer build() {
        BareBoneComputer computer = new BareBoneComputer("Bare-Bone Computer");

        CPU cpu = new CPU();
        computer.setCpu(cpu);

        RAM ram = new RAM();
        computer.setRam(ram);

        HardDrive hd = new HardDrive();
        computer.setHd(hd);

        return computer;
//        return new Computer("Bare-Bone Computer", new CPU(), new RAM(), new HardDrive());
    }
}
