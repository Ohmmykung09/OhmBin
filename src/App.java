import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class App {
    private VendingMachine vm;
    private JFrame frame;
    private JPanel productGridPanel;
    private DefaultListModel<String> cartModel;
    private JList<String> cartList;
    private JLabel totalPriceLabel;

    public App(VendingMachine vm) {
        this.vm = vm;
        this.frame = new JFrame("Vending Machine");
        this.cartModel = new DefaultListModel<>();
    }

    public void createAndShowGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Product Grid Panel
        productGridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        JScrollPane productScrollPane = new JScrollPane(productGridPanel);
        frame.add(productScrollPane, BorderLayout.CENTER);

        // Top Panel for Add/Edit buttons
        JPanel topPanel = new JPanel();
        JButton addProductButton = new JButton("Add Product");
        JButton editProductButton = new JButton("Edit Product");

        addProductButton.addActionListener(e -> openProductDialog(false));
        editProductButton.addActionListener(e -> openProductDialog(true));

        topPanel.add(addProductButton);
        topPanel.add(editProductButton);
        frame.add(topPanel, BorderLayout.NORTH);

        // Cart Panel
        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(new BorderLayout());
        cartList = new JList<>(cartModel);
        cartPanel.add(new JScrollPane(cartList), BorderLayout.CENTER);

        JPanel cartControlPanel = new JPanel();
        JButton increaseButton = new JButton("+");
        JButton decreaseButton = new JButton("-");
        JButton deleteButton = new JButton("Delete");

        increaseButton.addActionListener(e -> adjustQuantity(1));
        decreaseButton.addActionListener(e -> adjustQuantity(-1));
        deleteButton.addActionListener(e -> deleteSelectedCartItem());

        cartControlPanel.add(increaseButton);
        cartControlPanel.add(decreaseButton);
        cartControlPanel.add(deleteButton);

        cartPanel.add(cartControlPanel, BorderLayout.NORTH);

        totalPriceLabel = new JLabel("Total Price: 0 THB");
        cartPanel.add(totalPriceLabel, BorderLayout.SOUTH);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> {
            vm.cashOut();
            updateProductDisplay();
            updateCartDisplay();
            JOptionPane.showMessageDialog(frame, "Checkout successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        cartPanel.add(checkoutButton, BorderLayout.EAST);

        frame.add(cartPanel, BorderLayout.EAST);
        updateProductDisplay();
        frame.setVisible(true);
    }

    private void updateProductDisplay() {
        productGridPanel.removeAll();
        List<Product> products = vm.getAllProducts();

        for (Product product : products) {
            JButton productButton = new JButton("<html><center>" + product.getName() + "<br>" + product.getPrice() + " THB<br>Stock: " + product.getQuantity() + "</center></html>");

            // Set color based on priority
            if (product.getPriority() == 2) {
                productButton.setBackground(new Color(144, 238, 144));

            } else if (product.getPriority() == 1) {
                productButton.setBackground(new Color(240, 128, 128));
            } else {
                productButton.setBackground(Color.LIGHT_GRAY);
            }

            productButton.addActionListener(e -> {
                vm.AddToCart(product);
                updateCartDisplay();
                updateProductDisplay();
            });

            productGridPanel.add(productButton);
        }

        productGridPanel.revalidate();
        productGridPanel.repaint();
    }

    private void updateCartDisplay() {
        cartModel.clear();
        List<Product> cartItems = vm.getCart().getAllItems();
        for (Product product : cartItems) {
            cartModel.addElement(product.getName() + " - " + product.getQuantity() + " x " + product.getPrice() + " THB");
        }
        totalPriceLabel.setText("Total Price: " + vm.getCart().getSumPrice() + " THB");
    }

    private void openProductDialog(boolean isEdit) {
        JTextField nameField = new JTextField(10);
        JTextField priceField = new JTextField(5);
        JTextField quantityField = new JTextField(5);
        JComboBox<String> priorityBox = new JComboBox<>(new String[] {"Common", "Popular", "New Arrival"});

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Priority:"));
        panel.add(priorityBox);

        int result = JOptionPane.showConfirmDialog(frame, panel, isEdit ? "Edit Product" : "Add Product", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            int price = Integer.parseInt(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            int priority = priorityBox.getSelectedIndex();

            if (isEdit) {
                vm.editProduct(price, name, quantity, priority);
            } else {
                vm.addProduct(price,name,quantity,priority);
            }
            updateProductDisplay();
        }
    }

    private void adjustQuantity(int change) {
        int selectedIndex = cartList.getSelectedIndex();
        if (selectedIndex < 0) return;

        List<Product> cartItems = vm.getCart().getAllItems();
        if (selectedIndex >= cartItems.size()) return;

        Product selectedProduct = cartItems.get(selectedIndex);
        if (change > 0) {
            selectedProduct.addQuantity(change);
        } else {
            selectedProduct.addQuantity(change);
            if (selectedProduct.getQuantity() <= 0) {
                vm.getCart().removeFromCart(selectedProduct);
            }
        }
        vm.getCart().calculatePrice();
        updateCartDisplay();
    }

    private void deleteSelectedCartItem() {
        int index = cartList.getSelectedIndex() - 1;
        if (index >= 0) {
            Product item = vm.getCart().getAllItems().get(index);
            vm.DeleteFromCart(item);
            updateCartDisplay();
            updateProductDisplay();
        }
    }

    public static void main(String[] args) {
        VendingMachine vendingMachine = new VendingMachine();
        App gui = new App(vendingMachine);
        gui.createAndShowGUI();
    }
}
