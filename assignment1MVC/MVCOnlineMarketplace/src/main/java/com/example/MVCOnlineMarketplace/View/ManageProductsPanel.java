package com.example.MVCOnlineMarketplace.View;

import com.example.MVCOnlineMarketplace.Controller.ProductController;
import com.example.MVCOnlineMarketplace.Dto.ProductDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class ManageProductsPanel extends JPanel {

    private final ProductController productController;
    private JTable productTable;
    private DefaultTableModel tableModel;

    private JTextField nameField;
    private JTextField descField;
    private JTextField priceField;
    private JTextField shopIdField;

    public ManageProductsPanel(ProductController productController) {
        this.productController = productController;
        setLayout(new BorderLayout());
        initUI();
        refreshTable();
    }

    private void initUI() {
        // --- Form Panel (Top - For adding new products) ---
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Product"));

        formPanel.add(new JLabel("  Product Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("  Description:"));
        descField = new JTextField();
        formPanel.add(descField);

        formPanel.add(new JLabel("  Price ($):"));
        priceField = new JTextField();
        formPanel.add(priceField);

        formPanel.add(new JLabel("  Shop ID (Required):"));
        shopIdField = new JTextField();
        formPanel.add(shopIdField);

        JButton addBtn = new JButton("Save Product");
        addBtn.addActionListener(e -> addProduct());
        formPanel.add(new JLabel()); // Empty spacer to align the button
        formPanel.add(addBtn);

        add(formPanel, BorderLayout.NORTH);

        // --- Table Panel (Center - For viewing existing products) ---
        String[] columns = {"ID", "Name", "Description", "Price", "Shop ID"};
        tableModel = new DefaultTableModel(columns, 0);
        productTable = new JTable(tableModel);
        add(new JScrollPane(productTable), BorderLayout.CENTER);

        // --- Action Panel (Bottom - For Refreshing and Deleting) ---
        JPanel actionPanel = new JPanel();

        JButton refreshBtn = new JButton("Refresh List");
        refreshBtn.addActionListener(e -> refreshTable());

        JButton deleteBtn = new JButton("Delete Selected Product");
        deleteBtn.setForeground(Color.RED);
        deleteBtn.addActionListener(e -> deleteSelectedProduct());

        actionPanel.add(refreshBtn);
        actionPanel.add(deleteBtn);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private void refreshTable() {
        tableModel.setRowCount(0); // Clear the table
        List<ProductDto> products = productController.getAllProducts();

        for (ProductDto p : products) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getDescription(),
                    "$" + p.getPrice(),
                    p.getShopId() != null ? p.getShopId() : "N/A"
            });
        }
    }

    private void addProduct() {
        try {
            // Validate inputs
            if (nameField.getText().trim().isEmpty() || priceField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Price are required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Build the DTO
            ProductDto dto = ProductDto.builder()
                    .name(nameField.getText().trim())
                    .description(descField.getText().trim())
                    .price(new BigDecimal(priceField.getText().trim()))
                    .shopId(shopIdField.getText().trim().isEmpty() ? null : Long.parseLong(shopIdField.getText().trim()))
                    .build();

            // Pass to the Controller
            productController.saveProduct(dto);

            // Refresh UI and clear fields
            refreshTable();
            nameField.setText("");
            descField.setText("");
            priceField.setText("");
            shopIdField.setText("");

            JOptionPane.showMessageDialog(this, "Product successfully saved!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for Price and Shop ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving product: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // ID is in the first column (index 0)
                long productId = (long) tableModel.getValueAt(selectedRow, 0);
                productController.deleteProduct(productId);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Product deleted.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product from the table to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }
}