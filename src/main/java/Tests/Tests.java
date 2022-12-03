package Tests;

import VendingMachine.VendingMachine;
import VendingMachine.VendingMachines;

public class Tests {
    public static boolean failed = false;

    public static void main(String[] args) {
        VendingMachines machines = new VendingMachines("data/data.csv");
        VendingMachine machine = machines.getVendingMachineById(1);
        String purchase = machine.purchaseItem(0, 0, 0);

        if (machines.vendingMachines.size() == 0)
            fail("No vending machines loaded");

        if (purchase.contains("Please insert: $"))
            success("Purchase with 0 dollars worked");

        if (failed)
            System.exit(1);
    }

    public static void fail(String reason) {
        failed = true;
        System.out.println(ConsoleColors.RED + "FAILED: " + reason + ConsoleColors.RESET);
    }

    public static void success(String reason) {
        System.out.println(ConsoleColors.GREEN + "SUCCESS: " + reason + ConsoleColors.RESET);
    }
}
