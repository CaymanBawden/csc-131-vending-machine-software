package VendingMachine;

public class QueuedItem {
    public String reason;
    public String type;
    public String name;
    public int expiration;
    public String prettyDate;
    public int row, col, slot;
    public double price;

    public QueuedItem(Item item, String reason, String type) {
        this.name = item.name;
        this.expiration = item.expiration;
        this.prettyDate = item.prettyDate;
        this.row = item.row;
        this.col = item.col;
        this.slot = item.slot;
        this.reason = reason;
        this.type = type;
        this.price = item.price;
    }

    public String toString() {
        return String.format("%s*%s*%s*%s*%s*%s", (row + 1 + "") + "ABCDE".charAt(col) + (slot + 1 + ""), name, reason, type, prettyDate, price);
    }

    // for debugging purposes only
    public void print() {
        System.out.printf(
                "%d%d%d: %s, %s, %s, %s, %s",
                row,
                col,
                slot,
                name,
                prettyDate,
                price,
                reason,
                type
        );
    }
}
