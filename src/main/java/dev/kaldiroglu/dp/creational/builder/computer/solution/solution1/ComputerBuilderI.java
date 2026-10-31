package dev.kaldiroglu.dp.creational.ch04.computer.solution.solution1;

import dev.kaldiroglu.dp.creational.ch04.computer.domain.*;

public interface ComputerBuilderI {

	public RAM buildRAM();

	public CPU buildCPU();

	public HardDrive buildHardDrive();
	
	public GraphicCard buildGraphicCard();

	public Display buildDisplay();

	public Computer buildComputer();

}
