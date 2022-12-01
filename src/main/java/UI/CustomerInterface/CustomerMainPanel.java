package UI.CustomerInterface;

import InventoryManager.Item;
import VendingMachine.VendingMachines;
import VendingMachine.VendingMachine;

import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CustomerMainPanel {
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JPanel toolBar;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private Item selectedItem;
    private VendingMachine machine;
    private JFormattedTextField purchaseField;
    private double currentPrice;

    public CustomerMainPanel() {
        machine = new VendingMachines("data/data.csv").getVendingMachineById(1);
        prepareGUI();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Vending Machine Project");
        mainFrame.setSize(1000, 1000);
        mainFrame.setLayout(new GridLayout(3, 1));

        headerLabel = new JLabel("", JLabel.CENTER);
        statusLabel = new JLabel("", JLabel.CENTER);
        purchaseField = new JFormattedTextField("");
        purchaseField.setColumns(20);
        toolBar = new JPanel();
        toolBar.setLayout(new FlowLayout(FlowLayout.CENTER));

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(machine.inventory.rows, machine.inventory.cols, 8, 8));

        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(toolBar);

        mainFrame.setVisible(true);
    }

    public void show() {
        headerLabel.setText("Selected Item: None");

        // make grid btns
        for (int row = 0; row < machine.inventory.rows; row++) {
            for (int col = 0; col < machine.inventory.cols; col++) {
                String rowCol = row + 1 + "" + machine.alphabet.charAt(col);
                JButton gridBtn = new JButton(rowCol);
                gridBtn.setActionCommand(row + "" + col);
                gridBtn.addActionListener(new ButtonClickListener());
                controlPanel.add(gridBtn);
            }
        }

        // add purchase btn
        JButton purchaseBtn = new JButton("Purchase Item");
        purchaseBtn.setActionCommand("purchase");
        purchaseBtn.addActionListener(new ButtonClickListener());

        purchaseField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                changePrice();
            }

            public void removeUpdate(DocumentEvent e) {
                changePrice();
            }

            public void insertUpdate(DocumentEvent e) {
                changePrice();
            }

            private void changePrice() {
                String input = purchaseField.getText();
                try {
                    currentPrice = Double.parseDouble(input);
                    statusLabel.setText("");
                } catch (NumberFormatException ex) {
                    statusLabel.setText("Please set price to valid decimal");
                }


            }
        });

        toolBar.add(purchaseField);
        toolBar.add(purchaseBtn);
        toolBar.add(statusLabel);

        mainFrame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (Objects.equals(command, "purchase")) {
                if (selectedItem == null)
                    statusLabel.setText("Cannot purchase item because none is selected");
                else
                    statusLabel.setText(machine.purchaseItem(selectedItem.row, selectedItem.col, currentPrice));
            } else {
                String[] commandData = command.split("");
                int row = Integer.parseInt(commandData[0]);
                int col = Integer.parseInt(commandData[1]);

                double price = machine.inventory.getPrice(row, col);
                selectedItem = machine.inventory.getItem(row, col, 0);

                if (selectedItem == null)
                    headerLabel.setText("Selected Item: None");
                else
                    headerLabel.setText(String.format("Selected Item: %s: $%.2f", selectedItem.name, price));
            }
        }
    }
}