package dev.kaldiroglu.dp.creational.ch04.computer.solution.solution0.domain;

public class BareBoneComputer extends Computer {
    protected CPU cpu;
    protected RAM ram;
    protected HardDrive hd;

    public BareBoneComputer(String name) {
        super(name);
    }

    public BareBoneComputer(String name, CPU cpu, RAM ram, HardDrive hd) {
        this.name = name;
        this.cpu = cpu;
        this.ram = ram;
        this.hd = hd;
    }

    public void start(){
        cpu.start();
        ram.loadOSKernelImage();
        ram.loadOS();
        ram.loadDrivers();
        ram.startServices();
        hd.start();
        System.out.println("Computer " + name + " has started.\n");
    }

    public CPU getCpu() {
        return cpu;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    public RAM getRam() {
        return ram;
    }

    public void setRam(RAM ram) {
        this.ram = ram;
    }

    public HardDrive getHd() {
        return hd;
    }

    public void setHd(HardDrive hd) {
        this.hd = hd;
    }


}
