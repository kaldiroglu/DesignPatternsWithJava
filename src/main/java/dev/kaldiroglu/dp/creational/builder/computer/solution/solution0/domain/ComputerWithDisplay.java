package dev.kaldiroglu.dp.creational.ch04.computer.solution.solution0.domain;

public class ComputerWithDisplay extends BareBoneComputer {
    protected GraphicCard graphicCard;
    protected Display display;

    public ComputerWithDisplay(String name) {
        super(name);
    }

    public ComputerWithDisplay(String name, CPU cpu, RAM ram, HardDrive hd, GraphicCard graphicCard, Display display) {
        super(name, cpu, ram, hd);
    }

    public void start(){
        super.start();
        graphicCard.start();
        if (display != null)
            display.start();
        System.out.println("Computer " + name + " has started.\n");
    }

    public GraphicCard getGraphicCard() {
        return graphicCard;
    }

    public void setGraphicCard(GraphicCard graphicCard) {
        this.graphicCard = graphicCard;
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }
}
