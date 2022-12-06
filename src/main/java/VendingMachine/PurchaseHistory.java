package VendingMachine;

public class PurchaseHistory {
    private Item[] purchaseHistory = new Item[10000];

    public PurchaseHistory(String[] purchaseHistoryStrings) {
        if (purchaseHistoryStrings != null)
            for (String purchaseHistoryString : purchaseHistoryStrings) {
                String[] purchasedItem = purchaseHistoryString.split("\\*");
                String[] rowColData = purchasedItem[0].split("");
                int row = Integer.parseInt(rowColData[0]) - 1;
                int col = "ABCDE".indexOf(rowColData[1]);
                String name = purchasedItem[1];
                String date = purchasedItem[2];
                double price = Double.parseDouble(purchasedItem[3]);
                String purchasedDate = purchasedItem[4];
                Item purchase = new Item(name, date, row, col);
                purchase.setPurchasedDate(purchasedDate);
                purchase.setPrice(price);
                addPurchasedItem(purchase);
            }
    }

    public int size() {
        return purchaseHistory.length;
    }

    public Item getItem(int i) {
        return purchaseHistory[i];
    }

    protected void addPurchasedItem(Item item) {
        purchaseHistory[purchaseHistory.length - 1] = null;
        for (int i = purchaseHistory.length - 2; i >= 0; i--) {
            purchaseHistory[i + 1] = purchaseHistory[i];
        }

        purchaseHistory[0] = item;
    }

    public String toString() {
        StringBuilder purchasedString = new StringBuilder();
        int i = 0;
        for (Item item : purchaseHistory) {
            if (item == null)
                break;
            String itemString = String.format("%s%s*%s*%s*%s*%s", item.row + 1, "ABCDE".charAt(item.col), item.name, item.prettyDate, item.price, item.purchasedDate);
            if (i == 0)
                purchasedString.append(itemString);
            else
                purchasedString.append("~").append(itemString);
            i++;
        }
        return purchasedString.toString();
    }
}
