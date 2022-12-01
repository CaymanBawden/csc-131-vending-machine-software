package VendingMachine;

import utils.FileRead;

import java.util.ArrayList;
import java.util.Objects;

public class VendingMachines {
    public ArrayList<VendingMachine> vendingMachines = new ArrayList<>();

    public VendingMachines(String filepath) {
        ArrayList<String> lines = new ArrayList<>();
        FileRead fr = new FileRead(filepath);
        // used so every line is looped through
        int numLines = fr.getNumLines();

        // populate lines with all lines in the csv file
        for (int i = 1; i < numLines; i++)
            lines.add(fr.getLine(i).trim());

        for (String line : lines)
            vendingMachines.add(new VendingMachine(line));
    }

    public void saveData() {
        System.out.println("Saving Data");
    }

    public VendingMachine getVendingMachineById(int id) {
        for (int i = 0; i < vendingMachines.size(); i++) {
            if (vendingMachines.get(i).id == id)
                return vendingMachines.get(i);
        }
        return null;
    }

    public ArrayList<VendingMachine> getVendingMachinesByZipCode(int zipCode) {
        ArrayList<VendingMachine> zipCodeMachines = new ArrayList<>();
        for (VendingMachine machine : vendingMachines) {
            if (machine.location.zipCode == zipCode)
                zipCodeMachines.add(machine);
        }
        return zipCodeMachines;
    }

    public ArrayList<VendingMachine> getVendingMachinesByAddress(String address) {
        ArrayList<VendingMachine> addressMachines = new ArrayList<>();
        for (VendingMachine machine : vendingMachines) {
            if (Objects.equals(machine.location.address, address))
                addressMachines.add(machine);
        }
        return addressMachines;
    }

    public ArrayList<VendingMachine> getVendingMachinesByCity(String city) {
        ArrayList<VendingMachine> cityMachines = new ArrayList<>();
        for (VendingMachine machine : vendingMachines) {
            if (Objects.equals(machine.location.city, city))
                cityMachines.add(machine);
        }
        return cityMachines;
    }

    public ArrayList<VendingMachine> getVendingMachinesByState(String state) {
        ArrayList<VendingMachine> stateMachines = new ArrayList<>();
        for (VendingMachine machine : vendingMachines) {
            if (Objects.equals(machine.location.state, state))
                stateMachines.add(machine);
        }
        return stateMachines;
    }
}
