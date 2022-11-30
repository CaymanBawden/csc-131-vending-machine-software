package Main;

import InventoryManager.Item;
import VendingMachine.VendingMachine;
import VendingMachine.VendingMachines;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        VendingMachines vendingMachines = new VendingMachines("data/data.csv");
        ArrayList<VendingMachine> machines = vendingMachines.getVendingMachinesByState("Georgia");
        for (VendingMachine machine : machines) {
            machine.print();
        }
    }
}
