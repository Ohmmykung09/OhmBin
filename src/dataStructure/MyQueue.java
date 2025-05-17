package dataStructure;
public class MyQueue {
    //class Queue ประกอบไปด้วย front และ rear ตามสมบัติของ Queue 
    //โดยเป็น Queue ของ Class Product
    private Product front;
    private Product rear;

    public MyQueue() { //Constructor ประกาศ Queue ว่าง ๆ
        this.front = null;
        this.rear = null;
    }

    //Method การ Enqueue รับ parameter เป็น product 
    public void enQueue(Product P) {
        if (this.front == null) { //กรณีไม่มี front
            //set Product ให้เป็น front และ rear 
            this.front = P;
            this.rear = P;
        } else { //กรณีมี front
             
            this.rear.setNext(P); //set ตัวถัดไปของ rear เป็น product
            this.rear = P; //set rear เป็นค่าใหม่ 
        }
    }

    public Product deQueue() { //Method การ Dequeue
       
        if (this.front == null) {  //กรณีไม่มี front
            //print แสดงว่า Queue ว่าง และ return null
            System.out.println("Queue is empty.");
            return null;
        } else {
            Product removed = this.front; //เก็บค่า front
            this.front = this.front.getNext(); //set front เป็นตัวถัดไป
            if (this.front == null) { //ถ้า front ว่างให้ 
                this.rear = null; //set rear เป็น null
            }
            removed.setNext(null);
            return removed;  //return ค่า front ที่เก็บไว้
        }
    }

    public Product front() {     //เข้าถึง Product ตัวหน้าสุด
        return this.front;
    }

    public boolean isEmpty() { //Method ในการ check ว่า Priority Queue ว่างหรือไม่
        //ดูจาก front
        return this.front == null;
    }
}
