package InventoryManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Inventory {
    public int rows, cols, slots;
    private Item[][][] inventory;
    private HashMap<String, Double> prices = new HashMap<>();
    private String alphabet = "ABCDE";

    public Inventory(int rows, int cols, int slots) {
        this.rows = rows;
        this.cols = cols;
        this.slots = slots;
        inventory = new Item[rows][cols][slots];
    }

    public double getPrice(int row, int col) {
        return prices.get(row + "" + col);
    }

    // don't test this
    public boolean addPrice(int row, int col, Double price) {
        String key = row + "" + col;

        if (!prices.containsKey(key)) {
            prices.put(key, price);
        } else {
            return false;
        }

        return true;
    }

    public boolean changePrice(int row, int col, Double price) {
        String key = row + "" + col;

        if (prices.containsKey(key)) {
            prices.put(key, price);
            return true;
        }

        return false;
    }

    // Used to add items to first available slot in the inventory array
    public void addItem(Item item) {
        Item lastItem = inventory[item.row][item.col][slots - 1];

        if (lastItem != null) {
            throw new RuntimeException(String.format("Not enough space for item in row %s and col %s", item.row, item.col));
        }

        for (int slot = 0; slot < slots + 1; slot++) {
            if (inventory[item.row][item.col][slot] == null) {
                inventory[item.row][item.col][slot] = item;
                item.setSlot(slot);
                break;
            }
        }
    }

    // for debugging purposes only
    public void print(int limit) {
        int l = 0;
        for (int i = 0; i < inventory.length; i++) {
            for (int j = 0; j < inventory[i].length; j++) {
                for (int k = 0; k < inventory[i][j].length; k++) {
                    Item item = inventory[i][j][k];
                    if (item != null && l < limit) {
                        System.out.printf("%s $%.2f\n", item, getPrice(i, j));
                        l += 1;
                    }
                }
            }
        }
    }

    public void print() {
        for (int i = 0; i < inventory.length; i++) {
            for (int j = 0; j < inventory[i].length; j++) {
                for (int k = 0; k < inventory[i][j].length; k++) {
                    Item item = inventory[i][j][k];
                    if (item != null) {
                        item.print();
                    }
                }
            }
        }
    }

    public Item getItem(int row, int col, int slot) {
        return inventory[row][col][slot];
    }

    // don't test
    public Item removeFrontItem(int row, int col) {
        Item item = inventory[row][col][0];
        inventory[row][col][0] = null;
        shiftItemsDown(row, col);
        return item;
    }

    // Shifts items down after change of inventory
    private void shiftItemsDown(int row, int col) {
        int cursor = 1;
        for (int i = 0; i < slots - 1; i++) {
            Item[] rowAndCol = inventory[row][col];

            if (rowAndCol[i] == null) {
                inventory[row][col][i] = inventory[row][col][cursor];
                if (rowAndCol[i] != null)
                    rowAndCol[i].setSlot(i);
                rowAndCol[cursor] = null;
            }

            cursor++;
        }
    }

    public void removeMultItems(int[][] items) {
        for (int i = 0; i < items.length; i++) {
            int[] item = items[i];
            int row = item[0];
            int col = item[1];
            int slot = item[2];
            inventory[row][col][slot] = null;
        }
    }

    public String toString() {
        String itemName;
        double itemPrice;
        String inventoryString = "Inventory:\n";

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int stock = 0;
                for (int k = 0; k < slots && inventory[i][j][k] != null; k++)
                    stock = stock + 1;
                int itemRow = i + 1;
                char itemCol = alphabet.charAt(j);
                if (inventory[i][j][0] == null) {
                    itemName = "No Item";
                    itemPrice = 0.00;
                } else {
                    itemName = inventory[i][j][0].name;
                    itemPrice = prices.get(i + "" + j);
                }
                inventoryString = inventoryString + String.format("%d%s: %s. Stock: %d. Price: %.2f\n", itemRow, itemCol, itemName, stock, itemPrice);
            }
        }
        return inventoryString;
    }

    public ArrayList<Item> getExpiredItems() {
        ArrayList<Item> expiredItems = new ArrayList<>();
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        int currentDate = Integer.parseInt(dateFormat.format(date).replace("/", ""));

        for (int row = 0; row < inventory.length; row++) {
            for (int col = 0; col < inventory[row].length; col++) {
                for (int slot = 0; slot < inventory[row][col].length; slot++) {
                    Item item = inventory[row][col][slot];

                    if (item == null)
                        break;

                    int itemExpiration = item.expiration;
                    if (itemExpiration < currentDate)
                        expiredItems.add(item);
                }
            }
        }

        return expiredItems;
    }
}
