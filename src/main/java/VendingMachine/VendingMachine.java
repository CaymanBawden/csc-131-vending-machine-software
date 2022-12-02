package VendingMachine;

import InventoryManager.Inventory;
import InventoryManager.Item;
import utils.Location;

import java.util.ArrayList;
import java.util.Arrays;

public class VendingMachine {
    public int id;
    public Location location;
    public static final int rows = 8;
    public static final int cols = 5;
    public static final int slots = 15;
    public Inventory inventory = new Inventory(rows, cols, slots);
    public ArrayList<QueuedItem> queuedItems = new ArrayList<>();
    public Item[] purchasedItems = new Item[100];
    public String alphabet = "ABCDE";
    public Boolean online;

    // TODO: make all fields reduce to one string to write to file

    public VendingMachine(String data) {
        online = true;
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

    // all print functions are only for debugging
    public void print() {
        location.print();
    }

    public void printQueuedItems() {
        for (QueuedItem item : queuedItems)
            item.print();
    }

    public void addQueuedItem(QueuedItem queuedItem) {
        queuedItems.add(queuedItem);
    }

    public void removeQueuedItem(QueuedItem queuedItem) {
        queuedItems.remove(queuedItem);
    }

    public String purchaseItem(int row, int col, double price) {
        Item removedItem = inventory.removeFrontItem(row, col);
        if (removedItem == null)
            return "Could not remove item";

        String successMsg = "Check below for item";
        double itemPrice = inventory.getPrice(row, col);

        if (itemPrice < price)
            successMsg = "Dispensing item below and change of: $" + (price - itemPrice);

        if (itemPrice > price)
            return "Please insert: $" + (itemPrice - price) + " more";

        addPurchasedItem(removedItem);

        return successMsg;
    }

    private void addPurchasedItem(Item item) {
        item.setPurchasedDate();

        purchasedItems[purchasedItems.length - 1] = null;
        for (int i = purchasedItems.length - 2; i >= 0; i--) {
            purchasedItems[i + 1] = purchasedItems[i];
        }

        purchasedItems[0] = item;
    }

    // Removes and returns an item in a row and column
    public String toString() {
        String vendingMachineString = String.format("%d, %s, %s, %s, %d\n", id, location.address, location.city, location.state, location.zipCode);
        vendingMachineString = vendingMachineString + inventory.toString();

        String queuedString = "Queued Items:\n";
        for(QueuedItem item : queuedItems){
            queuedString = queuedString + String.format("%d%s: %s. Reason: %s",item.row+1, alphabet.indexOf(item.col), item.name, item.reason);
        }
        vendingMachineString = vendingMachineString + queuedString;

        String purchasedString = "Purchased Items:\n";
        for(int i=0; i<purchasedItems.length; i++){
            if(purchasedItems[i] == null)
                break;
            Item item = purchasedItems[i];
            purchasedString = purchasedString + String.format("%d%s: %s. Purchase Date: %s",item.row+1, alphabet.charAt(item.col), item.name, item.purchasedDate);
        }
        vendingMachineString = vendingMachineString + purchasedString;

        return vendingMachineString;
    }

}
