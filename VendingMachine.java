import java.util.List;

public class VendingMachine {
    private Screen Scr;
    private Cart cart;
    private History history; 
    public VendingMachine(){
        this.Scr = new Screen();
        this.cart = new Cart();
        this.history = new History();
    }

    public void AddToCart(Product P){
        Product Added = new Product(P.getId(), P.getName(), P.getPrice(), 1);
        Added.setPriority(P.getPriority());
        cart.addToCart(Added);
        cart.calculatePrice();
    }

    public void DeleteFromCart(Product P){
        cart.removeFromCart(P);
        cart.calculatePrice();
    }

    public void cashOut(){
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
        //cart.printCart(); // Assuming `printCart` exists
    }
}
