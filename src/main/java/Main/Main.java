package Main;

import UI.CustomerInterface.CustomerMainPanel;
import UI.ManagerInterface.ManagerMainPanel;
import UI.RestockerInterface.RestockerMainPanel;
import VendingMachine.VendingMachine;
import VendingMachine.VendingMachines;

public class Main {
    public static void main(String[] args) {
//        CustomerMainPanel customerMainPanel = new CustomerMainPanel();
//        customerMainPanel.show();

        ManagerMainPanel managerMainPanel = new ManagerMainPanel();
        managerMainPanel.show();

//        RestockerMainPanel restockerMainPanel = new RestockerMainPanel();
//        restockerMainPanel.show();

//        VendingMachines machines = new VendingMachines("data/data.csv");
//        VendingMachine machine = machines.getVendingMachineById(1);
//        System.out.println(machine.toString());
    }
}
