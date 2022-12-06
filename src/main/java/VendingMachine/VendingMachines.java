package VendingMachine;

import java.io.*;
import java.util.ArrayList;

public class VendingMachines {
    public ArrayList<VendingMachine> vendingMachines = new ArrayList<>();

    public VendingMachines(String filepath) {
        ArrayList<String> lines = new ArrayList<>();
        try {
            FileReader inFile = new FileReader(filepath);
            BufferedReader inStream = new BufferedReader(inFile);
            String line;
            inStream.readLine();
            while ((line = inStream.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        FileRead fr = new FileRead(filepath);
//        int numLines = fr.getNumLines();
//
//        for (int i = 1; i < numLines; i++)
//            lines.add(fr.getLine(i).trim());

        for (String line : lines)
            vendingMachines.add(new VendingMachine(line));
    }

    public void saveData() {
        try (FileWriter fileWriter = new FileWriter("data/data.csv", false)) {
            fileWriter.write("id,location,inventory,prices,queuedItems,purchaseHistory\n");
            for (VendingMachine machine : vendingMachines) {
                fileWriter.write(machine + "\n");
            }
        } catch (IOException ioException) {
            System.out.println("Could not open file");
            System.exit(1);
        }
    }

    public VendingMachine getVendingMachineById(int id) {
        for (int i = 0; i < vendingMachines.size(); i++) {
            if (vendingMachines.get(i).id == id)
                return vendingMachines.get(i);
        }
        return null;
    }
}
