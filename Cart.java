public class Cart {
    private myQueue Q;
    
    public Cart(){
        this.Q = new myQueue();
    }

    public void addToCart(Product P){
        Q.enQueue(P);
    }

    public void removeFromCart(Product P){
        if(P.getId() == Q.front().getId()){
            Q.deQueue();
        }
        else{
            Product temp = Q.front();
            Q.deQueue();
            Q.enQueue(temp);
            while(Q.front() != temp){
                Product temp2 = Q.front();
                Q.deQueue();
                if(temp2.getId()!= P.getId()){
                    Q.enQueue(temp2);
                }
            }
        }
    }
}
