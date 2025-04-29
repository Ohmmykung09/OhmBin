public class myQueue {
    private Product front;
    private Product rear;
    public myQueue(){
        this.front = null;
        this.rear = null;
    }

    public void enQueue(Product P){
        if(this.front ==null){
            this.front = P;
            this.rear = P;
        }else{
            this.rear.setNext(P);
            this.rear=P;
        }
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
    public Product front(){
        return this.front;
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
