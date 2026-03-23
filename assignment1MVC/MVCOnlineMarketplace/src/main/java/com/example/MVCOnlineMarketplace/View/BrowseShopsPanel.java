package com.example.MVCOnlineMarketplace.View;

import com.example.MVCOnlineMarketplace.Controller.OrderController;
import com.example.MVCOnlineMarketplace.Controller.ProductController;
import com.example.MVCOnlineMarketplace.Controller.ShopController;
import com.example.MVCOnlineMarketplace.Dto.*;
import com.example.MVCOnlineMarketplace.Security.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BrowseShopsPanel extends JPanel {

    private final ShopController shopController;
    private final OrderController orderController;
    private final ProductController productController;

    private JComboBox<String> shopComboBox;
    private List<ShopDto> shops;
    private List<ProductDto> currentProducts = new ArrayList<>();
    private JTable productTable;
    private DefaultTableModel tableModel;

    private JTextField filterField;
    private JComboBox<String> filterColCombo;
    private JComboBox<String> sortColCombo;
    private JComboBox<String> sortDirCombo;

    private List<OrderItemDto> cart = new ArrayList<>();

    private static final String[] FILTER_LABELS = {"Name", "Description", "Price"};
    private static final String[] FILTER_KEYS   = {"name", "description", "price"};

    public BrowseShopsPanel(ShopController shopController, OrderController orderController, ProductController productController) {
        this.shopController = shopController;
        this.orderController = orderController;
        this.productController = productController;
        setLayout(new BorderLayout());
        initUI();
        loadShops();
    }

    private void initUI() {
        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel shopSelectorPanel = new JPanel();
        shopSelectorPanel.add(new JLabel("Select a Store:"));
        shopComboBox = new JComboBox<>();
        shopComboBox.addActionListener(e -> loadProductsForSelectedShop());
        shopSelectorPanel.add(shopComboBox);
        topPanel.add(shopSelectorPanel, BorderLayout.NORTH);

        topPanel.add(buildFilterBar(), BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Description", "Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(productTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        if (UserSession.getInstance().isLoggedIn()) {
            JButton addToCartBtn = new JButton("Add Selected to Cart");
            JButton checkoutBtn = new JButton("Checkout");
            addToCartBtn.addActionListener(e -> addToCart());
            checkoutBtn.addActionListener(e -> checkout());
            bottomPanel.add(addToCartBtn);
            bottomPanel.add(checkoutBtn);
        } else {
            bottomPanel.add(new JLabel("Log in to purchase products."));
        }
        add(bottomPanel, BorderLayout.SOUTH);
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
        resetBtn.addActionListener(e -> { filterField.setText(""); loadProductsForSelectedShop(); });
        bar.add(resetBtn);
        return bar;
    }

    private void applyFilter() {
        int selectedIndex = shopComboBox.getSelectedIndex();
        if (selectedIndex < 0) return;
        long shopId = shops.get(selectedIndex).getId();
        String value = filterField.getText().trim();
        String column = FILTER_KEYS[filterColCombo.getSelectedIndex()];
        String sortBy = FILTER_KEYS[sortColCombo.getSelectedIndex()];
        boolean ascending = sortDirCombo.getSelectedIndex() == 0;
        currentProducts = productController.getFiltered(shopId, column, value, sortBy, ascending);
        tableModel.setRowCount(0);
        for (ProductDto p : currentProducts) {
            tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getDescription(), p.getPrice()});
        }
    }

    private void loadShops() {
        shops = shopController.getAllShops();
        for (ShopDto shop : shops) {
            shopComboBox.addItem(shop.getName() + " (ID: " + shop.getId() + ")");
        }
    }

    private void loadProductsForSelectedShop() {
        tableModel.setRowCount(0);
        if (filterField != null) filterField.setText("");
        int selectedIndex = shopComboBox.getSelectedIndex();
        if (selectedIndex < 0) return;
        long shopId = shops.get(selectedIndex).getId();
        currentProducts = productController.getFiltered(shopId, null, null, "name", true);
        for (ProductDto p : currentProducts) {
            tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getDescription(), p.getPrice()});
        }
    }

    private void addToCart() {
        int row = productTable.getSelectedRow();
        if (row < 0) return;
        long productId = (long) tableModel.getValueAt(row, 0);
        ProductDto selectedProduct = currentProducts.stream()
                .filter(p -> p.getId() == productId)
                .findFirst().orElse(null);
        if (selectedProduct != null) {
            cart.add(OrderItemDto.builder().productDto(selectedProduct).quantity(1).build());
            JOptionPane.showMessageDialog(this, selectedProduct.getName() + " added to cart! (Total items: " + cart.size() + ")");
        }
    }

    private void checkout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!");
            return;
        }
        BigDecimal total = cart.stream()
                .map(item -> item.getProductDto().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderDto newOrder = OrderDto.builder()
                .userDto(UserSession.getInstance().getCurrentUser())
                .orderItems(new ArrayList<>(cart))
                .totalPrice(total)
                .isPaid(true)
                .build();
        orderController.saveOrder(newOrder);
        cart.clear();
        JOptionPane.showMessageDialog(this, "Order placed successfully! Total: $" + total);
    }
}
