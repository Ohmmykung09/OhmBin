package model;

import dataStructure.MyQueue;
import dataStructure.PriorityQueue;
import dataStructure.Product;

public class Screen {
    //Class Screen แสดงรายการสินค้า ประกอบไปด้วย
    private PriorityQueue PQ; //Priority Queue ไว้จัดเรียงรายการสินค้า
    private MyQueue Q; //Queue ช่วยในการ Delete และ Edit สินค้า
    private int id; //id รวมไว้จัดการ id ของสินค้า

    //Constructor สร้าง Queue และ Priority Queue ใหม่ปรับ id เริ่มให้เป็น 1
    public Screen() {
        this.PQ = new PriorityQueue();
        this.Q = new MyQueue();
        this.id = 1;
    }

    //Method การ Add สินค้าใหม่ลงตะกร้า //รับ parameter เป็นค่าภาพใน Product
    public void addProduct(int price, String name, int quantity, int priority, String imagePath) {
        Product P = new Product(this.id, name, price, quantity, priority, imagePath); //สร้าง Product ใหม่
        id++; //เพิ่มค่าตัวจัดการ id 
        this.PQ.enQueue(P); //เพิ่ม Product ลงใน Priority Queue
    }

    public void editProduct(int id, int price, String name, int quantity, int priority,
            String imagePath) { //Method แก้ไขข้อมูลสินค้า
        while (!PQ.isEmpty()) { //Loop ทุกตัวใน Priority Queue ไปเก็บใน Queue
            Product temp = PQ.front();
            PQ.deQueue();
            if (temp.getId() == id) { //กรณีเจอ (id ตรงกัน)
                Product edited =
                        new Product(temp.getId(), name, price, quantity, priority, imagePath); //สร้าง Product ใหม่ที่มีค่าใหม่
                Q.enQueue(edited); //Enqueue ลงไปแทนตัวเก่า
            } else {
                Q.enQueue(temp);
            }
        }
        //นำตัวจาก Queue กลับมาใน Priority Queue
        while (!Q.isEmpty()) {
            Product temp = Q.front();
            Q.deQueue();
            PQ.enQueue(temp);
        }
    }

    public void removeProduct(String name) { //Method ลบสินค้าจากชื่อ
        while (!PQ.isEmpty()) { //กรณี Priority Queue ไม่ว่าง
            Product temp = PQ.front();
            PQ.deQueue(); //loop ทุกตัวใน Priority Queue ไปใส่ใน Queue 
            if (temp.getName() != name) { //ถ้าไม่เจอ Product ชื่อตรงกับที่จะลบให้นำไปเก็บใน Queue ถ้าเจอจะข้าม
                Q.enQueue(temp);
            }
        }
        while (!Q.isEmpty()) { //นำทุกตัวใน Queue กลับมาใน Priority Queue
            Product temp = Q.front();
            Q.deQueue();
            PQ.enQueue(temp);
        }
    }

    public Product getFrontProduct() { //ดูสินค้าตัวแรกใน Priority Queue
        return PQ.front();
    }
}
