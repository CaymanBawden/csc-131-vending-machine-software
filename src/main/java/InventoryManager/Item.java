package InventoryManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Item {
    public String name;
    public int expiration;
    public int row, col, slot;
    public String prettyDate;
    public String purchasedDate;

    public Item(String name, String expriation, int row, int col) {
        this.name = name;
        this.prettyDate = expriation;
        this.expiration = Integer.parseInt(expriation.replace("/", ""));
        this.row = row;
        this.col = col;
    }

    public void setPurchasedDate() {
        Date date = Calendar.getInstance().getTime();
        String formattedDate = new SimpleDateFormat("yyyy/MM/dd:hh:mmaa").format(date);
        purchasedDate = formattedDate;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String toString() {return String.format("%d %d %d: %s, %s", row, col, slot, name, prettyDate);}

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
