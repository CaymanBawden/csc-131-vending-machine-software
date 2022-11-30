package VendingMachine;

import InventoryManager.Item;

public class QueuedItem {
    public String reason;
    public String name;
    public int expiration;
    public int row, col;

    public QueuedItem(Item item, String reason) {
        this.name = item.name;
        this.expiration = item.expiration;
        this.row = item.row;
        this.col = item.col;
        this.reason = reason;
    }

    // for debugging purposes only
    public void print() {
        System.out.printf(
                "%d%d: %s, %s%n",
                row,
                col,
                name,
                expiration
        );
    }
}
