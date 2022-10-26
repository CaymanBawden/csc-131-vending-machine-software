package Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class VendingMachine {
    public int id;
    public String location;
    public static final int rows = 15;
    public static final int cols = 15;
    public static final int slots = 8;
    public String[][][][] inventory = new String[rows][cols][slots][];
    public String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public Boolean online = false;


    // TODO: have a list of all items that should be removed from the vending machine
    // TODO: find a way to store the current date
    // TODO: vending machine should store a boolean of whether it is online or not

    public VendingMachine(String data) {
        online = true;
        String[] splitData = data.split(",");
        id = Integer.parseInt(splitData[0]);
        location = splitData[1];
        String[] inventoryStrings = splitData[2].split(";");
        String[] pricesString = splitData[3].split(";");
        HashMap<String, String> prices = new HashMap<String, String>();

        for (String price : pricesString) {
            String[] priceData = price.split(":");
            prices.put(priceData[0], priceData[1]);
        }

        for (String itemString : inventoryStrings) {
            // split string into array for access
            String[] items = itemString.split(":");
            String[] inventoryIndex = items[0].split("");

            // convert alphabet character to index
            inventoryIndex[1] = Integer.toString(alphabet.indexOf(inventoryIndex[1]));
            String rowCol = items[0].substring(0, 2);
            String name = items[1];
            String date = items[2];

            int row = Integer.parseInt(inventoryIndex[0]) - 1;
            int col = Integer.parseInt(inventoryIndex[1]);
            int slot = Integer.parseInt(inventoryIndex[2]) - 1;

            // populate the item with data
            String[] item = new String[3];
            item[0] = name;
            item[1] = date;
            item[2] = prices.get(rowCol);

            inventory[row][col][slot] = item;
        }

         printInventory();
    }

    // for debugging purposes only
    public void printInventory() {
        for (int i = 0; i < inventory.length; i++) {
            for (int j = 0; j < inventory[i].length; j++) {
                for (int k = 0; k < inventory[i][j].length; k++) {
                    String[] item = inventory[i][j][k];
                    if (item != null)
                        System.out.println(String.format("Row: %s, Col: %s, Slot: %s ", i + 1, alphabet.charAt(j), k + 1) + Arrays.toString(item));
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
