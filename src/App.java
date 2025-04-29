import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class App {
    private VendingMachine vm;
    private JFrame frame;
    private DefaultListModel<String> productModel;
    private DefaultListModel<String> cartModel;
    private JList<String> cartList;
    private JLabel totalPriceLabel;

    public App(VendingMachine vm) {
        this.vm = vm;
        this.frame = new JFrame("Vending Machine");
        this.productModel = new DefaultListModel<>();
        this.cartModel = new DefaultListModel<>();
    }

    // Initialize GUI components
    public void createAndShowGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout());

        // Create Product Panel
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BorderLayout());

        JPanel productGridPanel = new JPanel();
        productGridPanel.setLayout(new GridLayout(0, 2));

        List<Product> products = vm.getAllProducts();
        for (Product product : products) {
            JButton productButton = new JButton(product.getName() + " - " + product.getPrice() + " THB");
            productButton.addActionListener(e -> {
                vm.AddToCart(product);
                updateCartDisplay();
            });
            productGridPanel.add(productButton);
        }
        productPanel.add(new JScrollPane(productGridPanel), BorderLayout.CENTER);

        // Add/Edit Buttons
        JPanel productControlPanel = new JPanel();
        JButton addProductBtn = new JButton("Add Product");
        JButton editProductBtn = new JButton("Edit Product");

        addProductBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(frame, "Enter product name:");
            int price = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter product price:"));
            int qty = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter quantity:"));
            vm.addProduct(price, name, qty);
            updateProductDisplay();
        });

        editProductBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(frame, "Enter product name to edit:");
            int price = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter new price:"));
            int qty = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter new quantity:"));
            int priority = qty;
            vm.editProduct(price, name, qty, priority);
            updateProductDisplay();
        });

        productControlPanel.add(addProductBtn);
        productControlPanel.add(editProductBtn);
        productPanel.add(productControlPanel, BorderLayout.SOUTH);

        // Cart Panel
        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(new BorderLayout());
        cartList = new JList<>(cartModel);
        cartPanel.add(new JScrollPane(cartList), BorderLayout.CENTER);

        JPanel cartControlPanel = new JPanel();
        JButton plusBtn = new JButton("+");
        JButton minusBtn = new JButton("-");
        JButton deleteBtn = new JButton("Delete");

        plusBtn.addActionListener(e -> {
            int idx = cartList.getSelectedIndex() - 1;
            if (idx >= 0) {
                Product p = vm.getCart().getAllItems().get(idx);
                p.addQuantity(1);
                vm.getCart().calculatePrice();
                updateCartDisplay();
            }
        });

        minusBtn.addActionListener(e -> {
            int idx = cartList.getSelectedIndex() - 1;
            if (idx >= 0) {
                Product p = vm.getCart().getAllItems().get(idx);
                if (p.getQuantity() > 1) {
                    p.addQuantity(-1);
                    vm.getCart().calculatePrice();
                } else {
                    vm.getCart().removeFromCart(p);
                }
                updateCartDisplay();
            }
        });

        deleteBtn.addActionListener(e -> {
            int idx = cartList.getSelectedIndex() - 1;
            if (idx >= 0) {
                Product p = vm.getCart().getAllItems().get(idx);
                vm.getCart().removeFromCart(p);
                updateCartDisplay();
            }
        });

        cartControlPanel.add(plusBtn);
        cartControlPanel.add(minusBtn);
        cartControlPanel.add(deleteBtn);

        totalPriceLabel = new JLabel("Total Price: 0 THB");

        cartPanel.add(cartControlPanel, BorderLayout.NORTH);
        cartPanel.add(totalPriceLabel, BorderLayout.SOUTH);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> {
            vm.cashOut();
            updateCartDisplay();
            JOptionPane.showMessageDialog(frame, "Checkout successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        cartPanel.add(checkoutButton, BorderLayout.EAST);

        frame.add(productPanel, BorderLayout.CENTER);
        frame.add(cartPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }

    public void updateProductDisplay() {
        frame.getContentPane().removeAll();
        createAndShowGUI();
    }

    public void updateCartDisplay() {
        cartModel.clear();
        cartModel.addElement("Total: " + vm.getCart().getSumPrice() + " THB");
        List<Product> cartItems = vm.getCart().getAllItems();
        for (Product product : cartItems) {
            cartModel.addElement(product.getName() + " - " + product.getQuantity() + " x " + product.getPrice() + " THB");
        }
        totalPriceLabel.setText("Total Price: " + vm.getCart().getSumPrice() + " THB");
    }

    public static void main(String[] args) {
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.addProduct(30, "Coke", 10);
        vendingMachine.addProduct(25, "Pepsi", 10);
        vendingMachine.addProduct(20, "Sprite", 10);

        App gui = new App(vendingMachine);
        gui.createAndShowGUI();
    }
}