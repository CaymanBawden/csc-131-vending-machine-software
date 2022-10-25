package Data;

import java.util.ArrayList;

public class VendingMachine {
    public int id;
    public String location;
    public String[][][] inventory = new String[15][15][8];
    public String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";


    // TODO: generate a 3 dimensional array (1st array is row, 2nd array is col, and 3rd array is slot) for now 2 rows, 1 col, 4 slots
    // TODO: have a list of all items that should be removed from the vending machine
    // TODO: find a way to store the current date
    // TODO: vending machine should store a boolean of whether it is online or not

    public VendingMachine(String data) {
        String[] splitData = data.split(",");
        id = Integer.parseInt(splitData[0]);
        location = splitData[1];
    }

    // TODO: make all fields reduce to one string to write to file
    public String toString() {
        return "vending-machine-string";
    }

    // TODO: return all items that are expired or marked as removed
    // TODO: return and remove item in specific row, col (e.g 1A)
    // TODO: add item in specific row, col, and slot (only for restockers)
    // NOTE: we need authentication at some level
}
