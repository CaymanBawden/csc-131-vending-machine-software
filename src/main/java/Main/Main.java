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
        VendingMachine machine = machines.getVendingMachineById(1);
        System.out.println(machine.inventory.toString());
        System.out.println(machine.toString());
    }
}
