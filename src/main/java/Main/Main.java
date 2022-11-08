package Main;

public class Main {
    public static void main(String[] args) {
        VendingMachines vendingMachineData = new VendingMachines("data/data.csv");
        vendingMachineData.getVendingMachineById(1).printInventory();
        System.out.println();
        vendingMachineData.getVendingMachineById(2).printInventory();
        System.out.println();
        vendingMachineData.getVendingMachineById(1).checkExpirations();
        System.out.println();
        vendingMachineData.getVendingMachineById(2).checkExpirations();
    }
}
