public class PriorityQueue{
    private Product front;
    private Product rear;
    public PriorityQueue(){
        this.front = null;
        this.rear = null;
    }

    //public void enQueue(int id,String name,int price,int quantity){
    public void enQueue(Product P){
        if(this.front == null){
            this.front = P;
            this.rear = P;
        }
        else if(P.getPriority() > this.front.getPriority()){
            Product temp = this.front;
            this.front = P;
            P.setNext(temp);
        }
        else{
            Product current = this.front;
            while(current.getNext() != null){
                if(P.getPriority() > current.getNext().getPriority()){
                    Product temp = current.getNext();
                    current.setNext(P);
                    P.setNext(temp);
                    break;   
                }
                current = current.getNext();
            }
            if(current.getNext() == null){
                current.setNext(P);
                this.rear = P;
            }
        }
    }

    public Product front(){
        return this.front;
    }

    public Product deQueue(){
        if(this.front==null){
            System.out.println("Empty");
            return null;
        }else{
            Product P = this.front;
            if(rear == front){
                rear=null;
                front = null;
            }else{
                this.front = this.front.getNext();
            }
            return P;
        }

    }

    public boolean isEmpty(){
        if(this.front==null && this.rear==null){
            return true;
        }
        else{
            return false;
        }
    }

}
