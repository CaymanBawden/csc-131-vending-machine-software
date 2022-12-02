package Main;

import UI.CustomerInterface.CustomerMainPanel;
import UI.ManagerInterface.ManagerMainPanel;
import VendingMachine.VendingMachine;
import VendingMachine.VendingMachines;

public class Main {
    public static void main(String[] args) {
//        CustomerMainPanel panel = new CustomerMainPanel();
//        panel.show();
//        ManagerMainPanel panel = new ManagerMainPanel();
//        panel.show();
        VendingMachines machines = new VendingMachines("data/data.csv");
        VendingMachine currentVendingMachine = machines.getVendingMachineById(1);
        String vendingString = currentVendingMachine.inventory.toString();
        System.out.println(vendingString);
        currentVendingMachine.inventory.getItem(5,4, 1).print();
    }
}
