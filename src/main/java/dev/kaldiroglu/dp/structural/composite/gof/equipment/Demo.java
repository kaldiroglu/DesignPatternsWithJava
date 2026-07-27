package dev.kaldiroglu.dp.structural.composite.gof.equipment;

/**
 * Client of the equipment Composite — the assembly code of GoF p. 173.
 *
 * <p>The book's client is reproduced almost line for line:</p>
 *
 * <pre>
 * Cabinet* cabinet = new Cabinet("PC Cabinet");
 * Chassis* chassis = new Chassis("PC Chassis");
 * cabinet-&gt;Add(chassis);
 * Bus* bus = new Bus("MCA Bus");
 * bus-&gt;Add(new Card("16Mbs Token Ring"));
 * chassis-&gt;Add(bus);
 * chassis-&gt;Add(new FloppyDisk("3.5in Floppy"));
 * cout &lt;&lt; "The net price is " &lt;&lt; chassis-&gt;NetPrice() &lt;&lt; endl;
 * </pre>
 */
public final class Demo {

    public static void main(String[] args) {
        Cabinet cabinet = new Cabinet("PC Cabinet");
        Chassis chassis = new Chassis("PC Chassis");
        cabinet.add(chassis);

        Bus bus = new Bus("MCA Bus");
        bus.add(new Card("16Mbs Token Ring"));
        chassis.add(bus);
        chassis.add(new FloppyDisk("3.5in Floppy"));

        System.out.println("--- The assembled equipment ---");
        printTree(cabinet, "");

        System.out.println();
        System.out.println("--- One call, answered by the whole subtree ---");
        System.out.println("The net price of the chassis is " + chassis.netPrice());
        System.out.println("The net price of the cabinet is " + cabinet.netPrice());
        System.out.println("The cabinet draws " + cabinet.power() + " W");
        System.out.println("The cabinet's discount price is " + cabinet.discountPrice());

        System.out.println();
        System.out.println("--- The same calls work on a single leaf ---");
        Equipment lone = new Card("Ethernet");
        System.out.println(lone.name() + ": net " + lone.netPrice()
                + ", discount " + lone.discountPrice() + ", " + lone.power() + " W");
    }

    /**
     * Walks an arbitrary equipment tree. The recursion needs no type test — a
     * leaf simply yields an empty iteration and the walk stops there.
     */
    private static void printTree(Equipment equipment, String indent) {
        System.out.printf("%s%s (net %s, %d W)%n",
                indent, equipment.name(), equipment.netPrice(), equipment.power());
        for (Equipment part : equipment) {
            printTree(part, indent + "    ");
        }
    }
}
