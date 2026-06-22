package com.example.MVCOnlineMarketplace.View;

import com.example.MVCOnlineMarketplace.Controller.*;
import com.example.MVCOnlineMarketplace.Dto.ShopDto;
import com.example.MVCOnlineMarketplace.Dto.UserDto;
import com.example.MVCOnlineMarketplace.Dto.UserRegistrationDto;
import com.example.MVCOnlineMarketplace.Security.UserSession;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

@Component
public class MainFrame extends JFrame {

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
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        refreshUI();
    }

    public void refreshUI() {
        getContentPane().removeAll();

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

            JButton registerBtn = new JButton("Register");
            registerBtn.addActionListener(e -> showRegisterDialog());
            headerPanel.add(registerBtn);
        }
        add(headerPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Browse Stores", new BrowseShopsPanel(shopController, orderController, productController));

        if (UserSession.getInstance().isLoggedIn()) {
            tabbedPane.addTab("My Orders", new MyOrdersPanel(orderController));
        }

        if (UserSession.getInstance().isStoreManager()) {
            long currentUserId = UserSession.getInstance().getCurrentUser().getId();
            Optional<ShopDto> managedShop = shopController.getShopByAdminId(currentUserId);
            if (managedShop.isPresent()) {
                tabbedPane.addTab("Manage Products", new ManageProductsPanel(productController, managedShop.get().getId()));
            }
        }

        if (UserSession.getInstance().isAdmin()) {
            tabbedPane.addTab("Manage Products", new ManageProductsPanel(productController, null));
            tabbedPane.addTab("Manage Shops", new ManageShopsPanel(shopController));
            tabbedPane.addTab("Manage Users", new ManageUsersPanel(userController));
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
                JOptionPane.showMessageDialog(loginDialog, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginDialog.setVisible(true);
    }

    private void showRegisterDialog() {
        JDialog registerDialog = new JDialog(this, "Register", true);
        registerDialog.setSize(320, 200);
        registerDialog.setLocationRelativeTo(this);
        registerDialog.setLayout(new GridLayout(4, 2, 5, 5));

        JTextField usernameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton registerBtn = new JButton("Create Account");

        registerDialog.add(new JLabel("  Username:"));
        registerDialog.add(usernameField);
        registerDialog.add(new JLabel("  Email:"));
        registerDialog.add(emailField);
        registerDialog.add(new JLabel("  Password:"));
        registerDialog.add(passField);
        registerDialog.add(new JLabel(""));
        registerDialog.add(registerBtn);

        registerBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(registerDialog, "All fields are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                UserRegistrationDto dto = UserRegistrationDto.builder()
                        .username(username)
                        .email(email)
                        .password(password)
                        .build();
                UserDto newUser = userController.register(dto);
                UserSession.getInstance().login(newUser);
                registerDialog.dispose();
                refreshUI();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(registerDialog, "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerDialog.setVisible(true);
    }
}
