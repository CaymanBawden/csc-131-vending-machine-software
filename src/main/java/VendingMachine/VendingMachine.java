package VendingMachine;

import java.util.Arrays;

public class VendingMachine {
    public int id;
    public Location location;
    public static final int rows = 8;
    public static final int cols = 5;
    public static final int slots = 15;
    public Inventory inventory;
    public QueuedItems queuedItems;
    public PurchaseHistory purchaseHistory;
    public String alphabet = "ABCDE";
    public Boolean online;

    public VendingMachine(String data) {
        online = true;
        int numColumns = 6;
        String[] csvData = new String[numColumns];
        String[] csvDataUneven = data.split(",");

        System.arraycopy(csvDataUneven, 0, csvData, 0, csvDataUneven.length);

        id = Integer.parseInt(csvData[0]);
        location = new Location(csvData[1]);

        String[] inventoryStrings = csvData[2].split("~");
        String[] pricesString = csvData[3].split("~");
        String[] queuedItemsStrings = csvData[4] != null && csvData[4] != "" ? csvData[4].split("~") : null;
        String[] purchaseHistoryStrings = csvData[5] != null && csvData[5] != "" ? csvData[5].split("~") : null;

        inventory = new Inventory(rows, cols, slots);

        for (String price : pricesString) {
            String[] priceData = price.split("\\*");
            String[] rowColData = priceData[0].split("");
            int row = Integer.parseInt(rowColData[0]) - 1;
            int col = alphabet.indexOf(rowColData[1]);
            inventory.addPrice(row, col, Double.parseDouble(priceData[1]));
        }

        for (String itemString : inventoryStrings) {
            String[] items = itemString.split("\\*");
            String position = items[0];

            int row = Integer.parseInt(String.valueOf(position.charAt(0))) - 1;
            int col = "ABCDE".indexOf(position.charAt(1));
            int slot = Integer.parseInt(position.substring(2)) - 1;

            Item item = new Item(items[1], items[2], row, col);
            item.setSlot(slot);

            inventory.addItem(item);
            item.setPrice(inventory.getPrice(row, col));
        }

        queuedItems = new QueuedItems(queuedItemsStrings);
        purchaseHistory = new PurchaseHistory(purchaseHistoryStrings);
    }

    // for debug
    public void print() {
        location.print();
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

        removedItem.setPurchasedDate();
        purchaseHistory.addPurchasedItem(removedItem);

        return successMsg;
    }

    public String toString() {
        return String.format("%s,%s,%s,%s,%s", id, location, inventory, queuedItems, purchaseHistory);
    }
}
