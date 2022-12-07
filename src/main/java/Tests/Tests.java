package Tests;

import VendingMachine.VendingMachine;
import VendingMachine.VendingMachines;
import java.util.Objects;

public class Tests { //certain tests need their inputs changed if you change from vending machine ID : 1
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
        if(machine.location.address.contains("Alexander"))
            success("Location is 527 Alexander Highway");

        // Purposely gives an incorrect address : is meant to fail
        if(machine.location.address.contains("Smith") == false)
            fail("Not correct address");

        //checks if vending machine is empty
        if (machines.vendingMachines.isEmpty())
            fail("Vending Machine is empty");

        if (machines.vendingMachines.isEmpty() == false)
            success("Vending Machine is not empty");

        //checks the price of an item in a particular row and column
        if(machine.inventory.getPrice(0,0) == 1.00)
            success("Price of Item in 1A is $1.00");

        //Purchases an item and checks if the correct message gets displayed (if you change the data set this breaks.)
        if(Objects.equals(machine.purchaseItem(0, 0, 1.00), "Dispensing item below and change of: $0.0"))
            success("Dispensing item below");

        if(Objects.equals(machine.purchaseItem(0, 1, 2.00), "Dispensing item below and change of: $1.0"))
            success("Dispensing item below and change of: $1.0");

        // checks quantity of items in a particular row and column
        if(machine.inventory.getQuantity(0,0) == 5)
            success("Quantity of Items in 1A is 5");

        //checks if the queue has items
        if(machine.queuedItems.queuedItems.isEmpty() == true)
            success("No Items in Queue");

        //Just test print statements
        System.out.println(machine.inventory.toString());
        System.out.println(machine.toString());
        System.out.println(machine.inventory.getExpiredItems());
        System.out.println(machine.inventory.getItem(0,0,1));
        System.out.println(machine.purchaseItem(0,1,2.00));
        System.out.println(machine.inventory.getPrice(0,0));
        System.out.println(machine.inventory.getQuantity(0,0));

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
