package dev.kaldiroglu.dp.creational.ch04.computer.solution.solution0.domain;

public class FullyLoadedComputer extends ComputerWithDisplay {
    protected Mouse mouse;
    protected Keyboard keyboard;

    public FullyLoadedComputer(String name) {
        super(name);
    }

    public FullyLoadedComputer(String name, CPU cpu, RAM ram, HardDrive hd, GraphicCard graphicCard, Display display, Mouse mouse, Keyboard keyboard) {
        super(name, cpu, ram, hd, graphicCard, display);
    }

    @Override
    public void start(){
        super.start();
        if (keyboard != null)
            keyboard.start();
        if (mouse != null)
            mouse.start();
        System.out.println("Computer " + name + " has started.\n");
    }

    public Mouse getMouse() {
        return mouse;
    }

    public void setMouse(Mouse mouse) {
        this.mouse = mouse;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
    }
}
