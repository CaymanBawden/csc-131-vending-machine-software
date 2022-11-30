package InventoryManager;

public class Item {
    public String name;
    public int expiration;
    public int row, col, slot;
    public String prettyDate;

    public Item(String name, String expriation, int row, int col) {
        this.name = name;
        this.prettyDate = expriation;
        this.expiration = Integer.parseInt(expriation.replace("/", ""));
        this.row = row;
        this.col = col;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void print() {
        System.out.printf(
                "%d %d %d: %s, %s\n",
                row,
                col,
                slot,
                name,
                prettyDate
        );
    }
}
