package Main;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        VendingMachines vendingMachines = new VendingMachines("data/v_data.csv");
        VendingMachine currentVendingMachine = vendingMachines.getVendingMachineById(1);

        //for (String[] item : currentVendingMachine.checkExpirations()) {
        //    System.out.println(Arrays.toString(item));
        //}
        currentVendingMachine.printInventory(15);
        int[][] itemsToRemove = new int[][] {
            {0, 0, 0},
                {0, 0, 1},
                {0, 0, 2},
                {0, 0, 14},
        };
        currentVendingMachine.removeMultItems(itemsToRemove);
        System.out.println();
        currentVendingMachine.printInventory(15);
    }
}
