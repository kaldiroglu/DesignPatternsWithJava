package dev.kaldiroglu.dp.creational.ch04.computer.solution.solution0;

import dev.kaldiroglu.dp.creational.ch04.computer.solution.solution0.domain.Computer;

public class ComputerTechnician {
    private ComputerBuilder bareboneComputerBuilder;
    private ComputerBuilder computerWithDisplayBuilder;
    private ComputerBuilder fullyLoadedComputerBuilder;

    public ComputerTechnician() {
        bareboneComputerBuilder = new BareBoneComputerBuilder();
        computerWithDisplayBuilder = new ComputerWithDisplayBuilder();
        fullyLoadedComputerBuilder = new FullyLoadedComputerBuilder();
    }

    public Computer produce(String computerType) {
        Computer computer = switch (computerType) {
            case "bare-bone" -> bareboneComputerBuilder.build();
            case "computer-with-display" -> computerWithDisplayBuilder.build();
            case "fully-loaded" -> fullyLoadedComputerBuilder.build();
            default -> throw new IllegalStateException("Unexpected value: " + computerType);
        };
        computer.start();
        return computer;
    }

    public static void main(String[] args) {
        ComputerTechnician computerTechnician = new ComputerTechnician();
        Computer computer = computerTechnician.produce("fully-loaded");
    }
}
