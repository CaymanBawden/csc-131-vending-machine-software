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
    public static final int rows = 15;
    public static final int cols = 15;
    public static final int slots = 8;
    public String[][][][] inventory = new String[rows][cols][slots][];
    public static String pricesData;
    public String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public Boolean online = false;
    public static String filepath;


    // TODO: have a list of all items that should be removed from the vending machine
    // TODO: find a way to store the current date
    // TODO: vending machine should store a boolean of whether it is online or not

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
    public void printInventory() {
        for (int i = 0; i < inventory.length; i++) {
            for (int j = 0; j < inventory[i].length; j++) {
                for (int k = 0; k < inventory[i][j].length; k++) {
                    String[] item = inventory[i][j][k];
                    if (item != null) {
                        System.out.println(String.format("Row: %s, Col: %s, Slot: %s ", i + 1, alphabet.charAt(j), k + 1) + Arrays.toString(item));
                    }else
                        break;
                }
            }
        }
    }

    //Used to add items to first available slot in the inventory array
    public boolean addItem(String[] item, int[] index){
        boolean hasSpace = false;
        if(inventory[index[0]][index[1]][7] != null){
            System.out.println("Too many items in slot");
            return hasSpace;
        }
        int i;
        for(i = 0; i < 8; i ++){
            if(inventory[index[0]][index[1]][i] == null)
                break;
        }
        inventory[index[0]][index[1]][i] = item;
        refreshData();
        return !hasSpace;
    }

    //Removes the front item from the inventory array, intended for customers
    public boolean removeFrontItem(int[] index){
        boolean hasItem = false;
        if(inventory[index[0]][index[1]][0] == null)
            return hasItem;
        String[][][][] tmpinv = inventory;
        for(int i = 0; i < 8; i ++){
            if(i < 7){
                tmpinv[index[0]][index[1]][i] = tmpinv[index[0]][index[1]][i + 1];
            }else if(i == 7)
                tmpinv[index[0]][index[1]][i] = null;
        }
        inventory = tmpinv;
        refreshData();
        return !hasItem;
    }

    //Removes items at specific index from the inventory array, intended for restockers
    public boolean removeSpecificItem(int[] index){
        boolean hasItem = false;
        if(inventory[index[0]][index[1]][0] == null || inventory[index[0]][index[1]][index[2]] == null)
            return hasItem;
        String[][][][] tmpinv = inventory;
        for(int i = index[2]; i < 8; i ++){
            if(i < 7) {
                tmpinv[index[0]][index[1]][i] = tmpinv[index[0]][index[1]][i + 1];
            }else if(i == 7)
                tmpinv[index[0]][index[1]][i] = null;
        }
        inventory = tmpinv;
        refreshData();
        return !hasItem;
    }

    //Updates the file with the changes made to the inventory
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

    // TODO: make all fields reduce to one string to write to file
    public String toString(){
        return "vending-machine-string";
    }

    // Lists all expired items for the restocker
    public void checkExpirations() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd");
        String[] strDate = dateFormat.format(date).split("/");
        for (int i = 0; i < inventory.length; i++) {
            for (int j = 0; j < inventory[i].length; j++) {
                for (int k = 0; k < inventory[i][j].length; k++) {
                    String[] item = inventory[i][j][k];
                    if (item != null){
                        String[] expiration = item[1].split("/");
                        if(Integer.parseInt(strDate[0]) > Integer.parseInt(expiration[0])){
                            System.out.println(String.format("Item is now expired: %s in slot %s%s%s", item[0], i + 1, alphabet.charAt(j), k + 1));
                        }else if(Integer.parseInt(strDate[0]) == Integer.parseInt(expiration[0])){
                            if(Integer.parseInt(strDate[1]) > Integer.parseInt(expiration[1])){
                                System.out.println(String.format("Item is now expired: %s in slot %s%s%s", item[0], i + 1, alphabet.charAt(j), k + 1));
                            }else if(Integer.parseInt(strDate[1]) == Integer.parseInt(expiration[1])){
                                if(Integer.parseInt(strDate[2]) > Integer.parseInt(expiration[2])){
                                    System.out.println(String.format("Item is now expired: %s in slot %s%s%s", item[0], i + 1, alphabet.charAt(j), k + 1));
                                }else if(Integer.parseInt(strDate[2]) == Integer.parseInt(expiration[2])){
                                    System.out.println(String.format("Item is about to expire: %s in slot %s%s%s", item[0], i+1, alphabet.charAt(j), k+1));
                                }
                            }
                        }
                    }else
                       break;
                }
            }
        }
    }

    // TODO: return all items that are expired or marked as removed
    // TODO: return and remove item in specific row, col (e.g 1A)
    // TODO: add item in specific row, col, and slot (only for restockers)
    // NOTE: we need authentication at some level
}
