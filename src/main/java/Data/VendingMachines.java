package Data;

import utils.FileRead;

import java.util.ArrayList;

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

        for (VendingMachine machine : vendingMachines)
            System.out.println(machine.location);
    }

    // TODO: return item if it matches a specific id
    public VendingMachine getItemById(int id) {
        return new VendingMachine("1,bar,baz");
    }

    // TODO: return item(s) if it matches a specific location
    public ArrayList<VendingMachine> getItemsByLocation(Location location) {
       return new ArrayList<>();
    }
}
