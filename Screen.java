public class Screen {
    private PriorityQueue PQ;
    private myQueue Q;
    private int id;

    public Screen(){
        this.PQ = new PriorityQueue();
        this.Q = new myQueue();
        this.id = 1;
    }

    public void addProduct(int price, String name,int quantity){
        Product P = new Product(this.id,name,price,quantity);
        id++;
        this.PQ.enQueue(P);
    }

    public void removeProduct(String name){
        while(!PQ.isEmpty()){
            Product temp = PQ.front();
            PQ.deQueue();
            if(temp.getName()!=name){
                Q.enQueue(temp);
            }
        }
        while(!Q.isEmpty()){
            Product temp = Q.front();
            Q.deQueue();
            PQ.enQueue(temp);
        }
    }
}
