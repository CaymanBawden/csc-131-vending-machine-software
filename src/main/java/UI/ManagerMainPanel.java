package UI;

import VendingMachine.Item;
import VendingMachine.VendingMachines;
import VendingMachine.VendingMachine;
import VendingMachine.QueuedItem;
import VendingMachine.Location;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

// TODO: optional add quantity text field parameter in Add Items
// TODO: optional add table on left for addItems and removeItems to see already queuedItems

public class ManagerMainPanel {
    private final String[][] tableData;
    private final String[] columnNames;
    VendingMachines machines;
    VendingMachine currentVendingMachine;
    private CardLayout vendingMachinePanelContentLayout;
    private JFrame mainFrame;
    private JTextField addNameTextField;
    private JTextField addPriceTextField;
    private JTextField addRowTextField;
    private JTextField addColTextField;
    private JTextField addReasonTextField;
    private JTextField removeColTextField;
    private JTextField removeReasonTextField;
    private JTextField removeRowTextField;
    private JPanel vendingMachinePanel;
    private JPanel vendingMachinePanelContent;
    private JPanel toolBar;
    private JPanel addItemsPanel;
    private JPanel removeItemsPanel;
    private JLabel vendingMachineViewHeader;
    private JLabel errorMessage;
    private JTable machineTable;
    private JTable inventoryTable;
    private JTable purchaseHistoryTable;
    private TableRowSorter<TableModel> sorter;

    public ManagerMainPanel() {
        machines = new VendingMachines("data/data.csv");

        columnNames = new String[]{"ID", "Address", "City", "State", "Zip"};
        tableData = new String[machines.vendingMachines.size()][columnNames.length];

        for (int i = 0; i < machines.vendingMachines.size(); i++) {
            VendingMachine machine = machines.vendingMachines.get(i);
            tableData[i] = new String[]{
                    String.valueOf(machine.id),
                    machine.location.address,
                    machine.location.city,
                    machine.location.state,
                    String.valueOf(machine.location.zipCode)
            };
        }


        prepareGUI();
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

        JPanel tableContainer = new JPanel();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.PAGE_AXIS));
        tableContainer.setSize(1000, 1000);

        machineTable = new JTable(tableData, columnNames);
        machineTable.setDefaultEditor(Object.class, null);
        machineTable.getTableHeader().setReorderingAllowed(false);

        sorter = new TableRowSorter<>(machineTable.getModel());
        machineTable.setRowSorter(sorter);

        JScrollPane sp = new JScrollPane(machineTable);
        sp.setSize(1000, 800);

        toolBar = new JPanel();

        tableContainer.add(sp);
        tableContainer.add(toolBar);

        vendingMachinePanel = new JPanel();
        vendingMachinePanel.setLayout(new BoxLayout(vendingMachinePanel, BoxLayout.PAGE_AXIS));

        vendingMachinePanelContent = new JPanel();
        vendingMachinePanelContentLayout = new CardLayout();
        vendingMachinePanelContent.setLayout(vendingMachinePanelContentLayout);

        inventoryTable = new JTable(new DefaultTableModel(new String[][]{}, new String[]{"Position", "Name", "Quantity", "Price"}));
        inventoryTable.setDefaultEditor(Object.class, null);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(inventoryTable.getModel());
        inventoryTable.setRowSorter(sorter);
        JScrollPane inventoryScroll = new JScrollPane(inventoryTable);
        inventoryScroll.setSize(1000, 800);

        purchaseHistoryTable = new JTable(new DefaultTableModel(new String[][]{}, new String[]{"Position", "Name", "Price", "Purchase At"}));
        purchaseHistoryTable.setDefaultEditor(Object.class, null);
        TableRowSorter<TableModel> purchaseHistorySorter = new TableRowSorter<>(purchaseHistoryTable.getModel());
        purchaseHistoryTable.setRowSorter(purchaseHistorySorter);
        JScrollPane purchaseHistoryScroll = new JScrollPane(purchaseHistoryTable);
        purchaseHistoryScroll.setSize(1000, 800);

        addItemsPanel = new JPanel();
        addItemsPanel.setLayout(new FlowLayout());

        removeItemsPanel = new JPanel();
        removeItemsPanel.setLayout(new FlowLayout());

        vendingMachinePanelContent.add(inventoryScroll, "viewItems");
        vendingMachinePanelContent.add(purchaseHistoryScroll, "viewPurchasedItems");
        vendingMachinePanelContent.add(addItemsPanel, "addItems");
        vendingMachinePanelContent.add(removeItemsPanel, "removeItems");

        vendingMachinePanel.add(vendingMachinePanelContent);

        mainFrame.add(tableContainer);
        mainFrame.add(vendingMachinePanel);
    }

    private void addToolbarItems() {
        createFilterTextField(new JTextField("", 10), "Filter by ID:", "ID");
        createFilterTextField(new JTextField("", 10), "Filter by Address:", "Address");
        createFilterTextField(new JTextField("", 10), "Filter by City:", "City");
        createFilterTextField(new JTextField("", 10), "Filter by State:", "State");
        createFilterTextField(new JTextField("", 10), "Filter by Zip:", "Zip");
    }

    private void addFilterFieldListener(JTextField textField, int col) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                newFilter();
            }

            public void removeUpdate(DocumentEvent e) {
                newFilter();
            }

            public void insertUpdate(DocumentEvent e) {
                newFilter();
            }

            private void newFilter() {
                RowFilter<TableModel, Object> rf = null;
                // If current expression doesn't parse, don't update.
                try {
                    rf = RowFilter.regexFilter("(?i)" + textField.getText(), col);
                } catch (java.util.regex.PatternSyntaxException e) {
                    return;
                }
                sorter.setRowFilter(rf);
            }
        });
    }

    private void createFilterTextField(JTextField textField, String label, String col) {
        JPanel panel = new JPanel();
        JLabel filterLabel = new JLabel(label);
        panel.add(filterLabel);
        panel.add(textField);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        toolBar.add(panel);
        int dynamicCol = machineTable.getColumn(col).getModelIndex();
        addFilterFieldListener(textField, dynamicCol);
    }

    private void addTableClickListener() {
        machineTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && machineTable.getSelectedRow() != -1) {
                int id = Integer.parseInt(machineTable.getValueAt(machineTable.getSelectedRow(), 0).toString());
                currentVendingMachine = machines.getVendingMachineById(id);
                Location location = currentVendingMachine.location;
                String headerString = String.format("%s: %s: %s, %s %s", currentVendingMachine.id, location.address, location.city, location.state, location.zipCode);
                vendingMachineViewHeader.setText("Selected Vending Machine: " + headerString);
                updateInventoryTable(currentVendingMachine);
                updatePurchaseHistoryTable(currentVendingMachine);
            }
        });
    }

    private void updateInventoryTable(VendingMachine machine) {
        DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
        int rowCount = model.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }

        for (int row = 0; row < machine.inventory.rows; row++) {
            for (int col = 0; col < machine.inventory.cols; col++) {
                String itemName = "None";
                Item item = machine.inventory.getItem(row, col, 0);
                if (item != null)
                    itemName = item.name;
                String quantity = Integer.toString(machine.inventory.getQuantity(row, col));
                if (Integer.parseInt(quantity) < 10)
                    quantity = "0" + quantity;
                model.addRow(new Object[]{row + 1 + "" + "ABCDE".charAt(col), itemName, quantity, machine.inventory.getPrice(row, col)});
            }
        }
    }

    private void updatePurchaseHistoryTable(VendingMachine machine) {
        DefaultTableModel model = (DefaultTableModel) purchaseHistoryTable.getModel();
        int rowCount = model.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }

        for (int i = 0; i < machine.purchaseHistory.size(); i++) {
            Item item = machine.purchaseHistory.getItem(i);
            if (item != null)
                model.addRow(new Object[]{item.row + 1 + "" + "ABCDE".charAt(item.col), item.name, item.price, item.purchasedDate});
        }
    }

    private void addTextField(String labelString, JTextField textField, JPanel container) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        JLabel label = new JLabel(labelString);

        panel.add(label);
        panel.add(textField);

        container.add(panel);
    }

    private void addAddItemsPanel() {
        addNameTextField = new JTextField(15);
        addTextField("Name", addNameTextField, addItemsPanel);

        addPriceTextField = new JTextField(15);
        addTextField("Price", addPriceTextField, addItemsPanel);

        addRowTextField = new JTextField(15);
        addTextField("Row", addRowTextField, addItemsPanel);

        addColTextField = new JTextField(15);
        addTextField("Col", addColTextField, addItemsPanel);

        addReasonTextField = new JTextField(15);
        addTextField("Reason", addReasonTextField, addItemsPanel);

        JButton addBtn = new JButton("Add");
        addBtn.setActionCommand("addQueuedItem");
        addBtn.addActionListener(new ButtonClickListener());

        errorMessage = new JLabel("");

        addItemsPanel.add(addBtn);
        addItemsPanel.add(errorMessage);
    }

    private void addRemoveItemsPanel() {
        removeRowTextField = new JTextField(15);
        addTextField("Row", removeRowTextField, removeItemsPanel);

        removeColTextField = new JTextField(15);
        addTextField("Col", removeColTextField, removeItemsPanel);

        removeReasonTextField = new JTextField(15);
        addTextField("Reason", removeReasonTextField, removeItemsPanel);

        JButton removeBtn = new JButton("Remove");
        removeBtn.setActionCommand("removeQueuedItem");
        removeBtn.addActionListener(new ButtonClickListener());

        errorMessage = new JLabel("");

        removeItemsPanel.add(removeBtn);
        removeItemsPanel.add(errorMessage);
    }

    private void addToolBarButton(String text, JPanel toolBar) {
        JButton btn = new JButton(text);
        btn.setActionCommand(text.replace(" ", ""));
        btn.addActionListener(new ButtonClickListener());
        toolBar.add(btn);
    }

    private void addVendingMachinePanel() {
        vendingMachineViewHeader = new JLabel("Selected Vending Machine: None", JLabel.CENTER);
        vendingMachineViewHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        vendingMachinePanel.add(vendingMachineViewHeader);
        JPanel toolBar = new JPanel();
        toolBar.setLayout(new FlowLayout());

        addToolBarButton("View Items", toolBar);
        addToolBarButton("View Purchased Items", toolBar);
        addToolBarButton("Add Items", toolBar);
        addToolBarButton("Remove Items", toolBar);
        addToolBarButton("Save", toolBar);

        vendingMachinePanel.add(toolBar);
    }

    public void show() {
        addToolbarItems();
        addTableClickListener();
        addVendingMachinePanel();
        addAddItemsPanel();
        addRemoveItemsPanel();
        mainFrame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (Objects.equals(command, "ViewItems"))
                vendingMachinePanelContentLayout.show(vendingMachinePanelContent, "viewItems");
            if (Objects.equals(command, "ViewPurchasedItems"))
                vendingMachinePanelContentLayout.show(vendingMachinePanelContent, "viewPurchasedItems");
            if (Objects.equals(command, "AddItems"))
                vendingMachinePanelContentLayout.show(vendingMachinePanelContent, "addItems");
            if (Objects.equals(command, "RemoveItems"))
                vendingMachinePanelContentLayout.show(vendingMachinePanelContent, "removeItems");
            if (Objects.equals(command, "Save"))
                machines.saveData();

            if (Objects.equals(command, "addQueuedItem")) {
                if (currentVendingMachine == null) {
                    errorMessage.setText("Must select vending machine before adding item");
                    return;
                }
                boolean error = false;

                String name = addNameTextField.getText();
                if (name.length() < 2) {
                    error = true;
                    errorMessage.setText("Please set name to more than 2 characters");
                }

                double price = 0;
                try {
                    price = Double.parseDouble(addPriceTextField.getText());
                } catch (NumberFormatException exception) {
                    errorMessage.setText("Please set price to a valid decimal");
                    error = true;
                }

                int row = 0;
                try {
                    row = Integer.parseInt(addRowTextField.getText()) - 1;
                    if (row < 0)
                        throw new NumberFormatException("Must be a positive integer");
                } catch (NumberFormatException exception) {
                    errorMessage.setText("Please set row to a valid positive integer");
                    error = true;
                }

                if (Objects.equals(addColTextField.getText().trim(), "")) {
                    errorMessage.setText("Column must not be empty");
                    error = true;
                }

                int col = "ABCDE".indexOf(addColTextField.getText());
                if (col < 0) {
                    errorMessage.setText("Valid columns are only A, B, C, D, and E");
                    error = true;
                }

                String reason = addReasonTextField.getText();
                if (reason.length() < 2) {
                    error = true;
                    errorMessage.setText("Please set reason to more than 2 characters");
                }

                if (error)
                    return;
                else
                    errorMessage.setText("");

                Item newItem = new Item(name, "0000/00/00", row, col);
                newItem.setPrice(price);
                int numberOfQueuedItemsWithSameRowAndCol = currentVendingMachine.queuedItems.getCountInRowCol(row, col);
                newItem.setSlot(numberOfQueuedItemsWithSameRowAndCol);

                QueuedItem queuedItem = new QueuedItem(newItem, reason, "Add");
                currentVendingMachine.queuedItems.add(queuedItem);
            }
            if (Objects.equals(command, "removeQueuedItem")) {
                if (currentVendingMachine == null) {
                    errorMessage.setText("Must select vending machine before removing item");
                    return;
                }
                boolean error = false;

                int row = 0;
                try {
                    row = Integer.parseInt(removeRowTextField.getText()) - 1;
                    if (row < 0)
                        throw new NumberFormatException("Must be a positive integer");
                } catch (NumberFormatException exception) {
                    errorMessage.setText("Please set row to a valid positive integer");
                    error = true;
                }

                int col = "ABCDE".indexOf(removeColTextField.getText());
                if (col < 0) {
                    errorMessage.setText("Valid columns are only A, B, C, D, and E");
                    error = true;
                }

                String reason = removeReasonTextField.getText();
                if (reason.length() < 2) {
                    error = true;
                    errorMessage.setText("Please set reason to more than 2 characters");
                }

                if (error)
                    return;
                else
                    errorMessage.setText("");

                ArrayList<Item> itemsToQueue = currentVendingMachine.inventory.getItems(row, col);
                for (Item item : itemsToQueue) {
                    currentVendingMachine.queuedItems.add(new QueuedItem(item, reason, "Remove"));
                }
            }
        }
    }
}
