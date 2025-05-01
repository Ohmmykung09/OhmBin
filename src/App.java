import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class App {
    private VendingMachine vm;
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JList<String> cartList;
    private DefaultListModel<String> cartModel;
    private DefaultListModel<String> productsModel; // Declare productsModel here
    private JLabel totalPriceLabel;
    private JPanel productGridPanel;
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color ACCENT_COLOR = new Color(52, 152, 219);
    private final Color BG_COLOR = new Color(236, 240, 241);
    private final Color TEXT_COLOR = new Color(44, 62, 80);
    private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private final Font REGULAR_FONT = new Font("Arial", Font.PLAIN, 14);
    private final String PASSWORD = "admin123"; // Simple password for admin access

    public App(VendingMachine vm) {
        this.vm = vm;
        this.frame = new JFrame("Modern Vending Machine");
        this.cartModel = new DefaultListModel<>();
        this.productsModel = new DefaultListModel<>();
        setupFrame();
    }

    private void setupFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.getContentPane().setBackground(BG_COLOR);

        // Create card layout for switching between customer and admin views
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BG_COLOR);

        // Create customer panel
        JPanel customerPanel = createCustomerPanel();

        // Create admin panel
        JPanel adminPanel = createAdminPanel();

        // Add panels to card layout
        cardPanel.add(customerPanel, "customer");
        cardPanel.add(adminPanel, "admin");

        frame.add(cardPanel);
    }

    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Vending Machine");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);

        JButton adminButton = createStyledButton("Admin", ACCENT_COLOR);
        adminButton.addActionListener(e -> promptAdminPassword());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(adminButton, BorderLayout.EAST);

        // Products Panel
        JPanel productsContainer = new JPanel(new BorderLayout());
        productsContainer.setBackground(BG_COLOR);
        productsContainer.setBorder(createRoundedBorder("Products", 10));

        productGridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        productGridPanel.setBackground(BG_COLOR);
        productGridPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        updateProductDisplay();

        JScrollPane productScroll = new JScrollPane(productGridPanel);
        productScroll.setBorder(null);
        productScroll.getVerticalScrollBar().setUnitIncrement(16);
        productsContainer.add(productScroll, BorderLayout.CENTER);

        // Cart Panel
        JPanel cartContainer = new JPanel(new BorderLayout());
        cartContainer.setBackground(BG_COLOR);
        cartContainer.setBorder(createRoundedBorder("Your Cart", 10));
        cartContainer.setPreferredSize(new Dimension(300, 0));

        cartList = new JList<>(cartModel);
        cartList.setFont(REGULAR_FONT);
        cartList.setBackground(Color.WHITE);
        cartList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane cartScroll = new JScrollPane(cartList);
        cartScroll.setBorder(null);

        JPanel cartControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cartControlPanel.setBackground(BG_COLOR);

        JButton plusBtn = createStyledButton("+", ACCENT_COLOR);
        JButton minusBtn = createStyledButton("-", ACCENT_COLOR);
        JButton deleteBtn = createStyledButton("Remove", ACCENT_COLOR);

        plusBtn.addActionListener(e -> {
            int idx = cartList.getSelectedIndex();
            if (idx <= 0) {
                JOptionPane.showMessageDialog(frame, "Please select an item to increase quantity.", "Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Product p = vm.getCart().getAllItems().get(idx - 1);
            Product originalProduct = vm.getAllProducts().stream()
                    .filter(prod -> prod.getId() == p.getId())
                    .findFirst()
                    .orElse(null);

            if (originalProduct != null && p.getQuantity() < originalProduct.getQuantity()) {
                p.addQuantity(1);
                vm.getCart().calculatePrice();
                updateCartDisplay();
            } else {
                JOptionPane.showMessageDialog(frame, "Not enough stock available!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        minusBtn.addActionListener(e -> {
            int idx = cartList.getSelectedIndex();
            if (idx <= 0)
                return;

            Product p = vm.getCart().getAllItems().get(idx - 1);
            if (p.getQuantity() > 1) {
                p.addQuantity(-1);
                vm.getCart().calculatePrice();
            } else {
                vm.getCart().removeFromCart(p);
            }
            updateCartDisplay();
        });

        deleteBtn.addActionListener(e -> {
            int idx = cartList.getSelectedIndex();
            if (idx <= 0) {
                JOptionPane.showMessageDialog(frame, "Please select an item to remove.", "Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Product p = vm.getCart().getAllItems().get(idx - 1);
            vm.getCart().removeFromCart(p);
            updateCartDisplay();
        });

        cartControlPanel.add(plusBtn);
        cartControlPanel.add(minusBtn);
        cartControlPanel.add(deleteBtn);

        totalPriceLabel = new JLabel("Total: 0 THB");
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPriceLabel.setForeground(TEXT_COLOR);
        totalPriceLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton checkoutBtn = createStyledButton("Checkout", PRIMARY_COLOR);
        checkoutBtn.setFont(new Font("Arial", Font.BOLD, 16));
        checkoutBtn.addActionListener(e -> {
            if (vm.getCart().getAllItems().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Your cart is empty!", "Checkout", JOptionPane.WARNING_MESSAGE);
            } else {
                vm.cashOut();
                updateCartDisplay();
                updateProductDisplay();
                updateProductsList(productsModel);
                JOptionPane.showMessageDialog(frame, "Purchase successful! Thank you.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel checkoutPanel = new JPanel(new BorderLayout());
        checkoutPanel.setBackground(BG_COLOR);
        checkoutPanel.add(totalPriceLabel, BorderLayout.WEST);
        checkoutPanel.add(checkoutBtn, BorderLayout.EAST);

        cartContainer.add(cartScroll, BorderLayout.CENTER);
        cartContainer.add(cartControlPanel, BorderLayout.NORTH);
        cartContainer.add(checkoutPanel, BorderLayout.SOUTH);

        updateCartDisplay();

        // Assemble panels
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(BG_COLOR);
        contentPanel.add(productsContainer, BorderLayout.CENTER);
        contentPanel.add(cartContainer, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    // warning: this method is not used in the code
    private void showProductDialog(Product product, DefaultListModel<String> productsModel) {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBackground(BG_COLOR);

        JTextField nameField = new JTextField(product != null ? product.getName() : "");
        JTextField priceField = new JTextField(product != null ? String.valueOf(product.getPrice()) : "");
        JTextField quantityField = new JTextField(product != null ? String.valueOf(product.getQuantity()) : "");

        // Priority dropdown with text labels
        String[] priorities = { "Common Item", "Hot Seller", "New Item" };
        JComboBox<String> priorityDropdown = new JComboBox<>(priorities);
        if (product != null) {
            priorityDropdown.setSelectedIndex(product.getPriority());
        }

        panel.add(new JLabel("Product Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Product Price (THB):"));
        panel.add(priceField);
        panel.add(new JLabel("Product Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Product Priority:"));
        panel.add(priorityDropdown);

        int result = JOptionPane.showConfirmDialog(frame, panel,
                product == null ? "Add New Product" : "Edit Product",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = product != null ? product.getId() : vm.getAllProducts().size() + 1; // Generate new ID
                String name = nameField.getText().trim();
                int price = Integer.parseInt(priceField.getText().trim());
                int quantity = Integer.parseInt(quantityField.getText().trim());
                int priority = priorityDropdown.getSelectedIndex(); // Map selected index to integer priority

                if (product == null) {
                    // Add new product
                    vm.addProduct(price, name, quantity, priority);
                } else {
                    // Edit existing product
                    vm.editProduct(id, price, name, quantity, priority);
                }
                updateProductDisplay();
                updateProductsList(productsModel);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers for price, quantity, and priority.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(192, 57, 43)); // Different color for admin
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Admin Control Panel");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);

        JButton backButton = createStyledButton("Back to Shop", new Color(231, 76, 60));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "customer"));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        // Admin Content Panel
        JPanel adminContent = new JPanel(new BorderLayout(15, 15));
        adminContent.setBackground(BG_COLOR);

        // Products List Panel
        JPanel productsListPanel = new JPanel(new BorderLayout());
        productsListPanel.setBackground(BG_COLOR);
        productsListPanel.setBorder(createRoundedBorder("Product Inventory", 10));

        JList<String> productsList = new JList<>(productsModel);
        productsList.setFont(REGULAR_FONT);
        productsList.setBackground(Color.WHITE);

        updateProductsList(productsModel);

        JScrollPane productsScroll = new JScrollPane(productsList);
        productsScroll.setBorder(null);

        productsListPanel.add(productsScroll, BorderLayout.CENTER);

        // Action Panel
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(BG_COLOR);
        actionPanel.setBorder(createRoundedBorder("Actions", 10));

        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonsPanel.setBackground(BG_COLOR);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton addButton = createStyledButton("Add New Product", new Color(39, 174, 96));
        JButton editButton = createStyledButton("Edit Selected Product", new Color(41, 128, 185));
        JButton removeButton = createStyledButton("Remove Selected Product", new Color(192, 57, 43));

        addButton.addActionListener(e -> {
            showProductDialog(null, productsModel);
        });

        editButton.addActionListener(e -> {
            int selectedIndex = productsList.getSelectedIndex();
            if (selectedIndex >= 0) {
                Product p = vm.getAllProducts().get(selectedIndex);
                showProductDialog(p, productsModel); // Pass the selected product to the dialog
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a product to edit.",
                        "Selection Required", JOptionPane.WARNING_MESSAGE);
            }
        });

        removeButton.addActionListener(e -> {
            int selectedIndex = productsList.getSelectedIndex();
            if (selectedIndex >= 0) {
                Product p = vm.getAllProducts().get(selectedIndex);
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to remove " + p.getName() + "?",
                        "Confirm Removal", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    vm.removeProduct(p.getName());
                    updateProductDisplay();
                    updateProductsList(productsModel);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a product to remove.",
                        "Selection Required", JOptionPane.WARNING_MESSAGE);
            }
        });

        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(removeButton);

        actionPanel.add(buttonsPanel, BorderLayout.CENTER);

        // Assemble panels
        adminContent.add(productsListPanel, BorderLayout.CENTER);
        adminContent.add(actionPanel, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(adminContent, BorderLayout.CENTER);

        return panel;
    }

    // Helper method to create a styled JButton
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(REGULAR_FONT);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    // Helper method to create rounded title border
    private Border createRoundedBorder(String title, int radius) {
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
                title);
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 16));
        titledBorder.setTitleColor(TEXT_COLOR);
        return titledBorder;
    }

    // Create product button with image placeholder, name, price, stock, and
    // priority
    private JButton createProductButton(Product product) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout(5, 5));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true));
        button.setFocusPainted(false);

        // Product icon panel
        JPanel iconPanel = new JPanel();
        iconPanel.setBackground(ACCENT_COLOR);
        iconPanel.setPreferredSize(new Dimension(0, 80));

        // Product info panel
        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(TEXT_COLOR);

        JLabel priceLabel = new JLabel(product.getPrice() + " THB");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        priceLabel.setForeground(TEXT_COLOR);

        JLabel stockLabel = new JLabel("Stock: " + product.getQuantity());
        stockLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        stockLabel.setForeground(new Color(149, 165, 166));

        // Product priority label
        JLabel priorityLabel = new JLabel(getPrioritylevel(product.getPriority()));
        priorityLabel.setFont(new Font("Arial", Font.BOLD, 12));
        priorityLabel.setForeground(getBgColor(product.getPriority()));

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(stockLabel);
        infoPanel.add(priorityLabel);

        button.add(iconPanel, BorderLayout.NORTH);
        button.add(infoPanel, BorderLayout.CENTER);

        if (product.getQuantity() <= 0) {
            button.setBackground(new Color(220, 220, 220));
            button.setEnabled(false);
            nameLabel.setForeground(new Color(128, 128, 128));
            priceLabel.setForeground(new Color(128, 128, 128));
            stockLabel.setForeground(new Color(128, 128, 128));
            priorityLabel.setForeground(new Color(128, 128, 128));
        }

        button.addActionListener(e -> {
            if (product.getQuantity() <= 0) {
                JOptionPane.showMessageDialog(frame, "This product is out of stock!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                vm.AddToCart(product);
                updateCartDisplay();
            }
        });

        return button;
    }

    public String getPrioritylevel(int priority) {
        switch (priority) {
            case 0:
                return "Common Item";
            case 1:
                return "Hot Seller";
            case 2:
                return "New Item";
            case -1:
                return "Out of Stock";
            default:
                return "Unknown";
        }
    }

    // Update product buttons in the grid
    public void updateProductDisplay() {
        if (productGridPanel != null) {
            productGridPanel.removeAll();
            vm.getAllProducts();
            List<Product> products = vm.getAllProducts();
            for (Product product : products) {
                productGridPanel.add(createProductButton(product));
            }

            productGridPanel.revalidate();
            productGridPanel.repaint();
        }
    }

    // Update the admin products list
    private void updateProductsList(DefaultListModel<String> model) {
        model.clear();
        List<Product> products = vm.getAllProducts();
        for (Product product : products) {
            model.addElement(product.getName() + " - " + product.getPrice() + " THB - Stock: " +
                    product.getQuantity() + " - Priority: " + product.getPriority());
        }
    }

    public Color getBgColor(int priority) {
        switch (priority) {
            case 0:
                return new Color(52, 152, 219); // Common Item
            case 1:
                return new Color(241, 72, 15); // Hot Seller
            case 2:
                return new Color(46, 204, 113); // New Item
            default:
                return BG_COLOR; // Default color
        }

    }

    // Update cart display
    public void updateCartDisplay() {
        cartModel.clear();
        cartModel.addElement("Total: " + vm.getCart().getSumPrice() + " THB");

        List<Product> cartItems = vm.getCart().getAllItems();
        for (Product product : cartItems) {
            cartModel.addElement(
                    product.getName() + " - " + product.getQuantity() + " x " + product.getPrice() + " THB");
        }

        totalPriceLabel.setText("Total: " + vm.getCart().getSumPrice() + " THB");
    }

    // Prompt for admin password
    private void promptAdminPassword() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(REGULAR_FONT);

        int result = JOptionPane.showConfirmDialog(frame, passwordField,
                "Enter Admin Password:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String password = new String(passwordField.getPassword());
            if (PASSWORD.equals(password)) {
                cardLayout.show(cardPanel, "admin");
            } else {
                JOptionPane.showMessageDialog(frame, "Incorrect password!",
                        "Access Denied", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Helper method for input prompts
    private String promptForInput(String title, String message) {
        return JOptionPane.showInputDialog(frame, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    // Helper method for input prompts with default value
    private String promptForInput(String title, String message, String defaultValue) {
        return (String) JOptionPane.showInputDialog(frame, message, title,
                JOptionPane.PLAIN_MESSAGE, null, null, defaultValue);
    }

    // Show the GUI
    public void createAndShowGUI() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Run the app
        SwingUtilities.invokeLater(() -> {
            VendingMachine vendingMachine = new VendingMachine();
            // Add products with priority (lower number = higher priority)
            vendingMachine.addProduct(30, "Coke", 10, 1);
            vendingMachine.addProduct(25, "Pepsi", 10, 2);
            vendingMachine.addProduct(20, "Sprite", 10, 0);
            vendingMachine.addProduct(35, "Fanta", 8, 0);
            vendingMachine.addProduct(40, "Water", 15, 1);
            vendingMachine.addProduct(45, "Coffee", 5, 2);

            App gui = new App(vendingMachine);
            gui.createAndShowGUI();
        });
    }
}
