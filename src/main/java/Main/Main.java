package Main;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        VendingMachines vendingMachines = new VendingMachines("data/data.csv");
        VendingMachine currentVendingMachine = vendingMachines.getVendingMachineById(1);
    }
}
