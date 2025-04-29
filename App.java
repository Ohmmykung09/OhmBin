import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class App {
    private VendingMachine vm;
    private JFrame frame;
    private DefaultListModel<String> productModel;
    private DefaultListModel<String> cartModel;
    private JList<String> productList;
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
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Create Product List Panel
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BorderLayout());
        productList = new JList<>(productModel);
        productPanel.add(new JScrollPane(productList), BorderLayout.CENTER);
        
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = productList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    Product selectedProduct = vm.getProductAt(selectedIndex);
                    vm.AddToCart(selectedProduct);
                    updateCartDisplay();
                }
            }
        });
        productPanel.add(addToCartButton, BorderLayout.SOUTH);

        // Create Cart Panel
        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(new BorderLayout());
        cartList = new JList<>(cartModel);
        cartPanel.add(new JScrollPane(cartList), BorderLayout.CENTER);

        // Total Price Label
        totalPriceLabel = new JLabel("Total Price: 0 THB");
        cartPanel.add(totalPriceLabel, BorderLayout.SOUTH);

        // Create Checkout Button
        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                vm.cashOut();
                updateCartDisplay();
                JOptionPane.showMessageDialog(frame, "Checkout successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        cartPanel.add(checkoutButton, BorderLayout.NORTH);

        // Add panels to frame
        frame.add(productPanel, BorderLayout.WEST);
        frame.add(cartPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }

    // Update Product List
    public void updateProductDisplay() {
        productModel.clear();
        List<Product> products = vm.getProducts(); // Assuming you have a method to get all products
        for (Product product : products) {
            productModel.addElement(product.getName() + " - " + product.getPrice() + " THB");
        }
    }

    // Update Cart Display
    public void updateCartDisplay() {
        cartModel.clear();
        cartModel.addElement("Total: " + vm.getCartTotal() + " THB");
        List<Product> cartItems = vm.getCartItems(); // Assuming you have a method to get cart items
        for (Product product : cartItems) {
            cartModel.addElement(product.getName() + " - " + product.getQuantity() + " x " + product.getPrice() + " THB");
        }
        totalPriceLabel.setText("Total Price: " + vm.getCartTotal() + " THB");
    }

    public static void main(String[] args) {
        VendingMachine vendingMachine = new VendingMachine();
        App gui = new App(vendingMachine);
        gui.createAndShowGUI();
        
        // Simulate adding some products to the vending machine
        vendingMachine.AddToCart(new Product(1, "Coke", 30, 10)); // Example products
        vendingMachine.AddToCart(new Product(2, "Pepsi", 25, 10)); 
        gui.updateProductDisplay();
    }
}
