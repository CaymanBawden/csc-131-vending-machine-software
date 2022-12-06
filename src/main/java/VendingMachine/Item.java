package VendingMachine;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Item {
    public String name;
    public int expiration;
    public int row, col, slot;
    public double price;
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
        purchasedDate = new SimpleDateFormat("yyyy/MM/dd hh:mmaa").format(date);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPurchasedDate(String date) {
        purchasedDate = date;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String toString() {
        // format 1A1:Name:PrettyDate
        return String.format("%d %d %d: %s, %s", row, col, slot, name, prettyDate);
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
