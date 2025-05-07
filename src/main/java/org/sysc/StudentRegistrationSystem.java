package org.sysc;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class StudentRegistrationSystem extends JFrame implements ActionListener, ItemListener {

    // Form fields for student information
    private JTextField nameField;
    private JTextField ageField;

    // Checkboxes for courses
    private JCheckBox mathCheck;
    private JCheckBox physicsCheck;
    private JCheckBox chemistryCheck;

    // Checkboxes for additional services
    private JCheckBox transportCheck;
    private JCheckBox hostelCheck;

    // Buttons for fee calculation and clearing the form
    private JButton calcButton;
    private JButton clearButton;

    // Table to display currently selected courses and services
    private DefaultTableModel selectionTableModel;
    private JTable selectionTable;

    // Table to log each transaction (student registration)
    private DefaultTableModel transactionTableModel;
    private JTable transactionTable;

    // Arrays to store course and service names along with their fees
    private final String[] courseNames = {"Mathematics", "Physics", "Chemistry"};
    private final int[] courseFees = {1000, 1200, 1500};

    private final String[] serviceNames = {"Transportation", "Hostel Accommodation"};
    private final int[] serviceFees = {500, 1500};

    public StudentRegistrationSystem() {
        initComponents();
        setTitle("Student Registration and Fee Calculation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        // Create a tabbed pane for switching between registration and transaction log views
        JTabbedPane tabbedPane = new JTabbedPane();

        // ============================== Registration Panel ==============================
        JPanel registrationPanel = new JPanel(new BorderLayout());

        // --- Top panel: Student Information and Selection Options ---
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Student Information Panel
        JPanel studentInfoPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        studentInfoPanel.setBorder(new TitledBorder("Student Information"));
        studentInfoPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        studentInfoPanel.add(nameField);
        studentInfoPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        studentInfoPanel.add(ageField);

        // Selection Options Panel (Courses and Additional Services)
        JPanel selectionOptionsPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        // Courses Panel
        JPanel coursesPanel = new JPanel(new GridLayout(3, 1));
        coursesPanel.setBorder(new TitledBorder("Courses"));
        mathCheck = new JCheckBox(courseNames[0] + " (Fee: " + courseFees[0] + ")");
        physicsCheck = new JCheckBox(courseNames[1] + " (Fee: " + courseFees[1] + ")");
        chemistryCheck = new JCheckBox(courseNames[2] + " (Fee: " + courseFees[2] + ")");
        coursesPanel.add(mathCheck);
        coursesPanel.add(physicsCheck);
        coursesPanel.add(chemistryCheck);

        // Additional Services Panel
        JPanel servicesPanel = new JPanel(new GridLayout(2, 1));
        servicesPanel.setBorder(new TitledBorder("Additional Services"));
        transportCheck = new JCheckBox(serviceNames[0] + " (Fee: " + serviceFees[0] + ")");
        hostelCheck = new JCheckBox(serviceNames[1] + " (Fee: " + serviceFees[1] + ")");
        servicesPanel.add(transportCheck);
        servicesPanel.add(hostelCheck);

        // Adding courses and services panels into the selection options panel
        selectionOptionsPanel.add(coursesPanel);
        selectionOptionsPanel.add(servicesPanel);

        // Add both student info and selection options panels to the top panel
        topPanel.add(studentInfoPanel);
        topPanel.add(selectionOptionsPanel);

        registrationPanel.add(topPanel, BorderLayout.NORTH);

        // --- Middle panel: Selected Items JTable ---
        JPanel selectedItemsPanel = new JPanel(new BorderLayout());
        selectedItemsPanel.setBorder(new TitledBorder("Selected Items"));
        selectionTableModel = new DefaultTableModel(new Object[]{"Category", "Item", "Fee"}, 0);
        selectionTable = new JTable(selectionTableModel);
        JScrollPane selectedScrollPane = new JScrollPane(selectionTable);
        selectedItemsPanel.add(selectedScrollPane, BorderLayout.CENTER);
        registrationPanel.add(selectedItemsPanel, BorderLayout.CENTER);

        // --- Bottom panel: Action Buttons ---
        JPanel buttonPanel = new JPanel();
        calcButton = new JButton("Calculate Fee");
        clearButton = new JButton("Clear");
        calcButton.addActionListener(this);
        clearButton.addActionListener(this);
        buttonPanel.add(calcButton);
        buttonPanel.add(clearButton);
        registrationPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add item listeners to checkboxes to update selected items table dynamically
        mathCheck.addItemListener(this);
        physicsCheck.addItemListener(this);
        chemistryCheck.addItemListener(this);
        transportCheck.addItemListener(this);
        hostelCheck.addItemListener(this);

        // ============================== Transaction Log Panel ==============================
        JPanel transactionPanel = new JPanel(new BorderLayout());
        transactionPanel.setBorder(new TitledBorder("Transaction Log"));
        transactionTableModel = new DefaultTableModel(new Object[]{"Student Name", "Age", "Courses", "Services", "Total Fee"}, 0);
        transactionTable = new JTable(transactionTableModel);
        JScrollPane transactionScrollPane = new JScrollPane(transactionTable);
        transactionPanel.add(transactionScrollPane, BorderLayout.CENTER);

        // ----------------------- Add both tabs to the tabbed pane -----------------------
        tabbedPane.addTab("Registration", registrationPanel);
        tabbedPane.addTab("Transaction Log", transactionPanel);

        add(tabbedPane);
    }

    // -------------------------- ItemListener --------------------------
    // Updating the selected items table as checkboxes are toggled
    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getSource();

        if (source == mathCheck) {
            updateSelectionItem("Course", courseNames[0], courseFees[0], e.getStateChange() == ItemEvent.SELECTED);
        } else if (source == physicsCheck) {
            updateSelectionItem("Course", courseNames[1], courseFees[1], e.getStateChange() == ItemEvent.SELECTED);
        } else if (source == chemistryCheck) {
            updateSelectionItem("Course", courseNames[2], courseFees[2], e.getStateChange() == ItemEvent.SELECTED);
        } else if (source == transportCheck) {
            updateSelectionItem("Service", serviceNames[0], serviceFees[0], e.getStateChange() == ItemEvent.SELECTED);
        } else if (source == hostelCheck) {
            updateSelectionItem("Service", serviceNames[1], serviceFees[1], e.getStateChange() == ItemEvent.SELECTED);
        }
    }

    // Helper method to add or remove an item from the Selected Items table
    private void updateSelectionItem(String category, String item, int fee, boolean add) {
        if (add) {
            selectionTableModel.addRow(new Object[]{category, item, fee});
        } else {
            // Remove the row that matches the item name
            for (int i = 0; i < selectionTableModel.getRowCount(); i++) {
                if (selectionTableModel.getValueAt(i, 1).equals(item)) {
                    selectionTableModel.removeRow(i);
                    break;
                }
            }
        }
    }

    // -------------------------- ActionListener --------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == calcButton) {
            calculateFee();
        } else if (e.getSource() == clearButton) {
            clearForm();
        }
    }

    // Calculate and display the total fee; log transaction details
    private void calculateFee() {
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();

        // Input validation
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the student's name.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int totalFee = 0;
        StringBuilder coursesList = new StringBuilder();
        StringBuilder servicesList = new StringBuilder();

        for (int i = 0; i < selectionTableModel.getRowCount(); i++) {
            String category = selectionTableModel.getValueAt(i, 0).toString();
            String item = selectionTableModel.getValueAt(i, 1).toString();
            int fee = Integer.parseInt(selectionTableModel.getValueAt(i, 2).toString());
            totalFee += fee;

            if (category.equals("Course")) {
                if (coursesList.length() > 0) coursesList.append(", ");
                coursesList.append(item);
            } else if (category.equals("Service")) {
                if (servicesList.length() > 0) servicesList.append(", ");
                servicesList.append(item);
            }
        }

        // Display the total fee using a dialog
        JOptionPane.showMessageDialog(this, "The total fee for " + name + " is: " + totalFee,
                "Fee Calculation", JOptionPane.INFORMATION_MESSAGE);

        // Log the transaction details in the log table
        transactionTableModel.addRow(new Object[]{
                name,
                age,
                coursesList.toString(),
                servicesList.toString(),
                totalFee
        });
    }

    // Clear all form inputs and selected items
    private void clearForm() {
        nameField.setText("");
        ageField.setText("");
        mathCheck.setSelected(false);
        physicsCheck.setSelected(false);
        chemistryCheck.setSelected(false);
        transportCheck.setSelected(false);
        hostelCheck.setSelected(false);
        selectionTableModel.setRowCount(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentRegistrationSystem());
    }
}
