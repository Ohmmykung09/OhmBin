import java.util.ArrayList;
import java.util.List;
public class Cart {
    private MyQueue Q;
    private int number;
    private int sumPrice;
    public Cart(){
        this.Q = new MyQueue();
        this.number = 1;
        this.sumPrice = 0;
    }

    public void addToCart(Product P){
        Q.enQueue(P);
    }

    public void removeFromCart(Product P){
        if(Q.isEmpty()){
            return;
        }
        else{
            Product first = Q.front();
            do {
                Product current = Q.front();
                Q.deQueue();
                if (current.getId() != P.getId()) {
                    Q.enQueue(current);
                }
            } while (Q.front() != first);
        }
    }

    public int getSumPrice(){
        return this.sumPrice;
    }

    public void setSumPrice(int Price){
        this.sumPrice = Price; 
    }

    public void calculatePrice(){
        Product temp = Q.front();
        Q.deQueue();
        Q.enQueue(temp);
        while(Q.front() != temp){
            Product temp2 = Q.front();
            Q.deQueue();
            this.setSumPrice(getSumPrice()+temp2.getQuantity()*temp2.getPrice());
            Q.enQueue(temp2);
        }
    }

    public int getNumber(){
        return this.number;
    }

    public void numberIncrement(){
        this.number++;
    }

    public void clearCart() {
        while (!Q.isEmpty()) {
            Q.deQueue();
        }
        sumPrice = 0;
    }

    public List<Product> getAllItems() {
        List<Product> items = new ArrayList<>();
    
        if (Q.isEmpty()) return items;
    
        Product temp = Q.front();
        Q.deQueue();
        Q.enQueue(temp);
        items.add(temp);
    
        while (Q.front() != temp) {
            Product curr = Q.front();
            Q.deQueue();
            Q.enQueue(curr);
            items.add(curr);
        }
    
        return items;
    }
}
