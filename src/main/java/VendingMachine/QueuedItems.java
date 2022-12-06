package VendingMachine;

import java.util.ArrayList;

public class QueuedItems {
    public ArrayList<QueuedItem> queuedItems = new ArrayList<>();
    public String alphabet = "ABCDE";

    public QueuedItems(String[] queuedItems) {
        if (queuedItems != null) {
            for (String queuedItemString : queuedItems) {
                String[] queuedItemData = queuedItemString.split("\\*");
                String position = queuedItemData[0];
                int row = Integer.parseInt(String.valueOf(position.charAt(0))) - 1;
                int col = "ABCDE".indexOf(position.charAt(1));
                int slot = Integer.parseInt(position.substring(2)) - 1;
                String name = queuedItemData[1];
                String reason = queuedItemData[2];
                String type = queuedItemData[3];
                String date = queuedItemData[4];
                double price = Double.parseDouble(queuedItemData[5]);
                Item newItem = new Item(name, date, row, col);
                newItem.setPrice(price);
                newItem.setSlot(slot);
                QueuedItem queuedItem = new QueuedItem(newItem, reason, type);
                this.queuedItems.add(queuedItem);
            }
        }
    }

    public void add(QueuedItem queuedItem) {
        queuedItems.add(queuedItem);
    }

    public void print() {
        for (QueuedItem item : queuedItems)
            item.print();
    }

    public boolean remove(int row, int col, int slot) {
        return queuedItems.removeIf(item -> item.row == row && item.col == col && item.slot == slot);
    }

    public int getCountInRowCol(int row, int col) {
        int count = 0;

        for (QueuedItem item : queuedItems)
            if (item.row == row && item.col == col)
                count++;

        return count;
    }

    public QueuedItem get(int row, int col, int slot) {
        for (QueuedItem item : queuedItems)
            if (row == item.row && col == item.col && slot == item.slot)
                return item;
        return null;
    }

    public String toString() {
        StringBuilder queuedString = new StringBuilder();
        int i = 0;
        for (QueuedItem item : queuedItems) {
            if (i == 0)
                queuedString.append(item.toString());
            else
                queuedString.append("~").append(item);
            i++;
        }
        return queuedString.toString();
    }
}
