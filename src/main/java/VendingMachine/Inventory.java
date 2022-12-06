package VendingMachine;

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

    public ArrayList<Item> getItems(int row, int col) {
        ArrayList<Item> items = new ArrayList<>();
        for (int i = 0; i < inventory[row][col].length; i++)
            if (inventory[row][col][i] != null)
                items.add(inventory[row][col][i]);
        return items;
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

    public void remove(int row, int col, int slot) {
        inventory[row][col][slot] = null;
    }

    public void removeAndShift(int row, int col, int slot) {
        inventory[row][col][slot] = null;
        shiftItemsDown(row, col);
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

    public String toString() {
        StringBuilder inventoryString = new StringBuilder();
        int i = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                for (int slot = 0; slot < slots; slot++) {
                    if (inventory[row][col][slot] == null) {
                        break;
                    }
                    Item item = inventory[row][col][slot];
                    String rowColSlot = String.format("%d%s%d", item.row + 1, alphabet.charAt(item.col), item.slot + 1);
                    if (i == 0)
                        inventoryString.append(String.format("%s*%s*%s", rowColSlot, item.name, item.prettyDate));
                    else
                        inventoryString.append(String.format("~%s*%s*%s", rowColSlot, item.name, item.prettyDate));
                    i++;
                }
            }
        }

        StringBuilder priceString = new StringBuilder(",");
        for (int priceRow = 0; priceRow < rows; priceRow++) {
            for (int priceCol = 0; priceCol < cols; priceCol++) {
                String rowCol = priceRow + 1 + "" + alphabet.charAt(priceCol);
                if (priceCol == 0 && priceRow == 0)
                    priceString.append(String.format("%s*%.2f", rowCol, prices.get(priceRow + "" + priceCol)));
                else
                    priceString.append(String.format("~%s*%.2f", rowCol, prices.get(priceRow + "" + priceCol)));
            }
        }

        inventoryString.append(priceString);

        return inventoryString.toString();
    }

    public int getQuantity(int row, int col) {
        int quantity = 0;
        for (int slot = 0; slot < inventory[row][col].length; slot++)
            if (inventory[row][col][slot] != null)
                quantity++;
        return quantity;
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
