import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class VendingMachine {
    private Screen Scr;
    private Cart cart;
    private History history;

    public VendingMachine() {
        this.Scr = new Screen();
        this.cart = new Cart();
        this.history = new History();
    }

    public void AddToCart(Product P) {
        if (P.getQuantity() <= 0) {
            JOptionPane.showMessageDialog(null, "This product is out of stock!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Product> cartItems = cart.getAllItems();
        for (Product cartItem : cartItems) {
            if (cartItem.getId() == P.getId()) {
                if (cartItem.getQuantity() + 1 > P.getQuantity()) {
                    JOptionPane.showMessageDialog(null, "Not enough stock available!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                cartItem.addQuantity(1);
                cart.calculatePrice();
                return;
            }
        }

        Product Added = new Product(P.getId(), P.getName(), P.getPrice(), 1, P.getPriority());
        cart.addToCart(Added);
        cart.calculatePrice();
    }

    public void DeleteFromCart(Product P) {
        cart.removeFromCart(P);
        cart.calculatePrice();
    }

    public void cashOut() {
        List<Product> cartItems = cart.getAllItems();
        for (Product cartItem : cartItems) {
            for (Product product : getAllProducts()) {
                if (product.getId() == cartItem.getId()) {
                    product.addQuantity(-cartItem.getQuantity()); // Proper stock reduction
                    product.UpdateSale(cartItem.getQuantity());
                    break;
                }
            }
        }
        cart.clearCart();
    }

    public void editProduct(int id, int price, String name, int quantity, int priority) {
        Scr.editProduct(id, price, name, quantity, priority);
    }

    public void removeProductFromScreen(String name) {
        Scr.removeProduct(name);
    }

    public void viewCart() {
        // cart.printCart(); // Assuming `printCart` exists
    }

    public Product getProductAt(int index) {
        int i = 0;
        Product current = Scr.getFrontProduct();

        while (current != null) {
            if (i == index) {
                return current;
            }
            current = current.getNext();
            i++;
        }

        return null; // index out of bounds
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        Product current = Scr.getFrontProduct();

        while (current != null) {
            products.add(current);
            current = current.getNext();
        }

        return products;
    }

    public Cart getCart() {
        return this.cart;
    }

    public Screen getScreen() {
        return this.Scr;
    }

    public void addProduct(int price, String name, int quantity, int priority) {
        Scr.addProduct(price, name, quantity, priority);
    }

    public void removeProduct(String name) {
        Scr.removeProduct(name);
    }

}
