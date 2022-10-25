package Data;

import java.util.ArrayList;

public class VendingMachine {
    public int id;
    public String location;
    public String[][][] inventory = new String[15][15][8];
    public String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public String[] tmp1 = new String[1800];
    public String[] tmp2 = new String[3];
    public int inventoryCounter = 0;


    // TODO: generate a 3 dimensional array (1st array is row, 2nd array is col, and 3rd array is slot) for now 2 rows, 1 col, 4 slots
    // TODO: have a list of all items that should be removed from the vending machine
    // TODO: find a way to store the current date
    // TODO: vending machine should store a boolean of whether it is online or not

    public VendingMachine(String data) {
        String[] splitData = data.split(",");
        id = Integer.parseInt(splitData[0]);
        location = splitData[1];

        // this is the inventory string
        tmp1 = splitData[2].split(";");

        // TODO: loop through inventory string and populate inventory
        for (int i = 0; i < inventory.length; i++){
            for (int j = 0; j < inventory[i].length; j++){
                for (int k = 0; k < inventory[i][j].length; k++){
                    tmp1 = splitData[2].split(";");
                    if (tmp1.length > inventoryCounter){
                        tmp2 = tmp1[inventoryCounter].split(":");
                        inventory[i][j][k] = tmp2[1] + ":" + tmp2[2];
                        inventoryCounter++;
                    }
                }
            }
        }
        printInventory();
        // this is how we access columns by a character
        // alphabet.indexOf(character)

    }

    // for debugging purposes only
    public void printInventory() {
        for (int i = 0; i < inventory.length - 2; i++) {
            for (int j = 0; j < inventory[i].length - 2; j++) {
                for (int k = 0; k < inventory[i][j].length - 2; k++) {
                    String item = inventory[i][j][k];
                    if (item != null)
                        System.out.println(item);
                }
            }
        }
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
