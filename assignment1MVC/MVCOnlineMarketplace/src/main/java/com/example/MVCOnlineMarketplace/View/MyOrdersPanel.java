package com.example.MVCOnlineMarketplace.View;

import com.example.MVCOnlineMarketplace.Controller.OrderController;
import com.example.MVCOnlineMarketplace.Dto.OrderDto;
import com.example.MVCOnlineMarketplace.Dto.OrderItemDto;
import com.example.MVCOnlineMarketplace.Security.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class MyOrdersPanel extends JPanel {

    private final OrderController orderController;

    private JTable orderTable;
    private DefaultTableModel orderTableModel;

    private JTable itemsTable;
    private DefaultTableModel itemsTableModel;

    private JTextField filterField;
    private JComboBox<String> filterColCombo;
    private JComboBox<String> sortColCombo;
    private JComboBox<String> sortDirCombo;

    private List<OrderDto> currentOrders;

    private static final String[] FILTER_LABELS = {"Total Price", "Status"};
    private static final String[] FILTER_KEYS   = {"totalPrice", "isPaid"};

    public MyOrdersPanel(OrderController orderController) {
        this.orderController = orderController;
        setLayout(new BorderLayout());
        initUI();
        loadMyOrders();
    }

    private void initUI() {
        String[] orderColumns = {"Order ID", "Total Price", "Status", "Items Count"};
        orderTableModel = new DefaultTableModel(orderColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        orderTable = new JTable(orderTableModel);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadItemsForSelectedOrder();
        });

        String[] itemColumns = {"Product Name", "Price", "Qty", "Subtotal"};
        itemsTableModel = new DefaultTableModel(itemColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        itemsTable = new JTable(itemsTableModel);

        JPanel filterBar = buildFilterBar();

        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.add(filterBar, BorderLayout.NORTH);
        orderPanel.add(new JScrollPane(orderTable), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                orderPanel, new JScrollPane(itemsTable));
        splitPane.setResizeWeight(0.55);
        add(splitPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh Orders");
        refreshBtn.addActionListener(e -> loadMyOrders());
        bottomPanel.add(refreshBtn);
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
        resetBtn.addActionListener(e -> { filterField.setText(""); loadMyOrders(); });
        bar.add(resetBtn);
        return bar;
    }

    private void applyFilter() {
        if (!UserSession.getInstance().isLoggedIn()) return;
        String value = filterField.getText().trim();
        String column = FILTER_KEYS[filterColCombo.getSelectedIndex()];
        String sortBy = FILTER_KEYS[sortColCombo.getSelectedIndex()];
        boolean ascending = sortDirCombo.getSelectedIndex() == 0;
        long userId = UserSession.getInstance().getCurrentUser().getId();
        currentOrders = orderController.getFilteredByUserId(userId, column, value, sortBy, ascending);
        orderTableModel.setRowCount(0);
        itemsTableModel.setRowCount(0);
        for (OrderDto order : currentOrders) {
            int itemCount = order.getOrderItems() != null ? order.getOrderItems().size() : 0;
            orderTableModel.addRow(new Object[]{
                    order.getId(), "$" + order.getTotalPrice(),
                    order.isPaid() ? "Paid" : "Pending", itemCount
            });
        }
    }

    private void loadMyOrders() {
        orderTableModel.setRowCount(0);
        itemsTableModel.setRowCount(0);
        if (!UserSession.getInstance().isLoggedIn()) return;

        long currentUserId = UserSession.getInstance().getCurrentUser().getId();
        currentOrders = orderController.getOrdersByUserId(currentUserId);

        for (OrderDto order : currentOrders) {
            int itemCount = order.getOrderItems() != null ? order.getOrderItems().size() : 0;
            orderTableModel.addRow(new Object[]{
                    order.getId(),
                    "$" + order.getTotalPrice(),
                    order.isPaid() ? "Paid" : "Pending",
                    itemCount
            });
        }
    }

    private void loadItemsForSelectedOrder() {
        itemsTableModel.setRowCount(0);
        int row = orderTable.getSelectedRow();
        if (row < 0 || currentOrders == null) return;

        OrderDto order = currentOrders.get(row);
        if (order.getOrderItems() == null) return;

        for (OrderItemDto item : order.getOrderItems()) {
            String name = item.getProductDto() != null ? item.getProductDto().getName() : "N/A";
            String price = item.getProductDto() != null ? "$" + item.getProductDto().getPrice() : "N/A";
            int qty = item.getQuantity();
            String subtotal = item.getProductDto() != null
                    ? "$" + item.getProductDto().getPrice().multiply(new BigDecimal(qty)) : "N/A";
            itemsTableModel.addRow(new Object[]{name, price, qty, subtotal});
        }
    }
}
