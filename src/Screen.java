public class Screen {
    private PriorityQueue PQ;
    private MyQueue Q;
    private int id;

    public Screen() {
        this.PQ = new PriorityQueue();
        this.Q = new MyQueue();
        this.id = 1;
    }

    public void addProduct(int price, String name, int quantity, int priority) {
        Product P = new Product(this.id, name, price, quantity, priority);
        id++;
        this.PQ.enQueue(P);
    }

    public void editProduct(int price, String name, int quantity, int priority) {
        while (!PQ.isEmpty()) {
            Product temp = PQ.front();
            PQ.deQueue();
            if (temp.getName().equals(name)) {
                Product edited = new Product(temp.getId(), name, price, quantity, priority);
                // edited.setPriority(quantity);
                Q.enQueue(edited);
            } else {
                Q.enQueue(temp);
            }
        }

        while (!Q.isEmpty()) {
            Product temp = Q.front();
            Q.deQueue();
            PQ.enQueue(temp);
        }
    }

    public void removeProduct(String name) {
        while (!PQ.isEmpty()) {
            Product temp = PQ.front();
            PQ.deQueue();
            if (temp.getName() != name) {
                Q.enQueue(temp);
            }
        }
        while (!Q.isEmpty()) {
            Product temp = Q.front();
            Q.deQueue();
            PQ.enQueue(temp);
        }
    }

    public Product getFrontProduct() {
        return PQ.front();
    }

}
