package UI;

import VendingMachine.VendingMachines;
import VendingMachine.VendingMachine;
import VendingMachine.QueuedItem;
import VendingMachine.Item;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class RestockerMainPanel {
    private final String[][] tableData;
    private final String[] columns;
    public VendingMachines machines;
    public VendingMachine currentVendingMachine;
    public Item currentItem;
    public QueuedItem currentQueuedItem;
    private JFrame mainFrame;
    private JLabel itemDescription;
    private JLabel errorMessage;
    private JTextField itemAddExpiration;
    private JTable itemActionTable;

    public RestockerMainPanel() {
        machines = new VendingMachines("data/data.csv");
        currentVendingMachine = machines.getVendingMachineById(2);

        int totalItems = 0;

        ArrayList<Item> actionItems = new ArrayList<>(currentVendingMachine.inventory.getExpiredItems());
        ArrayList<QueuedItem> queuedItems = new ArrayList<>(currentVendingMachine.queuedItems.queuedItems);

        totalItems += actionItems.size() + queuedItems.size();

        columns = new String[]{"Position", "Name", "Expiration Date", "Price", "Reason"};
        tableData = new String[totalItems][columns.length];

        for (int i = 0; i < actionItems.size(); i++) {
            Item item = actionItems.get(i);
            tableData[i] = new String[]{
                    Integer.toString(item.row + 1) + "ABCDE".charAt(item.col) + (item.slot + 1),
                    item.name,
                    item.prettyDate,
                    String.valueOf(currentVendingMachine.inventory.getPrice(item.row, item.col)),
                    "Remove: Expired"
            };
        }

        for (int i = 0; i < queuedItems.size(); i++) {
            QueuedItem item = queuedItems.get(i);
            tableData[i + actionItems.size()] = new String[]{
                    Integer.toString(item.row + 1) + "ABCDE".charAt(item.col) + (item.slot + 1),
                    item.name,
                    item.expiration == 0 ? "N/A" : item.prettyDate,
                    String.valueOf(item.price),
                    item.type + ": " + item.reason,
            };
        }

        prepareGUI();
    }

    private void addTableClickListener() {
        itemActionTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && itemActionTable.getSelectedRow() != -1) {
                String reason = itemActionTable.getValueAt(itemActionTable.getSelectedRow(), 4).toString();
                String itemLocationString = itemActionTable.getValueAt(itemActionTable.getSelectedRow(), 0).toString();

                int row = Integer.parseInt(String.valueOf(itemLocationString.charAt(0))) - 1;
                int col = "ABCDE".indexOf(itemLocationString.charAt(1));
                int slot = Integer.parseInt(itemLocationString.substring(2)) - 1;

                // if item is a queuedItem
                if (!reason.equals("Remove: Expired")) {
                    currentItem = null;
                    currentQueuedItem = currentVendingMachine.queuedItems.get(row, col, slot);
                    String description = String.format("%s: %s, %s: %s", itemLocationString, currentQueuedItem.name, currentQueuedItem.type, currentQueuedItem.reason);
                    itemDescription.setText("Selected Queued Item: " + description);
                    return;
                }

                currentQueuedItem = null;
                currentItem = currentVendingMachine.inventory.getItem(row, col, slot);
                String description = String.format("%s: %s, %s", itemLocationString, currentItem.name, currentItem.prettyDate);
                itemDescription.setText("Selected Item: " + description);
            }
        });
    }

    private void addTable() {
        itemActionTable = new JTable(new DefaultTableModel(tableData, columns));
        itemActionTable.setDefaultEditor(Object.class, null);
        itemActionTable.getTableHeader().setReorderingAllowed(false);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(itemActionTable.getModel());
        itemActionTable.setRowSorter(sorter);

        JScrollPane sp = new JScrollPane(itemActionTable);
        sp.setSize(1000, 800);

        addTableClickListener();

        mainFrame.add(sp);
    }

    private void addItemPanel() {
        JPanel itemActionPanel = new JPanel();
        itemActionPanel.setLayout(new BoxLayout(itemActionPanel, BoxLayout.PAGE_AXIS));

        itemDescription = new JLabel("Selected Item: None");
        itemDescription.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel toolBar = new JPanel();
        toolBar.setLayout(new FlowLayout());

        JButton resolveBtn = new JButton("Resolve");
        JButton saveBtn = new JButton("Save");
        resolveBtn.setActionCommand("resolve");
        saveBtn.setActionCommand("save");
        resolveBtn.addActionListener(new ButtonClickListener());
        saveBtn.addActionListener(new ButtonClickListener());

        toolBar.add(resolveBtn);
        toolBar.add(saveBtn);

        JPanel expirationPanel = new JPanel();

        itemAddExpiration = new JTextField("", 15);
        JLabel itemAddExpirationLabel = new JLabel("Add Expiration (YYYY/MM/DD)");
        errorMessage = new JLabel();

        expirationPanel.add(itemAddExpirationLabel);
        expirationPanel.add(itemAddExpiration);
        expirationPanel.add(errorMessage);

        itemActionPanel.add(itemDescription);
        itemActionPanel.add(toolBar);
        itemActionPanel.add(expirationPanel);

        mainFrame.add(itemActionPanel);
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Vending Machine Project");
        mainFrame.setSize(1000, 1000);
        mainFrame.setLayout(new GridLayout(2, 1));

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        addTable();
        addItemPanel();
    }

    private void updateItemActionTable() {
        int selectedRow = itemActionTable.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) itemActionTable.getModel();
        int rowCount = model.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }

        int totalItems = 0;

        ArrayList<Item> expiredItems = new ArrayList<>(currentVendingMachine.inventory.getExpiredItems());
        ArrayList<QueuedItem> queuedItems = new ArrayList<>(currentVendingMachine.queuedItems.queuedItems);

        totalItems += expiredItems.size() + queuedItems.size();

        String[][] newTableData = new String[totalItems][columns.length];

        for (int i = 0; i < expiredItems.size(); i++) {
            Item item = expiredItems.get(i);
            newTableData[i] = new String[]{
                    Integer.toString(item.row + 1) + "ABCDE".charAt(item.col) + (item.slot + 1),
                    item.name,
                    item.prettyDate,
                    String.valueOf(currentVendingMachine.inventory.getPrice(item.row, item.col)),
                    "Remove: Expired"
            };
        }

        for (int i = 0; i < queuedItems.size(); i++) {
            QueuedItem item = queuedItems.get(i);
            newTableData[i + expiredItems.size()] = new String[]{
                    Integer.toString(item.row + 1) + "ABCDE".charAt(item.col) + (item.slot + 1),
                    item.name,
                    item.expiration == 0 ? "N/A" : item.prettyDate,
                    String.valueOf(item.price),
                    item.type + ": " + item.reason,
            };
        }

        for (String[] row : newTableData) model.addRow(row);

        if (selectedRow >= totalItems)
            selectedRow = totalItems - 1;

        if (totalItems != 0)
            itemActionTable.setRowSelectionInterval(selectedRow, selectedRow);
    }

    public void show() {
        mainFrame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("resolve") && currentItem != null) {
                // remove expired item
                currentVendingMachine.inventory.removeAndShift(currentItem.row, currentItem.col, currentItem.slot);
                currentItem = null;
                itemDescription.setText("Selected Item: None");
                updateItemActionTable();
            }
            if (command.equals("resolve") && currentQueuedItem != null) {
                SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                // add queued item
                if (currentQueuedItem.type.equals("Add")) {
                    String expiration = itemAddExpiration.getText();
                    try {
                        inputDateFormat.parse(expiration);
                    } catch (ParseException exception) {
                        errorMessage.setText("Please set a valid date in format (YYYY/MM/DD)");
                        return;
                    }

                    Date currentDate = Calendar.getInstance().getTime();
                    String currentDateFormat = new SimpleDateFormat("yyyy/MM/dd").format(currentDate);
                    int currentDateExpiration = Integer.parseInt(currentDateFormat.replace("/", ""));
                    int inputtedDate = Integer.parseInt(expiration.replace("/", ""));

                    if (inputtedDate <= currentDateExpiration) {
                        errorMessage.setText("Can't put expired item in vending machine");
                        return;
                    }

                    Item newItem = new Item(currentQueuedItem.name, expiration, currentQueuedItem.row, currentQueuedItem.col);
                    currentVendingMachine.inventory.addItem(newItem);
                    currentVendingMachine.inventory.changePrice(currentQueuedItem.row, currentQueuedItem.col, currentQueuedItem.price);
                    currentVendingMachine.queuedItems.remove(currentQueuedItem.row, currentQueuedItem.col, currentQueuedItem.slot);
                    updateItemActionTable();

                    itemDescription.setText("Selected Item: None");
                    errorMessage.setText("");
                }
                // remove queued item
                if (currentQueuedItem.type.equals("Remove")) {
                    currentVendingMachine.inventory.remove(currentQueuedItem.row, currentQueuedItem.col, currentQueuedItem.slot);
                    currentVendingMachine.queuedItems.remove(currentQueuedItem.row, currentQueuedItem.col, currentQueuedItem.slot);
                    updateItemActionTable();
                    itemDescription.setText("Selected Item: None");
                }
            }

            if (command.equals("save")) {
                machines.saveData();
            }
        }
    }
}
