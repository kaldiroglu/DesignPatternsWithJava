package dev.kaldiroglu.dp.creational.ch04.computer.solution.solution1;


import dev.kaldiroglu.dp.creational.ch04.computer.domain.*;

public class Test {

	public static void main(String[] args) {

		ComputerBuilderI builder = new ComputerBuilder();
		Computer computer = builder.buildComputer();
		computer.start();

		RAM ram = builder.buildRAM();
		Display display = builder.buildDisplay();

	}
}
