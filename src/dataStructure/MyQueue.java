package dataStructure;
public class MyQueue {
    private Product front;
    private Product rear;

    public MyQueue() {
        this.front = null;
        this.rear = null;
    }

    public void enQueue(Product P) {
        if (this.front == null) {
            this.front = P;
            this.rear = P;
        } else {
            this.rear.setNext(P);
            this.rear = P;
        }
    }

    public Product deQueue() {
        if (this.front == null) {
            System.out.println("Queue is empty.");
            return null;
        } else {
            Product removed = this.front;
            this.front = this.front.getNext();
            if (this.front == null) {
                this.rear = null;
            }
            removed.setNext(null);
            return removed;
        }
    }

    public Product front() {
        return this.front;
    }

    public boolean isEmpty() {
        return this.front == null;
    }
}
