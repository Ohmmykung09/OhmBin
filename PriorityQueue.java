public class PriorityQueue{
    private Product front;
    private Product rear;

    public PriorityQueue(){
        this.front = null;
        this.rear = null;
    }

    public void enQueue(Product P){
        if(this.front == null){
            front = rear = P;
        }
        else if(P.getPriority() > this.front.getPriority()){
            P.setNext(this.front);
            this.front = P;
        }
        else{
            Product current = this.front;
            while(current.getNext() != null && P.getPriority() <= current.getNext().getPriority()){
                current = current.getNext();
            }
            P.setNext(current.getNext());
            current.setNext(P);

            if(P.getNext() == null){
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
            Product removed = front;
            front = front.getNext();
            if (front == null) {
                rear = null;
            }
            removed.setNext(null);
            return removed;
        }

    }

    public boolean isEmpty(){
        return this.front == null;
    }

}
