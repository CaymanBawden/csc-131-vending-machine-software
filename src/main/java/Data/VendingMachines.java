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
            vendingMachines.add(new VendingMachine(line, filepath));
    }

    // TODO: return item if it matches a specific id
    public VendingMachine getVendingMachineById(int id) {
        for(int i = 0; i < vendingMachines.size(); i ++){
            if(vendingMachines.get(i).id == id)
                return vendingMachines.get(i);
        }
        return null;
    }

    // TODO: return item(s) if it matches a specific location
    public ArrayList<VendingMachine> getVendingMachinesByLocation(Location location) {
       return new ArrayList<>();
    }
}
