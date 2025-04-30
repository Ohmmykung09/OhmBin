import java.util.List;
import java.util.ArrayList;

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
            return; // Cannot add out-of-stock product
        }

        // Decrease stock
        P.addQuantity(-1);

        // If stock is 0, mark as unavailable
        if (P.getQuantity() == 0) {
            P.setPriority(-1); // Grey
        }

        // Add to cart (clone to avoid affecting original product in list)
        Product Added = new Product(P.getId(), P.getName(), P.getPrice(), 1, P.getPriority());
        Added.setPriority(P.getPriority());
        cart.addToCart(Added);

        cart.calculatePrice();
    }

    public void DeleteFromCart(Product P) {
        cart.removeFromCart(P);
        cart.calculatePrice();
    }

    public void cashOut() {
        List<Product> cartItems = cart.getAllItems();
        int total = cart.getSumPrice();
        history.addLog(cart.getNumber(), cartItems, total);
        cart.clearCart();
        cart.numberIncrement();

    }

    public void editProduct(int price, String name, int quantity, int priority) {
        Scr.editProduct(price, name, quantity, priority);
    }

    public void removeProductFromScreen(String name) {
        Scr.removeProduct(name);
    }

    public void viewCart() {
        // cart.printCart(); // Assuming `printCart` exists
    }

    public Product getProductAt(int index) {
        int i = 0;
        Product current = Scr.getFrontProduct(); // You need to expose this in Screen

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
