package com.example.MVCOnlineMarketplace.View;

import com.example.MVCOnlineMarketplace.Controller.OrderController;
import com.example.MVCOnlineMarketplace.Dto.OrderDto;
import com.example.MVCOnlineMarketplace.Security.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MyOrdersPanel extends JPanel {

    private final OrderController orderController;
    private JTable orderTable;
    private DefaultTableModel tableModel;

    public MyOrdersPanel(OrderController orderController) {
        this.orderController = orderController;
        setLayout(new BorderLayout());
        initUI();
        loadMyOrders();
    }

    private void initUI() {
        // Create Table Model and Table
        String[] columns = {"Order ID", "Total Price", "Status", "Items Count"};
        tableModel = new DefaultTableModel(columns, 0);
        orderTable = new JTable(tableModel);

        // Add scroll pane for the table
        add(new JScrollPane(orderTable), BorderLayout.CENTER);

        // Add a refresh button at the bottom
        JPanel bottomPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh Orders");
        refreshBtn.addActionListener(e -> loadMyOrders());
        bottomPanel.add(refreshBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadMyOrders() {
        tableModel.setRowCount(0); // Clear existing data

        // Ensure a user is logged in before trying to fetch orders
        if (UserSession.getInstance().isLoggedIn()) {
            long currentUserId = UserSession.getInstance().getCurrentUser().getId();
            List<OrderDto> myOrders = orderController.getOrdersByUserId(currentUserId);

            for (OrderDto order : myOrders) {
                // Determine item count based on your DTO structure (using lineItems or orderItems)
                int itemCount = (order.getOrderItems() != null) ? order.getOrderItems().size() : 0;

                tableModel.addRow(new Object[]{
                        order.getId(),
                        "$" + order.getTotalPrice(),
                        order.isPaid() ? "Paid" : "Pending",
                        itemCount
                });
            }
        }
    }
}