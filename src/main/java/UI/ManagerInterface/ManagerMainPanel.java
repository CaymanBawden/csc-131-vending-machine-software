package UI.ManagerInterface;

import VendingMachine.VendingMachines;
import VendingMachine.VendingMachine;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class ManagerMainPanel {
    private JFrame mainFrame;
    private JTable machineTable;
    private String[][] tableData;
    private String[] columnNames;
    private JPanel vendingMachinePanel;
    VendingMachines machines;

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
        mainFrame.setLayout(new GridLayout(3, 1));

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        machineTable = new JTable(tableData, columnNames);
        tableData = null;
        machineTable.setDefaultEditor(Object.class, null);
        machineTable.setAutoCreateRowSorter(true);

        JScrollPane sp = new JScrollPane(machineTable);
        sp.setSize(1000, 400);

        vendingMachinePanel = new JPanel();

        mainFrame.add(sp);
        mainFrame.add(vendingMachinePanel);

        mainFrame.setVisible(true);
    }

    public String[] getRowAt(int row) {
        String[] result = new String[columnNames.length];

        for (int i = 0; i < columnNames.length; i++) {
            result[i] = (String) machineTable.getModel().getValueAt(row, i);
        }

        return result;
    }

    public void show() {
        machineTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = machineTable.rowAtPoint(evt.getPoint());
                int col = machineTable.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    int id = Integer.parseInt(getRowAt(row)[0]);
                    VendingMachine machine = machines.getVendingMachineById(id);
                }
            }
        });
        JLabel idLabel = new JLabel("1");
        vendingMachinePanel.add(idLabel);
    }
}