package VendingMachine;

import InventoryManager.Inventory;
import InventoryManager.Item;
import utils.Location;

import java.util.ArrayList;

public class VendingMachine {
    public int id;
    public Location location;
    public static final int rows = 8;
    public static final int cols = 5;
    public static final int slots = 15;
    public Inventory inventory = new Inventory(rows, cols, slots);
    public ArrayList<QueuedItem> queuedItems = new ArrayList<>();
    public String alphabet = "ABCDE";
    public Boolean online = false;
    public static String filepath;

    // TODO: have a list of all items that should be removed from the vending machine
    // TODO: make all fields reduce to one string to write to file

    public VendingMachine(String data, String path) {
        online = true;
        filepath = path;
        String[] csvData = data.split(",");
        id = Integer.parseInt(csvData[0]);
        location = new Location(csvData[1]);

        String[] inventoryStrings = csvData[2].split(";");
        String[] pricesString = csvData[3].split(";");

        for (String price : pricesString) {
            String[] priceData = price.split(":");
            String[] rowColData = priceData[0].split("");
            int row = Integer.parseInt(rowColData[0]) - 1;
            int col = alphabet.indexOf(rowColData[1]);
            inventory.addPrice(row, col, Double.parseDouble(priceData[1]));
        }

        for (String itemString : inventoryStrings) {
            // split string into array for access
            String[] items = itemString.split(":");
            String[] inventoryIndex = items[0].split("");

            int row = Integer.parseInt(inventoryIndex[0]) - 1;
            int col = alphabet.indexOf(inventoryIndex[1]);
            int slot = Integer.parseInt(inventoryIndex[2]) - 1;

            Item item = new Item(items[1], items[2], row, col);
            item.setSlot(slot);

            inventory.addItem(item);
        }
    }

    public void print() {
        location.print();
    }

    public void printQueuedItems() {
        for (QueuedItem item : queuedItems)
            item.print();
    }

    public void addQueuedItem(Item item, String reason) {
        queuedItems.add(new QueuedItem(item, reason));
    }

    // Removes and returns an item in a row and column
    public String toString() {
        return "vending-machine-string";
    }

}
