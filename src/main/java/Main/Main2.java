package Main;

import VendingMachine.VendingMachine;
import VendingMachine.VendingMachines;

public class Main2 {
    public static void main(String[] args) {
        System.out.println("Hello World");
        VendingMachines machines = new VendingMachines("data/data.csv");
        VendingMachine machine = machines.getVendingMachineById(1);

        machine.purchaseItem(4, 4, 2);


    }
}
