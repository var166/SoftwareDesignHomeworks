package com.example.MVCOnlineMarketplace.View;

import com.example.MVCOnlineMarketplace.Controller.*;
import com.example.MVCOnlineMarketplace.Dto.UserDto;
import com.example.MVCOnlineMarketplace.Security.UserSession;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class MainFrame extends JFrame {

    // Injecting all necessary Controllers
    private final ProductController productController;
    private final ShopController shopController;
    private final OrderController orderController;
    private final UserController userController;

    private JPanel headerPanel;
    private JTabbedPane tabbedPane;

    public MainFrame(ProductController productController, ShopController shopController,
                     OrderController orderController, UserController userController) {
        this.productController = productController;
        this.shopController = shopController;
        this.orderController = orderController;
        this.userController = userController;

        setTitle("MVC Online Marketplace");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setLayout(new BorderLayout());

        refreshUI();
    }


    public void refreshUI() {
        getContentPane().removeAll();

        // --- Header (Dynamic Login/Logout) ---
        headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        if (UserSession.getInstance().isLoggedIn()) {
            String username = UserSession.getInstance().getCurrentUser().getUsername();
            String role = UserSession.getInstance().getCurrentUser().getRole();
            headerPanel.add(new JLabel("Welcome, " + username + " (" + role + ")"));

            JButton logoutBtn = new JButton("Logout");
            logoutBtn.addActionListener(e -> {
                UserSession.getInstance().logout();
                refreshUI();
            });
            headerPanel.add(logoutBtn);
        } else {
            headerPanel.add(new JLabel("Viewing as Guest"));
            JButton loginBtn = new JButton("Login");
            loginBtn.addActionListener(e -> showLoginDialog());
            headerPanel.add(loginBtn);
        }
        add(headerPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Browse Stores", new BrowseShopsPanel(shopController, orderController));

        if (UserSession.getInstance().isLoggedIn()) {
            tabbedPane.addTab("My Orders", new MyOrdersPanel(orderController));
        }

        if (UserSession.getInstance().isAdmin() || UserSession.getInstance().isStoreManager()) {
            tabbedPane.addTab("Manage Products (Admin)", new ManageProductsPanel(productController));
        }

        add(tabbedPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private void showLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Login", true);
        loginDialog.setSize(300, 150);
        loginDialog.setLocationRelativeTo(this);
        loginDialog.setLayout(new GridLayout(3, 2, 5, 5));

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Submit");

        loginDialog.add(new JLabel("  Username:"));
        loginDialog.add(userField);
        loginDialog.add(new JLabel("  Password:"));
        loginDialog.add(passField);
        loginDialog.add(new JLabel(""));
        loginDialog.add(loginBtn);

        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            UserDto user = userController.authenticate(username, password);

            if (user != null) {
                UserSession.getInstance().login(user);
                loginDialog.dispose();
                refreshUI();

            } else {
                JOptionPane.showMessageDialog(loginDialog,"Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginDialog.setVisible(true);
    }
}