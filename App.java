import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout());

        // Grid panel to hold product panels
        productGridPanel = new JPanel();
        JScrollPane productScrollPane = new JScrollPane(productGridPanel);
        frame.add(productScrollPane, BorderLayout.CENTER);

        // Cart Panel
        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(new BorderLayout());
        cartList = new JList<>(cartModel);
        cartPanel.add(new JScrollPane(cartList), BorderLayout.CENTER);

        // Total Price Label
        totalPriceLabel = new JLabel("Total Price: 0 THB");
        cartPanel.add(totalPriceLabel, BorderLayout.SOUTH);

        // Checkout Button
        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> {
            vm.cashOut();
            updateCartDisplay();
            JOptionPane.showMessageDialog(frame, "Checkout successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        cartPanel.add(checkoutButton, BorderLayout.NORTH);

        frame.add(cartPanel, BorderLayout.EAST);

        // Initialize GUI with products
        updateProductDisplay();
        frame.setVisible(true);
    }

    public void updateProductDisplay() {
        productGridPanel.removeAll();
        List<Product> products = vm.getAllProducts();

        int cols = 3;
        int rows = (int) Math.ceil(products.size() / (double) cols);
        productGridPanel.setLayout(new GridLayout(rows, cols, 10, 10));

        for (Product p : products) {
            JPanel productPanel = new JPanel();
            productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
            productPanel.setBorder(BorderFactory.createTitledBorder(p.getName()));

            JLabel priceLabel = new JLabel("Price: " + p.getPrice());
            JLabel qtyLabel = new JLabel("Qty: " + p.getQuantity());

            JButton addToCartBtn = new JButton("Add");
            addToCartBtn.addActionListener(e -> {
                vm.AddToCart(p);
                updateCartDisplay();
            });

            productPanel.add(priceLabel);
            productPanel.add(qtyLabel);
            productPanel.add(addToCartBtn);
            productGridPanel.add(productPanel);
        }

        productGridPanel.revalidate();
        productGridPanel.repaint();
    }

    public void updateCartDisplay() {
        cartModel.clear();
        List<Product> cartItems = vm.getCart().getAllItems();
        for (Product product : cartItems) {
            cartModel.addElement(product.getName() + " - " + product.getQuantity() + " x " + product.getPrice() + " THB");
        }
        totalPriceLabel.setText("Total Price: " + vm.getCart().getSumPrice() + " THB");
    }

    public static void main(String[] args) {
        VendingMachine vendingMachine = new VendingMachine();
        App gui = new App(vendingMachine);

        // Simulate adding some products
        vendingMachine.getScreen().addProduct(30, "Coke", 10);
        vendingMachine.getScreen().addProduct(25, "Pepsi", 10);
        vendingMachine.getScreen().addProduct(20, "Sprite", 15);
        vendingMachine.getScreen().addProduct(40, "Fanta", 5);

        gui.createAndShowGUI();
    }
}
