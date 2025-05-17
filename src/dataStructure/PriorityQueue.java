package dataStructure;
public class PriorityQueue {
    //class Priority Queue ประกอบไปด้วย front และ rear ตามสมบัติของ Queue 
    //โดยเป็น Priority Queue ของ Class Product
    private Product front;
    private Product rear;

    //Constructor ประกาศ Priority Queue ว่าง ๆ
    public PriorityQueue() {
        this.front = null;
        this.rear = null;
    }

    public void enQueue(Product P) { //Method ในการ Enqueue สินค้า มี parameter เป็น Product
        //กรณียังไม่มี front ให้ set Product นั้นเป็น front
        if (this.front == null) {
            front = rear = P;
        //กรณี priority สูงกว่า front ให้ set Product นั้นเป็น front
        } else if (P.getPriority() > this.front.getPriority()) {
            P.setNext(this.front);
            this.front = P;
        //กรณีอื่น ๆ ให้ loop จนกว่าจะเจอ Product ที่มี priority ต่ำกว่าและทำการแทรกไว้ข้างหน้า
        } else {
            Product current = this.front;
            while (current.getNext() != null
                    && P.getPriority() <= current.getNext().getPriority()) {
                current = current.getNext();
            }
            P.setNext(current.getNext());
            current.setNext(P);

            if (P.getNext() == null) {
                this.rear = P;
            }
        }
    }
   
    public Product front() {  //เข้าถึง product ตัวหน้าสุด
        return this.front;
    }
    
    public Product deQueue() { //Method Dequeue นำ front ออกจาก Priority Queue
        if (this.front == null) { //กรณีไม่มี front
            //print คำว่า Empty และ return null ออกมา
            System.out.println("Empty");
            return null;
        } else { //กรณีมี front 
            Product removed = front; //เก็บค่า Product front ไว้    
            front = front.getNext(); //set front เป็นตัวถัดไป
            
            if (front == null) { //ถ้าไม่มี front 
                rear = null; //set rear เป็น null 
            }
            removed.setNext(null);
            return removed; //return ค่า front ที่เก็บไว้
        }

    }
    
    public boolean isEmpty() { //Method ในการ check ว่า Priority Queue ว่างหรือไม่
        return this.front == null; //ดูจาก front
    }

}
