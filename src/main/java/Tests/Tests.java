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

        //checks if prompt works
        if (purchase.contains("Please insert: $"))
            success("Purchase with 0 dollars worked");
        //checks if machine is online
        if(machine.online == true)
            success("Vending machine online");
        //checks the vending machine location based on input
        if(machine.location.address.contains("Shaffer"))
            success("Location is 8018 Shaffer Locks Apt. 172");
//        if(machine.location.address.contains("Smith")) //not working as intended
//            success("Wrong tester");
        //checks if vending machine is empty
        if (machines.vendingMachines.isEmpty()== true)
            fail("Vending Machine is empty");
        //checks the price of an item in a particular row and column
        if(machine.inventory.getPrice(0,0) == 1.00)
            success("Price of Item in 1A is $1");


        //working on still, second portion of change dispensing is issue.
        System.out.println(machine.purchaseItem(0,1,2.00));
        if(machine.purchaseItem(0,1,1.5) == "Check below for item")
            success("Item Purchased");

        if(machine.purchaseItem(0,1,2.00) == "Dispensing item below and change of: $0.5")
            success("Item Purchased, change dispensed");

        // to be uncommented later
//        System.out.println(machine.inventory.toString());
//        System.out.println(machine.toString());
//        System.out.println(machine.inventory.getExpiredItems());

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
