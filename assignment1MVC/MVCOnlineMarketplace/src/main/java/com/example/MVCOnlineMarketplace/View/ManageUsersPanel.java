package com.example.MVCOnlineMarketplace.View;

import com.example.MVCOnlineMarketplace.Controller.UserController;
import com.example.MVCOnlineMarketplace.Dto.UserDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageUsersPanel extends JPanel {

    private static final String[] ROLES = {"User", "StoreManager", "Admin"};
    private static final String[] FILTER_LABELS = {"Username", "Email", "Role"};
    private static final String[] FILTER_KEYS   = {"username", "email", "role"};

    private final UserController userController;

    private JTable userTable;
    private DefaultTableModel tableModel;

    private JTextField usernameField;
    private JTextField emailField;
    private JTextField passwordField;
    private JComboBox<String> roleCombo;
    private JButton saveBtn;

    private JTextField filterField;
    private JComboBox<String> filterColCombo;
    private JComboBox<String> sortColCombo;
    private JComboBox<String> sortDirCombo;

    private long editingUserId = 0;

    public ManageUsersPanel(UserController userController) {
        this.userController = userController;
        setLayout(new BorderLayout());
        initUI();
        refreshTable();
    }

    private void initUI() {
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("User Details"));

        formPanel.add(new JLabel("  Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("  Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("  Password:"));
        passwordField = new JTextField();
        formPanel.add(passwordField);

        formPanel.add(new JLabel("  Role:"));
        roleCombo = new JComboBox<>(ROLES);
        formPanel.add(roleCombo);

        saveBtn = new JButton("Create User");
        saveBtn.addActionListener(e -> saveUser());
        formPanel.add(new JLabel());
        formPanel.add(saveBtn);

        JButton clearBtn = new JButton("Clear / New");
        clearBtn.addActionListener(e -> clearForm());
        formPanel.add(new JLabel());
        formPanel.add(clearBtn);

        add(formPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Username", "Email", "Role"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedRowIntoForm();
        });

        JPanel filterBar = buildFilterBar();

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(filterBar, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(userTable), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshTable());
        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setForeground(Color.RED);
        deleteBtn.addActionListener(e -> deleteSelectedUser());
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
        List<UserDto> users = userController.getFiltered(column, value, sortBy, ascending);
        for (UserDto u : users) {
            tableModel.addRow(new Object[]{u.getId(), u.getUsername(), u.getEmail(), u.getRole()});
        }
    }

    private void loadSelectedRowIntoForm() {
        int row = userTable.getSelectedRow();
        if (row < 0) return;

        editingUserId = (long) tableModel.getValueAt(row, 0);
        usernameField.setText((String) tableModel.getValueAt(row, 1));
        emailField.setText((String) tableModel.getValueAt(row, 2));
        passwordField.setText("");
        roleCombo.setSelectedItem(tableModel.getValueAt(row, 3));
        saveBtn.setText("Update User");
    }

    private void clearForm() {
        editingUserId = 0;
        usernameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        roleCombo.setSelectedIndex(0);
        userTable.clearSelection();
        saveBtn.setText("Create User");
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<UserDto> users = userController.getAllUsers();
        for (UserDto u : users) {
            tableModel.addRow(new Object[]{u.getId(), u.getUsername(), u.getEmail(), u.getRole()});
        }
        clearForm();
    }

    private void saveUser() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String role = (String) roleCombo.getSelectedItem();

        if (username.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and Email are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (editingUserId == 0 && password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password is required for new users.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String resolvedPassword = password;
            if (editingUserId > 0 && password.isEmpty()) {
                UserDto existing = userController.findById(editingUserId);
                resolvedPassword = existing != null ? existing.getPassword() : "";
            }
            UserDto dto = UserDto.builder()
                    .id(editingUserId)
                    .username(username)
                    .email(email)
                    .password(resolvedPassword)
                    .role(role)
                    .build();
            userController.saveUser(dto);
            String msg = editingUserId > 0 ? "User updated." : "User created.";
            refreshTable();
            JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedUser() {
        int viewRow = userTable.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a user first.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            long userId = (long) tableModel.getValueAt(viewRow, 0);
            try {
                userController.deleteUserById(userId);
                refreshTable();
                JOptionPane.showMessageDialog(this, "User deleted.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
