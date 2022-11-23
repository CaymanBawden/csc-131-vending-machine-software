package Main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import utils.FileRead;
import utils.FileWrite;
import java.util.Date;
import java.util.Calendar;
import java.text.DateFormat;

public class VendingMachine {
    public int id;
    public String location;
    public static final int rows = 8;
    public static final int cols = 5;
    public static final int slots = 15;
    public String[][][][] inventory = new String[rows][cols][slots][];
    public static String pricesData;
    public String alphabet = "ABCDE";
    public Boolean online = false;
    public static String filepath;

    // TODO: have a list of all items that should be removed from the vending machine
    // TODO: make all fields reduce to one string to write to file

    public VendingMachine(String data, String path) {
        online = true;
        filepath = path;
        String[] splitData = data.split(",");
        id = Integer.parseInt(splitData[0]);
        location = splitData[1];
        String[] inventoryStrings = splitData[2].split(";");
        String[] pricesString = splitData[3].split(";");
        pricesData = splitData[3];
        HashMap<String, String> prices = new HashMap<String, String>();

        for (String price : pricesString) {
            String[] priceData = price.split(":");
            prices.put(priceData[0], priceData[1]);
        }

        for (String itemString : inventoryStrings) {
            // split string into array for access
            String[] items = itemString.split(":");
            String[] inventoryIndex = items[0].split("");

            // convert alphabet character to index
            inventoryIndex[1] = Integer.toString(alphabet.indexOf(inventoryIndex[1]));
            String rowCol = items[0].substring(0, 2);
            String name = items[1];
            String date = items[2];

            int row = Integer.parseInt(inventoryIndex[0]) - 1;
            int col = Integer.parseInt(inventoryIndex[1]);
            int slot = Integer.parseInt(inventoryIndex[2]) - 1;

            // populate the item with data
            String[] item = new String[3];
            item[0] = name;
            item[1] = date;
            item[2] = prices.get(rowCol);

            inventory[row][col][slot] = item;
        }
    }

    // for debugging purposes only
    public void printInventory(int limit) {
        int l = 0;
        for (int i = 0; i < inventory.length; i++) {
            for (int j = 0; j < inventory[i].length; j++) {
                for (int k = 0; k < inventory[i][j].length; k++) {
                    String[] item = inventory[i][j][k];
                    if (item != null && l < limit) {
                        l += 1;
                        System.out.println(String.format("Row: %s, Col: %s, Slot: %s ", i + 1, alphabet.charAt(j), k + 1) + Arrays.toString(item));
                    }
                }
            }
        }
    }

    public void printInventory() {
        int l = 0;
        for (int i = 0; i < inventory.length; i++) {
            for (int j = 0; j < inventory[i].length; j++) {
                for (int k = 0; k < inventory[i][j].length; k++) {
                    String[] item = inventory[i][j][k];
                    if (item != null) {
                        l += 1;
                        System.out.println(String.format("Row: %s, Col: %s, Slot: %s ", i + 1, alphabet.charAt(j), k + 1) + Arrays.toString(item));
                    }
                }
            }
        }
    }

    //Used to add items to first available slot in the inventory array
    public boolean addItem(String[] item, int row, char colChar){
        int col = alphabet.indexOf(colChar);
        String[] lastItem = inventory[row][col][slots - 1];

        if(lastItem != null){
            throw new RuntimeException(String.format("Not enough space for item in row %s and col %s", row, col));
        }

        for(int slot = 0; slot < slots + 1; slot ++){
            if(inventory[row][col][slot] == null) {
                inventory[row][col][slot] = item;
                break;
            }
        }

        return true;
    }

    // Removes and returns an item in a row and column
    public String[] removeFrontItem(int row, char colChar){
        int col = alphabet.indexOf(colChar);
        String[] item = inventory[row-1][col][0];
        inventory[row][col][0] = null;
        shiftItemsDown(row, col);
        return item;
    }

    //Shifts items down after change of inventory
    private void shiftItemsDown(int row, int col){
        int cursor = 1;
        for(int i = 0; i < slots - 1; i++){
            String [][] rowAndCol = inventory[row][col];

            if (rowAndCol[i] == null) {
                rowAndCol[i] = rowAndCol[cursor];
                rowAndCol[cursor] = null;
            }

            cursor++;
        }
    }

    //Updates the file with the changes made to the inventory
    //Will be removed or changed
    public void refreshData(){
        ArrayList<String> lines = new ArrayList<>();
        FileRead fr = new FileRead(filepath);
        FileWrite fw = new FileWrite(filepath);
        int fileIndex = 0;
        int numLines = fr.getNumLines();
        lines.add(fr.getLine(0));
        for(int i = 1; i < numLines; i++) {
            lines.add(fr.getLine(i).trim());
            String[] data = lines.get(i).split(",");
            if (Integer.parseInt(data[0]) == id){
                fileIndex = i;
            }
        }
        String totalInventory = "";
        for (int i = 0; i < inventory.length; i++) {
            for (int j = 0; j < inventory[i].length; j++) {
                for (int k = 0; k < inventory[i][j].length; k++) {
                    String[] item = inventory[i][j][k];
                    if (item != null) {
                        String partOfInventory =String.format("%s%s%s:%s:%s;", i + 1, alphabet.charAt(j), k+1, item[0], item[1]);
                        totalInventory = totalInventory + partOfInventory;
                    }else
                        break;
                }
            }
        }
        totalInventory = totalInventory.substring(0, totalInventory.length() - 1);
        lines.remove(fileIndex);
        lines.add(fileIndex, String.format("%s,%s,%s,%s", id, location, totalInventory, pricesData));
        for(int i = 0; i < lines.size(); i++){
            fw.writeLine(lines.get(i));
        }
        fw.saveFile();
    }

    public String toString(){
        return "vending-machine-string";
    }

    // Lists all expired items
    public ArrayList<String[]> checkExpirations() {
        ArrayList<String[]> expiredItems = new ArrayList<>();
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        int currentDate = Integer.parseInt(dateFormat.format(date).replace("/", ""));

        for (int row = 0; row < inventory.length; row++) {
            for (int col = 0; col < inventory[row].length; col++) {
                for (int slot = 0; slot < inventory[row][col].length; slot++) {
                    String[] item = inventory[row][col][slot];

                    if (item == null)
                        break;

                    int itemExpiration = Integer.parseInt(item[1].replace("/", ""));
                    if (itemExpiration < currentDate)
                        expiredItems.add(item);
                }
            }
        }

        return expiredItems;
    }

    // Checks item queue and returns list of all items in it.
    public ArrayList<String[]> checkQueuedItems(){
        ArrayList<String[]> queuedItems = new ArrayList<>();
        return queuedItems;
    }


    public void removeMultItems(int[][] items){
        for(int i = 0; i < items.length; i++){
            int[] item = items[i];
            int row = item[0];
            int col = item[1];
            int slot = item[2];
            inventory[row][col][slot] = null;
        }
    }
}
