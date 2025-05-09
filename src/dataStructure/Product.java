package dataStructure;
public class Product {
    private int id;
    private String name;
    private int quantity;
    private int total_sale;
    private int price;
    private Product next;
    private int priority;
    public String imagePath;

    public Product(int id, String name, int price, int quantity, int priority, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.total_sale = 0;
        this.next = null;
        this.priority = priority;
        this.imagePath = imagePath;
    }

    public int getId() {
        return this.id;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int n) {
        this.price = n;
    }

    public String getName() {
        return this.name;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int n) {
        this.priority = n;
    }

    public int addQuantity(int n) {
        this.quantity = this.quantity + n;
        return this.quantity;
    }

    public int UpdateSale(int n) {
        this.total_sale = this.total_sale + n;
        return this.total_sale;
    }

    public int getTotalSale() {
        return this.total_sale;
    }

    public Product getNext() {
        return this.next;
    }

    public void setNext(Product P) {
        this.next = P;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String path) {
        this.imagePath = path;
    }
}
