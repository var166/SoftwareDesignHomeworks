package com.example.MVCOnlineMarketplace.View;

import com.example.MVCOnlineMarketplace.Controller.ProductController;
import com.example.MVCOnlineMarketplace.Dto.ProductDto;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.List;

public class ManageProductsPanel extends JPanel {

    private final ProductController productController;
    private final Long restrictedShopId;

    private JTable productTable;
    private DefaultTableModel tableModel;

    private JTextField nameField;
    private JTextField descField;
    private JTextField priceField;
    private JTextField shopIdField;
    private JButton saveBtn;
    private JButton clearBtn;

    private JTextField filterField;
    private JComboBox<String> filterColCombo;
    private JComboBox<String> sortColCombo;
    private JComboBox<String> sortDirCombo;

    private long editingProductId = 0;
    private List<ProductDto> currentProducts = new java.util.ArrayList<>();

    private static final String[] FILTER_LABELS = {"Name", "Description", "Price", "Shop ID"};
    private static final String[] FILTER_KEYS   = {"name", "description", "price", "shopId"};

    public ManageProductsPanel(ProductController productController, Long restrictedShopId) {
        this.productController = productController;
        this.restrictedShopId = restrictedShopId;
        setLayout(new BorderLayout());
        initUI();
        refreshTable();
    }

    private void initUI() {
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));

        formPanel.add(new JLabel("  Product Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("  Description:"));
        descField = new JTextField();
        formPanel.add(descField);

        formPanel.add(new JLabel("  Price ($):"));
        priceField = new JTextField();
        formPanel.add(priceField);

        formPanel.add(new JLabel("  Shop ID:"));
        shopIdField = new JTextField();
        if (restrictedShopId != null) {
            shopIdField.setText(String.valueOf(restrictedShopId));
            shopIdField.setEditable(false);
        }
        formPanel.add(shopIdField);

        saveBtn = new JButton("Save Product");
        saveBtn.addActionListener(e -> saveProduct());
        formPanel.add(new JLabel());
        formPanel.add(saveBtn);

        clearBtn = new JButton("Clear / New");
        clearBtn.addActionListener(e -> clearForm());
        formPanel.add(new JLabel());
        formPanel.add(clearBtn);

        add(formPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Description", "Price", "Shop ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedRowIntoForm();
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(buildFilterBar(), BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();

        JButton refreshBtn = new JButton("Refresh List");
        refreshBtn.addActionListener(e -> refreshTable());

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setForeground(Color.RED);
        deleteBtn.addActionListener(e -> deleteSelectedProduct());

        JButton exportJsonBtn = new JButton("Export JSON");
        exportJsonBtn.addActionListener(e -> exportProducts("json", "json", "JSON Files"));

        JButton exportXmlBtn = new JButton("Export XML");
        exportXmlBtn.addActionListener(e -> exportProducts("xml", "xml", "XML Files"));

        JButton exportCsvBtn = new JButton("Export CSV");
        exportCsvBtn.addActionListener(e -> exportProducts("csv", "csv", "CSV Files"));

        actionPanel.add(refreshBtn);
        actionPanel.add(deleteBtn);
        actionPanel.add(new JSeparator(SwingConstants.VERTICAL));
        actionPanel.add(exportJsonBtn);
        actionPanel.add(exportXmlBtn);
        actionPanel.add(exportCsvBtn);
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
        currentProducts = productController.getFiltered(restrictedShopId, column, value, sortBy, ascending);
        for (ProductDto p : currentProducts) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getName(), p.getDescription(),
                    "$" + p.getPrice(),
                    p.getShopId() != null ? p.getShopId() : "N/A"
            });
        }
    }

    private void loadSelectedRowIntoForm() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow < 0) return;

        editingProductId = (long) tableModel.getValueAt(selectedRow, 0);
        nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
        descField.setText((String) tableModel.getValueAt(selectedRow, 2));
        String rawPrice = (String) tableModel.getValueAt(selectedRow, 3);
        priceField.setText(rawPrice.replace("$", ""));
        Object shopIdVal = tableModel.getValueAt(selectedRow, 4);
        if (restrictedShopId == null) {
            shopIdField.setText(shopIdVal instanceof Long ? String.valueOf(shopIdVal) : "");
        }
        saveBtn.setText("Update Product");
    }

    private void clearForm() {
        editingProductId = 0;
        nameField.setText("");
        descField.setText("");
        priceField.setText("");
        if (restrictedShopId == null) shopIdField.setText("");
        productTable.clearSelection();
        saveBtn.setText("Save Product");
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        currentProducts = restrictedShopId != null
                ? productController.getProductsByShopId(restrictedShopId)
                : productController.getAllProducts();

        for (ProductDto p : currentProducts) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getDescription(),
                    "$" + p.getPrice(),
                    p.getShopId() != null ? p.getShopId() : "N/A"
            });
        }
        clearForm();
    }

    private void saveProduct() {
        try {
            if (nameField.getText().trim().isEmpty() || priceField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Price are required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Long shopId = restrictedShopId != null
                    ? restrictedShopId
                    : (shopIdField.getText().trim().isEmpty() ? null : Long.parseLong(shopIdField.getText().trim()));

            ProductDto dto = ProductDto.builder()
                    .id(editingProductId)
                    .name(nameField.getText().trim())
                    .description(descField.getText().trim())
                    .price(new BigDecimal(priceField.getText().trim()))
                    .shopId(shopId)
                    .build();

            productController.saveProduct(dto);

            String msg = editingProductId > 0 ? "Product updated successfully!" : "Product saved successfully!";
            refreshTable();
            JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for Price and Shop ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving product: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportProducts(String format, String extension, String description) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Export");
        fileChooser.setSelectedFile(new java.io.File("products." + extension));
        fileChooser.setFileFilter(new FileNameExtensionFilter(description, extension));
        int result = fileChooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;
        try {
            String content = productController.exportProducts(currentProducts, format);
            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                writer.write(content);
            }
            JOptionPane.showMessageDialog(this, "Exported successfully to " + fileChooser.getSelectedFile().getName(),
                    "Export", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage(),
                    "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
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
