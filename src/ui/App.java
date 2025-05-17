package ui;
import javax.swing.*;
import javax.swing.border.*;

import dataStructure.Product;
import model.VendingMachine;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class App {
    private VendingMachine vm;
    private MusicPlayer Bgm;
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JPanel cartItemsPanel; 
    private DefaultListModel<String> cartModel;
    private DefaultListModel<String> productsModel;
    private DefaultListModel<String> historyModel = new DefaultListModel<>();
    private JLabel totalPriceLabel;
    private JPanel productGridPanel;
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color ACCENT_COLOR = new Color(52, 152, 219);
    private final Color BG_COLOR = new Color(236, 240, 241);
    private final Color TEXT_COLOR = new Color(44, 62, 80);
    private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private final Font REGULAR_FONT = new Font("Arial", Font.PLAIN, 14);
    private final String PASSWORD = "admin123";
    private final String ASSET_PATH = "OhmBin/asset/";
    private final String BUTTON_SOUND = "assets/Music/ButtonSound.wav";
    private final String LEVER_SOUND = "assets/Music/LeverSound.wav";
    private Map<String, ImageIcon> productImages = new HashMap<>();

    //Constructoro สร้าง Vending Machine และ Setup Frame ของ App และสร้าง music Player
    public App(VendingMachine vm) {
        this.vm = vm;
        this.frame = new JFrame("Modern Vending Machine");
        this.cartModel = new DefaultListModel<>();
        this.productsModel = new DefaultListModel<>();
        loadProductImages();
        setupFrame();
        setupMusic();
        frame.setVisible(true);
    }

    private void setupMusic() { //เปิดเพลง Bgm
        Bgm = new MusicPlayer();
        Bgm.playMusic("assets/Music/FinalBgm.wav");
    }

    private void loadProductImages() { //Load รูปสินค้า
        List<Product> products = vm.getAllProducts(); //สร้าง List ของ product จากรายการสินค้า
        for (Product product : products) { //Loop Product ทุกตัว
            try {
                String imagePath = product.getImagePath(); //อ่าน image path
                System.out.println(
                        "Attempting to load image from: " + new File(imagePath).getAbsolutePath());
                File imageFile = new File(imagePath);

                if (imageFile.exists()) { //กรณีเจอให้สร้างรูปขนาดเท่ากับ 80*80
                    BufferedImage img = ImageIO.read(imageFile);
                    Image scaledImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                    ImageIcon icon = new ImageIcon(scaledImg);
                    productImages.put(product.getName(), icon);
                } else { //กรณีไม่เจอรูปให้สร้างเป็น Placeholder เป็นอักษรย่อ
                    BufferedImage placeholder =
                            new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = placeholder.createGraphics();
                    g2d.setColor(ACCENT_COLOR);
                    g2d.fillRect(0, 0, 80, 80);
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(product.getName().substring(0, 1), 35, 45);
                    g2d.dispose();
                    productImages.put(product.getName(), new ImageIcon(placeholder));
                }
            } catch (IOException e) { //กรณีอื่น ๆ
                System.err.println(
                        "Error loading image for " + product.getName() + ": " + e.getMessage());
                BufferedImage placeholder = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = placeholder.createGraphics();
                g2d.setColor(ACCENT_COLOR);
                g2d.fillRect(0, 0, 80, 80);
                g2d.dispose();
                productImages.put(product.getName(), new ImageIcon(placeholder));
            }
        }
    }

    private void setupFrame() { //สร้าง frame ของ App 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.getContentPane().setBackground(BG_COLOR);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BG_COLOR);
        //สร้าง cardPanel รวมทุกอย่าง
        JPanel customerPanel = createCustomerPanel(); //มี Customer Panel 

        JPanel adminPanel = createAdminPanel(); //มี Admin Panel

        cardPanel.add(customerPanel, "customer");
        cardPanel.add(adminPanel, "admin");
        //Add Cardpanel ลงใน frame
        frame.add(cardPanel);
    }

    private JPanel createCustomerPanel() { //Customer Panel
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Ohmboo the Vending Machine");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        //สร้างปุ่ม admin และ history
        JButton adminButton = createStyledButton("", ACCENT_COLOR);
        JButton historyButton = createStyledButton("", ACCENT_COLOR); // Updated to use icon
        try { //Load รูปปุ่ม history
            BufferedImage historyIcon = ImageIO.read(new File("assets/IconPic/Historyicon.png"));
            Image scaledHistoryIcon = historyIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            historyButton.setIcon(new ImageIcon(scaledHistoryIcon));
        } catch (IOException e) {
            System.err.println("Error loading history icon: " + e.getMessage());
        }
        try {  //Load รูปปุ่ม admin
            BufferedImage adminIcon = ImageIO.read(new File("assets/IconPic/AdminIcon.png"));
            Image scaledIcon = adminIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            adminButton.setIcon(new ImageIcon(scaledIcon));
        } catch (IOException e) {
            System.err.println("Error loading admin icon: " + e.getMessage());
        }
        adminButton.addActionListener(e -> promptAdminPassword()); //Validate admin หลังจากกดปุ่ม history

        historyButton.addActionListener(e -> showHistoryDialog()); //แสดงประวัติการขาย หลังจากกดปุ่ม history

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(PRIMARY_COLOR);
        buttonPanel.add(historyButton);
        buttonPanel.add(adminButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        //รายการสินค้า
        JPanel productsContainer = new JPanel(new BorderLayout());
        productsContainer.setBackground(BG_COLOR);
        productsContainer.setBorder(createRoundedBorder("Products", 10));

        productGridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        productGridPanel.setBackground(BG_COLOR);
        productGridPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        updateProductDisplay(); //แสดงรายการสินค้า

        JScrollPane productScroll = new JScrollPane(productGridPanel);
        productScroll.setBorder(null);
        productScroll.getVerticalScrollBar().setUnitIncrement(16);
        productsContainer.add(productScroll, BorderLayout.CENTER);

        //ตะกร้า
        JPanel cartContainer = new JPanel(new BorderLayout());
        cartContainer.setBackground(BG_COLOR);
        cartContainer.setBorder(createRoundedBorder("Your Cart", 10));
        cartContainer.setPreferredSize(new Dimension(300, 0));

        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBackground(Color.WHITE);
        cartItemsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        cartItemsPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        JScrollPane cartScroll = new JScrollPane(cartItemsPanel);
        cartScroll.setBorder(null);

        totalPriceLabel = new JLabel("Total: 0 THB");
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPriceLabel.setForeground(TEXT_COLOR);
        totalPriceLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton checkoutBtn = createStyledButton("", PRIMARY_COLOR);
        checkoutBtn.setFont(new Font("Arial", Font.BOLD, 16));
        try { //Load รูปปุ่ม Cashout
            BufferedImage cartIcon = ImageIO.read(new File("assets/IconPic/CartIcon.png"));
            Image scaledIcon = cartIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            checkoutBtn.setIcon(new ImageIcon(scaledIcon));
        } catch (IOException e) {
            System.err.println("Error loading cart icon: " + e.getMessage());
        }
        checkoutBtn.addActionListener(e -> { //กดปุ่ม Checkout
            if (vm.getCart().getAllItems().isEmpty()) { //กรณีตะกร้าว่าง
                JOptionPane.showMessageDialog(frame, "Your cart is empty!", "Checkout",
                        JOptionPane.WARNING_MESSAGE); //pop up แจ้งเตือน
            } else { //กรณีตะกร้าไม่ว่าง
                JPanel paymentPanel = new JPanel(new GridLayout(3, 1, 10, 10));
                paymentPanel.setBackground(BG_COLOR);
                paymentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JLabel label = new JLabel("Select Payment Method:"); //pop up เลือกวิธีชำระเงิน
                label.setFont(REGULAR_FONT);
                label.setForeground(TEXT_COLOR);

                JRadioButton cashOption = new JRadioButton("Cash");
                JRadioButton PromptPayOption = new JRadioButton("PromptPay");
                ButtonGroup paymentGroup = new ButtonGroup();
                paymentGroup.add(cashOption);
                paymentGroup.add(PromptPayOption);

                paymentPanel.add(label);
                paymentPanel.add(cashOption);
                paymentPanel.add(PromptPayOption);

                int result = JOptionPane.showConfirmDialog(frame, paymentPanel, "Payment Method",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    if (!cashOption.isSelected() && !PromptPayOption.isSelected()) {
                        JOptionPane.showMessageDialog(frame, "Please select a payment method.",
                                "Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (PromptPayOption.isSelected()) { //กรณีเลือก prompt pay
                        String[] qrs = {"0965293625", "0630519730", "0930626610"};
                        Random random = new Random();
                        int randomIdx = random.nextInt(qrs.length);
                        String qr = qrs[randomIdx];
                        String qrUrl = String.format("https://promptpay.io/%s/%d", qr,
                                vm.getCart().getSumPrice());
                        handleQRPayment(qrUrl); //แสดง Qrcode
                    } else { //กรณีชำระเงินสด
                        handleCashPayment(); //แสดงหน้าจำลองชำระเงินสด
                    }
                }
            }
        });

        JPanel checkoutPanel = new JPanel(new BorderLayout());
        checkoutPanel.setBackground(BG_COLOR);
        checkoutPanel.add(totalPriceLabel, BorderLayout.WEST);
        checkoutPanel.add(checkoutBtn, BorderLayout.EAST);

        cartContainer.add(cartScroll, BorderLayout.CENTER);
        cartContainer.add(checkoutPanel, BorderLayout.SOUTH);

        updateCartDisplay();

        //Panel ใหญ่รวมรายการสินค้า และตะกร้า
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(BG_COLOR);
        contentPanel.add(productsContainer, BorderLayout.CENTER);
        contentPanel.add(cartContainer, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private void showProductDialog(Product product, DefaultListModel<String> productsModel) { //แสดง popup edit สินค้า
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);

        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imagePanel.setBackground(BG_COLOR);
        JLabel imageLabel = new JLabel();

        if (product != null && productImages.containsKey(product.getName())) {
            imageLabel.setIcon(productImages.get(product.getName()));
        } else {
            imageLabel
                    .setIcon(new ImageIcon(new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB)));
        }

        imagePanel.add(imageLabel);
        panel.add(imagePanel, BorderLayout.NORTH);

        // Input fields ต่าง ๆ
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBackground(BG_COLOR);

        JTextField nameField = new JTextField(product != null ? product.getName() : "");
        JTextField priceField =
                new JTextField(product != null ? String.valueOf(product.getPrice()) : "");
        JTextField quantityField =
                new JTextField(product != null ? String.valueOf(product.getQuantity()) : "");
        JTextField imagePathField = new JTextField(
                product != null ? ASSET_PATH + product.getName().toLowerCase() + ".png" : "");

        // Priority แสดงเป็น drop down
        String[] priorities = {"Common Item", "Hot Seller", "New Item"};
        JComboBox<String> priorityDropdown = new JComboBox<>(priorities);
        if (product != null) {
            priorityDropdown.setSelectedIndex(product.getPriority());
        }
        //Browse file รูปจากเครื่อง
        JButton browseButton = createStyledButton("Browse", ACCENT_COLOR);
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                imagePathField.setText(selectedFile.getAbsolutePath());
                //image preview
                try {
                    BufferedImage img = ImageIO.read(selectedFile);
                    Image scaledImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImg));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error loading image: " + ex.getMessage(),
                            "Image Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        formPanel.add(new JLabel("Product Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Product Price (THB):"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Product Quantity:"));
        formPanel.add(quantityField);
        formPanel.add(new JLabel("Product Priority:"));
        formPanel.add(priorityDropdown);
        formPanel.add(new JLabel("Upload Image:"));
        formPanel.add(browseButton);

        panel.add(formPanel, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(frame, panel,
                product == null ? "Add New Product" : "Edit Product", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = product != null ? product.getId() : vm.getAllProducts().size() + 1; 
                String name = nameField.getText().trim();
                int price = Integer.parseInt(priceField.getText().trim());
                int quantity = Integer.parseInt(quantityField.getText().trim());
                int priority = priorityDropdown.getSelectedIndex();

                // Validate ราคา and ปริมาณ
                if (price < 0 || quantity < 0) {
                    JOptionPane.showMessageDialog(frame, "Price and quantity must be greater than or equal to 0.",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validate image path 
                String imagePath = imagePathField.getText().trim();
                boolean imageExists = new File(imagePath).exists();

                if (product == null) { //กรณีไม่มี product
                    // Add new product
                    vm.addProduct(price, name, quantity, priority, imagePath);
                    if (imageExists) {
                        try {
                            BufferedImage img = ImageIO.read(new File(imagePath));
                            Image scaledImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                            productImages.put(name, new ImageIcon(scaledImg));
                        } catch (IOException ex) {
                            System.err.println("Error loading image for " + name);
                        }
                    }
                } else { //กรณี edit product
                    // Edit existing product
                    if (!product.getName().equals(name)) {
                        productImages.remove(product.getName());
                    }

                    if (imageExists) {
                        try {
                            BufferedImage img = ImageIO.read(new File(imagePath));
                            Image scaledImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                            productImages.put(name, new ImageIcon(scaledImg));
                        } catch (IOException ex) {
                            System.err.println("Error loading image for " + name);
                        }
                    }

                    vm.editProduct(id, price, name, quantity, priority, imagePath);
                }

                // Update รายการสินค้าใหม่
                updateProductDisplay();
                updateProductsList(productsModel);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Please enter valid numbers for price, quantity, and priority.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createAdminPanel() { //สร้าง Admin panel
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(192, 57, 43)); // Different color for admin
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Ohmboo Control Panel");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        //ปุ่มกลับไปยัง customer
        JButton backButton = createStyledButton("", new Color(231, 76, 60));
        try {
            BufferedImage backIcon = ImageIO.read(new File("assets/IconPic/BackIcon.png"));
            Image scaledIcon = backIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            backButton.setIcon(new ImageIcon(scaledIcon));
        } catch (IOException e) {
            System.err.println("Error loading cart icon: " + e.getMessage());
        }
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "customer"));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        JPanel adminContent = new JPanel(new BorderLayout(15, 15));
        adminContent.setBackground(BG_COLOR);

        // Products List ดูข้อมูลสินค้า
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

        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(BG_COLOR);
        actionPanel.setBorder(createRoundedBorder("Actions", 10));

        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonsPanel.setBackground(BG_COLOR);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton addButton = createStyledButton("Add New Product", new Color(39, 174, 96));
        JButton editButton = createStyledButton("Edit Selected Product", new Color(41, 128, 185));
        JButton removeButton =
                createStyledButton("Remove Selected Product", new Color(192, 57, 43));

        addButton.addActionListener(e -> { //เด้ง pop up แก้ไข/เพิ่ม ข้อมูลสินค้า
            showProductDialog(null, productsModel);
        });

        editButton.addActionListener(e -> { //เด้ง pop up แก้ไข/เพิ่ม ข้อมูลสินค้า แต่อิงมาจาก product เดิม
            int selectedIndex = productsList.getSelectedIndex();
            if (selectedIndex >= 0) {
                Product p = vm.getAllProducts().get(selectedIndex);
                showProductDialog(p, productsModel);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a product to edit.",
                        "Selection Required", JOptionPane.WARNING_MESSAGE);
            }
        });

        removeButton.addActionListener(e -> { //ลบจาก list ตาม index
            int selectedIndex = productsList.getSelectedIndex();
            if (selectedIndex >= 0) {
                Product p = vm.getAllProducts().get(selectedIndex);
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to remove " + p.getName() + "?", "Confirm Removal",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    vm.removeProduct(p.getName());
                    productImages.remove(p.getName());
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

        adminContent.add(productsListPanel, BorderLayout.CENTER);
        adminContent.add(actionPanel, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(adminContent, BorderLayout.CENTER);

        return panel;
    }

    private void showHistoryDialog() { //แสดงประวัติการขายสินค้า
        JList<String> historyList = new JList<>(historyModel);
        historyList.setFont(REGULAR_FONT);
        historyList.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(historyList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JDialog historyDialog = new JDialog(frame, "Purchase History", true);
        historyDialog.getContentPane().add(scrollPane);
        historyDialog.setSize(400, 300);
        historyDialog.setLocationRelativeTo(frame);
        historyDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        historyDialog.setVisible(true);
    }

    // ปุ่มมี Animation
    private void animateButton(JButton button) {
        Color originalColor = button.getBackground();
        button.setBackground(button.getBackground().brighter());

        javax.swing.Timer timer = new javax.swing.Timer(150, e -> {
            button.setBackground(originalColor);
        });
        timer.setRepeats(false);
        timer.start();
    }
    // Animation หลังจาก Add สินค้าลง Cart
    private void animateCartUpdate(JButton button) {
        animateButton(button);

        Font originalFont = totalPriceLabel.getFont();
        Color originalColor = totalPriceLabel.getForeground();

        totalPriceLabel
                .setFont(new Font(originalFont.getName(), Font.BOLD, originalFont.getSize() + 2));
        totalPriceLabel.setForeground(new Color(231, 76, 60));

        javax.swing.Timer timer = new javax.swing.Timer(300, e -> {
            totalPriceLabel.setFont(originalFont);
            totalPriceLabel.setForeground(originalColor);
        });
        timer.setRepeats(false);
        timer.start();
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(REGULAR_FONT);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setOpaque(true);
        button.setBorderPainted(false);

        button.addMouseListener(new MouseAdapter() { //hover effect
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private Border createRoundedBorder(String title, int radius) {
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true), title);
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 16));
        titledBorder.setTitleColor(TEXT_COLOR);
        return titledBorder;
    }

    private JButton createProductButton(Product product) { //สร้างปุ่มสินค้า (ในรายการสินค้า)
        JButton button = new JButton();
        button.setLayout(new BorderLayout(5, 5));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true));
        button.setFocusPainted(false);

        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setBackground(ACCENT_COLOR);
        iconPanel.setPreferredSize(new Dimension(0, 100));

        // สร้างรูป
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        if (productImages.containsKey(product.getName())) {
            imageLabel.setIcon(productImages.get(product.getName()));
        }

        iconPanel.add(imageLabel, BorderLayout.CENTER);

        // แสดงข้อมูล
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

        // แสดงข้อมูลตาม priority 
        JLabel priorityLabel = new JLabel(getPriorityLevel(product.getPriority()));
        priorityLabel.setFont(new Font("Arial", Font.BOLD, 12));
        priorityLabel.setForeground(getBgColor(product.getPriority()));

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(stockLabel);
        infoPanel.add(priorityLabel);

        button.add(iconPanel, BorderLayout.NORTH);
        button.add(infoPanel, BorderLayout.CENTER);

        if (product.getQuantity() <= 0) { //การแสดงผลกรณีสินค้าหมด
            button.setBackground(new Color(220, 220, 220));
            button.setEnabled(false);
            nameLabel.setForeground(new Color(128, 128, 128));
            priceLabel.setForeground(new Color(128, 128, 128));
            stockLabel.setForeground(new Color(128, 128, 128));
            priorityLabel.setForeground(new Color(128, 128, 128));

            JLabel outOfStockLabel = new JLabel("OUT OF STOCK");
            outOfStockLabel.setHorizontalAlignment(JLabel.CENTER);
            outOfStockLabel.setFont(new Font("Arial", Font.BOLD, 14));
            outOfStockLabel.setForeground(Color.WHITE);
            outOfStockLabel.setBackground(new Color(231, 76, 60, 180));
            outOfStockLabel.setOpaque(true);
            iconPanel.add(outOfStockLabel, BorderLayout.SOUTH);
        } else {
            // hover effect
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(245, 245, 245));
                    iconPanel.setBackground(ACCENT_COLOR.brighter());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(Color.WHITE);
                    iconPanel.setBackground(ACCENT_COLOR);
                }
            });
        }

        button.addActionListener(e -> {
            if (product.getQuantity() <= 0) { //Validate ห้ามซื้อสินค้าหมด
                JOptionPane.showMessageDialog(frame, "This product is out of stock!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                // เล่นเสียง ขณะ Add สินค้าเข้าตะกร้า
                Bgm.playSoundEffect(BUTTON_SOUND);

                // Animate button 
                iconPanel.setBackground(ACCENT_COLOR.darker());
                javax.swing.Timer timer = new javax.swing.Timer(150, event -> { 
                    iconPanel.setBackground(ACCENT_COLOR);
                });
                timer.setRepeats(false);
                timer.start();

                vm.AddToCart(product);
                updateCartDisplay();
            }
        });

        return button;
    }

    public String getPriorityLevel(int priority) { //คำแสดงผลตาม priority
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

    // Update product button
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

    // Update product ของ admin 
    private void updateProductsList(DefaultListModel<String> model) {
        model.clear();
        List<Product> products = vm.getAllProducts();
        for (Product product : products) {
            model.addElement(product.getName() + " - " + product.getPrice() + " THB - Stock: "
                    + product.getQuantity() + " - Priority: " + product.getPriority());
        }
    }

    public Color getBgColor(int priority) { // Background color ตาม priority
        switch (priority) {
            case 0:
                return new Color(52, 152, 219); // Common Item
            case 1:
                return new Color(241, 72, 15); // Hot Seller
            case 2:
                return new Color(46, 204, 113); // New Item
            default:
                return BG_COLOR;
        }

    }

    public void updateCartDisplay() { // Update ตะกร้า
        cartModel.clear();
        cartItemsPanel.removeAll();
        List<Product> cartItems = vm.getCart().getAllItems();
        for (int i = 0; i < cartItems.size(); i++) {
            Product product = cartItems.get(i);
            JPanel itemPanel = new JPanel(new BorderLayout(5, 0));
            itemPanel.setBackground(Color.WHITE);
            itemPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            itemPanel.setAlignmentY(Component.TOP_ALIGNMENT);

            JLabel infoLabel = new JLabel(product.getName() + " - " + product.getQuantity() + " x "
                    + product.getPrice() + " THB");
            infoLabel.setFont(REGULAR_FONT);
            infoLabel.setForeground(TEXT_COLOR);
            infoLabel.setVerticalAlignment(SwingConstants.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

            JButton plusBtn = createStyledButton("+", ACCENT_COLOR);
            JButton minusBtn = createStyledButton("-", ACCENT_COLOR);
            JButton deleteBtn = createStyledButton("x", ACCENT_COLOR);
            plusBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
            minusBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
            deleteBtn.setAlignmentY(Component.CENTER_ALIGNMENT);

            int idx = i; 
            plusBtn.addActionListener(e -> {
                Product p = vm.getCart().getAllItems().get(idx);
                Product originalProduct = vm.getAllProducts().stream()
                        .filter(prod -> prod.getId() == p.getId()).findFirst().orElse(null);
                if (originalProduct != null && p.getQuantity() < originalProduct.getQuantity()) {
                    p.addQuantity(1);
                    vm.getCart().calculatePrice();
                    updateCartDisplay();
                    animateCartUpdate(plusBtn);
                } else {
                    JOptionPane.showMessageDialog(frame, "Not enough stock available!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
            minusBtn.addActionListener(e -> {
                Product p = vm.getCart().getAllItems().get(idx);
                if (p.getQuantity() > 1) {
                    p.addQuantity(-1);
                    vm.getCart().calculatePrice();
                } else {
                    vm.getCart().removeFromCart(p);
                }
                updateCartDisplay();
                animateCartUpdate(minusBtn);
            });
            deleteBtn.addActionListener(e -> {
                Product p = vm.getCart().getAllItems().get(idx);
                vm.getCart().removeFromCart(p);
                updateCartDisplay();
                animateCartUpdate(deleteBtn);
            });

            buttonPanel.add(plusBtn);
            buttonPanel.add(Box.createHorizontalStrut(5));
            buttonPanel.add(minusBtn);
            buttonPanel.add(Box.createHorizontalStrut(5));
            buttonPanel.add(deleteBtn);

            itemPanel.add(infoLabel, BorderLayout.CENTER);
            itemPanel.add(buttonPanel, BorderLayout.EAST);

            cartItemsPanel.add(itemPanel);
        }
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
        totalPriceLabel.setText("Total: " + vm.getCart().getSumPrice() + " THB");
    }

    private void promptAdminPassword() { //เด้ง pop up Validate รหัสผ่าน admin
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(REGULAR_FONT);

        int result = JOptionPane.showConfirmDialog(frame, passwordField, "Enter Secret Code:",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) { //กรณีใส่รหัสถูก
            String password = new String(passwordField.getPassword());
            if (PASSWORD.equals(password)) {
                Bgm.playSoundEffect(LEVER_SOUND); // เล่นเสียง effect
                cardLayout.show(cardPanel, "admin");
            } else { //กรณีใส่ไม่ถูกแจ้ง error
                JOptionPane.showMessageDialog(frame, "Incorrect password!", "Access Denied",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addHistoryEntry() { //เพิ่มประวัติหลังการขาย
        StringBuilder historyEntry = new StringBuilder("Purchase on " + new Date() + ": ");
        List<Product> cartItems = vm.getCart().getAllItems();
        for (Product product : cartItems) {
            historyEntry.append(product.getName()).append(" (x").append(product.getQuantity())
                    .append("), ");
        }
        historyEntry.append("Total: ").append(vm.getCart().getSumPrice()).append(" THB");
        historyModel.addElement(historyEntry.toString());
    }

    public void createAndShowGUI() { //Method แสดง GUI
        frame.setVisible(true);
    }

    private void showDialog(String title, JPanel contentPanel, int width, int height) { //Method แสดง Frame
        JDialog dialog = new JDialog(frame, title, true);
        dialog.getContentPane().add(contentPanel);
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(frame);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    private JPanel createQRPanel(String qrUrl) throws IOException { //สร้าง pop up แสดง QR 
        JPanel qrPanel = new JPanel(new BorderLayout(15, 15));
        qrPanel.setBackground(BG_COLOR);
        qrPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel qrLabel = new JLabel();
        qrLabel.setHorizontalAlignment(JLabel.CENTER);

        BufferedImage qrImage = ImageIO.read(URI.create(qrUrl).toURL());
        Image scaledQrImage = qrImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        qrLabel.setIcon(new ImageIcon(scaledQrImage));
        qrLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        qrPanel.add(qrLabel, BorderLayout.CENTER);
        return qrPanel;
    }

    private void handleQRPayment(String qrUrl) { //หลังจากชำระ QR เสร็จ
        try {
            JPanel qrPanel = createQRPanel(qrUrl);

            JButton nextButton = createStyledButton("Next", PRIMARY_COLOR);
            nextButton.addActionListener(event -> {
                JOptionPane.showMessageDialog(frame, "Purchase successful! Thank you.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                addHistoryEntry(); //เพิ่มประวัติลงใน history
                vm.cashOut(); //Cashputตะกร้า
                updateCartDisplay(); //Update ตะกร้าที่ว่างแล้ว
                updateProductDisplay(); //Update จำนวนสินค้า Stock ใหม่
                updateProductsList(productsModel);
                SwingUtilities.getWindowAncestor(nextButton).dispose();
            });

            qrPanel.add(nextButton, BorderLayout.SOUTH);
            showDialog("QR Code Payment", qrPanel, 350, 400);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Failed to load QR code image: " + ex.getMessage(),
                    "Image Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCashPayment() { //หลังชำระเงินสดเสร็จ
        int confirm = JOptionPane.showConfirmDialog(frame,
                "You selected Cash. Proceed to finish?", "Confirm Payment",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(frame, "Purchase successful! Thank you.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            addHistoryEntry(); //เพิ่มประวัติลงใน history
            vm.cashOut(); //Cashputตะกร้า
            updateCartDisplay(); //Update ตะกร้าที่ว่างแล้ว
            updateProductDisplay(); //Update จำนวนสินค้า Stock ใหม่
            updateProductsList(productsModel);
        }
    }

    public static void main(String[] args) {
        try { //Setup UI
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Run App
        SwingUtilities.invokeLater(() -> {
            VendingMachine vendingMachine = new VendingMachine();
            vendingMachine.addProduct(4, "MuekGroob", 6, 1, "assets/productPic/Meukgrub.png");
            vendingMachine.addProduct(10, "Provita", 4, 0, "assets/productPic/Provita.png");
            vendingMachine.addProduct(2, "Lactasoy", 4, 1, "assets/productPic/Lactasoy.png");
            //vendingMachine.addProduct(35, "Fanta", 8, 0, "assets/productPic/Fanta.png");
            vendingMachine.addProduct(15, "Sunbite", 1, 0, "assets/productPic/Sunbite.png");
            //vendingMachine.addProduct(45, "Coffee", 5, 2, "assets/productPic/Coffee.png");
            App gui = new App(vendingMachine);
            gui.createAndShowGUI();
        });
    }
}
