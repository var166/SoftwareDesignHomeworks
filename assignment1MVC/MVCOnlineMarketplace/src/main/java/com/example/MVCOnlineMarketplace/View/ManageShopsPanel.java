package com.example.MVCOnlineMarketplace.View;

import com.example.MVCOnlineMarketplace.Controller.ShopController;
import com.example.MVCOnlineMarketplace.Dto.ShopDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageShopsPanel extends JPanel {

    private final ShopController shopController;

    private JTable shopTable;
    private DefaultTableModel tableModel;

    private JTextField nameField;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField descField;
    private JTextField adminIdField;
    private JButton saveBtn;

    private JTextField filterField;
    private JComboBox<String> filterColCombo;
    private JComboBox<String> sortColCombo;
    private JComboBox<String> sortDirCombo;

    private long editingShopId = 0;

    private static final String[] FILTER_LABELS = {"Name", "Address", "Phone", "Email", "Description", "Admin ID"};
    private static final String[] FILTER_KEYS   = {"name", "address", "phone", "email", "description", "adminId"};

    public ManageShopsPanel(ShopController shopController) {
        this.shopController = shopController;
        setLayout(new BorderLayout());
        initUI();
        refreshTable();
    }

    private void initUI() {
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Shop Details"));

        formPanel.add(new JLabel("  Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("  Address:"));
        addressField = new JTextField();
        formPanel.add(addressField);

        formPanel.add(new JLabel("  Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(new JLabel("  Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("  Description:"));
        descField = new JTextField();
        formPanel.add(descField);

        formPanel.add(new JLabel("  Admin User ID:"));
        adminIdField = new JTextField();
        formPanel.add(adminIdField);

        saveBtn = new JButton("Create Shop");
        saveBtn.addActionListener(e -> saveShop());
        formPanel.add(new JLabel());
        formPanel.add(saveBtn);

        JButton clearBtn = new JButton("Clear / New");
        clearBtn.addActionListener(e -> clearForm());
        formPanel.add(new JLabel());
        formPanel.add(clearBtn);

        add(formPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Address", "Phone", "Email", "Description", "Admin ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        shopTable = new JTable(tableModel);
        shopTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        shopTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedRowIntoForm();
        });

        JPanel filterBar = buildFilterBar();

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(filterBar, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(shopTable), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh List");
        refreshBtn.addActionListener(e -> refreshTable());
        JButton deleteBtn = new JButton("Delete Selected Shop");
        deleteBtn.setForeground(Color.RED);
        deleteBtn.addActionListener(e -> deleteSelectedShop());
        actionPanel.add(refreshBtn);
        actionPanel.add(deleteBtn);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private JPanel buildFilterBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 3));
        bar.add(new JLabel("Filter:"));
        filterField = new JTextField(15);
        bar.add(filterField);
        bar.add(new JLabel("by:"));
        filterColCombo = new JComboBox<>(FILTER_LABELS);
        bar.add(filterColCombo);
        bar.add(new JLabel("Sort by:"));
        sortColCombo = new JComboBox<>(FILTER_LABELS);
        bar.add(sortColCombo);
        sortDirCombo = new JComboBox<>(new String[]{"Asc", "Desc"});
        bar.add(sortDirCombo);
        JButton applyBtn = new JButton("Apply");
        applyBtn.addActionListener(e -> applyFilter());
        bar.add(applyBtn);
        JButton resetBtn = new JButton("Reset");
        resetBtn.addActionListener(e -> { filterField.setText(""); refreshTable(); });
        bar.add(resetBtn);
        return bar;
    }

    private void applyFilter() {
        String value = filterField.getText().trim();
        String column = FILTER_KEYS[filterColCombo.getSelectedIndex()];
        String sortBy = FILTER_KEYS[sortColCombo.getSelectedIndex()];
        boolean ascending = sortDirCombo.getSelectedIndex() == 0;
        tableModel.setRowCount(0);
        List<ShopDto> shops = shopController.getFiltered(column, value, sortBy, ascending);
        for (ShopDto s : shops) {
            tableModel.addRow(new Object[]{
                    s.getId(), s.getName(), s.getAddress(), s.getPhone(),
                    s.getEmail(), s.getDescription(),
                    s.getAdminId() != null ? s.getAdminId() : "N/A"
            });
        }
    }

    private void loadSelectedRowIntoForm() {
        int row = shopTable.getSelectedRow();
        if (row < 0) return;

        editingShopId = (long) tableModel.getValueAt(row, 0);
        nameField.setText((String) tableModel.getValueAt(row, 1));
        addressField.setText(nullToEmpty(tableModel.getValueAt(row, 2)));
        phoneField.setText(nullToEmpty(tableModel.getValueAt(row, 3)));
        emailField.setText(nullToEmpty(tableModel.getValueAt(row, 4)));
        descField.setText(nullToEmpty(tableModel.getValueAt(row, 5)));
        Object adminVal = tableModel.getValueAt(row, 6);
        adminIdField.setText(adminVal instanceof Long ? String.valueOf(adminVal) : "");
        saveBtn.setText("Update Shop");
    }

    private String nullToEmpty(Object val) {
        return val == null || "N/A".equals(val) ? "" : val.toString();
    }

    private void clearForm() {
        editingShopId = 0;
        nameField.setText("");
        addressField.setText("");
        phoneField.setText("");
        emailField.setText("");
        descField.setText("");
        adminIdField.setText("");
        shopTable.clearSelection();
        saveBtn.setText("Create Shop");
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<ShopDto> shops = shopController.getAllShops();
        for (ShopDto s : shops) {
            tableModel.addRow(new Object[]{
                    s.getId(), s.getName(), s.getAddress(), s.getPhone(),
                    s.getEmail(), s.getDescription(),
                    s.getAdminId() != null ? s.getAdminId() : "N/A"
            });
        }
        clearForm();
    }

    private void saveShop() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Shop name is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Long adminId = adminIdField.getText().trim().isEmpty()
                    ? null : Long.parseLong(adminIdField.getText().trim());
            ShopDto dto = ShopDto.builder()
                    .id(editingShopId)
                    .name(nameField.getText().trim())
                    .address(addressField.getText().trim())
                    .phone(phoneField.getText().trim())
                    .email(emailField.getText().trim())
                    .description(descField.getText().trim())
                    .adminId(adminId)
                    .build();
            shopController.saveShop(dto);
            String msg = editingShopId > 0 ? "Shop updated!" : "Shop created!";
            refreshTable();
            JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Admin ID must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedShop() {
        int viewRow = shopTable.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a shop first.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this shop?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            long shopId = (long) tableModel.getValueAt(viewRow, 0);
            shopController.deleteShop(shopId);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Shop deleted.");
        }
    }
}
