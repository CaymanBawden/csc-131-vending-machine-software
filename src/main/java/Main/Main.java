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
        String vendingString = currentVendingMachine.toString();
        System.out.println(vendingString);
        currentVendingMachine.purchaseItem(3, 4, 1.00);
        vendingString = currentVendingMachine.toString();
        System.out.println(vendingString);
    }
}
