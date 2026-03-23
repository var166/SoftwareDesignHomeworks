package com.example.MVCOnlineMarketplace.View;

import com.example.MVCOnlineMarketplace.Controller.OrderController;
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
    private JComboBox<String> shopComboBox;
    private List<ShopDto> shops;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private List<OrderItemDto> cart = new ArrayList<>(); // Simple cart state

    public BrowseShopsPanel(ShopController shopController, OrderController orderController) {
        this.shopController = shopController;
        this.orderController = orderController;
        setLayout(new BorderLayout());
        initUI();
        loadShops();
    }

    private void initUI() {
        // Top: Shop Selector
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select a Store:"));
        shopComboBox = new JComboBox<>();
        shopComboBox.addActionListener(e -> loadProductsForSelectedShop());
        topPanel.add(shopComboBox);
        add(topPanel, BorderLayout.NORTH);

        // Center: Product Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Description", "Price"}, 0);
        productTable = new JTable(tableModel);
        add(new JScrollPane(productTable), BorderLayout.CENTER);

        // Bottom: Cart Actions (Only for Logged In Users)
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

    private void loadShops() {
        shops = shopController.getAllShops();
        for (ShopDto shop : shops) {
            shopComboBox.addItem(shop.getName() + " (ID: " + shop.getId() + ")");
        }
    }

    private void loadProductsForSelectedShop() {
        tableModel.setRowCount(0);
        int selectedIndex = shopComboBox.getSelectedIndex();
        if (selectedIndex >= 0) {
            ShopDto selectedShop = shops.get(selectedIndex);
            if (selectedShop.getProducts() != null) {
                for (ProductDto p : selectedShop.getProducts()) {
                    tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getDescription(), p.getPrice()});
                }
            }
        }
    }

    private void addToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            int shopIdx = shopComboBox.getSelectedIndex();
            ProductDto selectedProduct = shops.get(shopIdx).getProducts().get(selectedRow);

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