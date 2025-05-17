package model;
import java.util.ArrayList;
import java.util.List;

import dataStructure.MyQueue;
import dataStructure.Product;

public class Cart {
    //Class ตะกร้าสินค้า ประกอบไปด้วย 
    private MyQueue Q; //Queue รายการสินค้าในตะกร้า
    private int number; //เลขตะกร้า
    private int sumPrice; //ราคารวมในตะกร้า

    //Constructor สร้าง Queue ใหม่ โดยเลขตะกร้าแรก = 1
    public Cart() {
        this.Q = new MyQueue();
        this.number = 1;
        this.sumPrice = 0;
    }

    //Method นำสินค้าเข้าตะกร้ามี Parameter เป็น Product
    public void addToCart(Product P) {
        if (Q.isEmpty()) { //กรณี Queue ว่าง
            Q.enQueue(P); //นำ Product เข้าไปใน Queue
        } else if (P.getId() == Q.front().getId()) { //กรณีมีสินค้าเดียวกันในตะกร้าตัวหน้าสุด
            Q.front().addQuantity(P.getQuantity()); //เพิ่มจำนวนสินค้าในตะกร้า
        } else { //กรณีอื่น
            Boolean find = false; 
            Product temp = Q.front();
            Q.deQueue();
            Q.enQueue(temp);
            //ทำการหาตัวอื่น ๆ ใน Queue
            while (Q.front() != temp) {
                Product current = Q.front();
                Q.deQueue();
                if (current.getId() == P.getId()) { //กรณีหาเจอ
                    current.addQuantity(P.getQuantity()); //เพิ่มจำนวนสินค้า
                    find = true;
                }
                Q.enQueue(current);
            }
            if (!find) { //กรณีหาไม่เจอ
                Q.enQueue(P); //เพิ่ม Product ลงไปใน Queue
            }
        }
    }

    public void removeFromCart(Product P) { //Method ลบสินค้าออกจากตะกร้า
        if (Q.isEmpty()) //กรณี Queue ว่างจะไม่ทำอะไร
            return;

        if (Q.front().getId() == P.getId()) { //กรณี Product ตรงกับ front
            Q.deQueue(); //Dequeue สินค้า
            calculatePrice(); //Update ราคารวม
            return;
        }
        //กรณีอื่น ๆ
        Product temp = Q.front();
        Q.deQueue();
        Q.enQueue(temp);
        //ทำการหาตัวอื่น ๆ ใน Queue
        while (Q.front() != temp) {
            Product current = Q.front();
            Q.deQueue();
            if (current.getId() == P.getId()) { //กรณีหาเจอ
                calculatePrice(); //จะไม่นำเข้า Queue ใหม่ และ Update ราคารวม
                return;
            } else { 
                Q.enQueue(current);
            }
        } // ถ้าหาไม่เจอก็จะไม่ทำอะไรเลย
    }

    public int getSumPrice() { //ดูราคารวม
        return this.sumPrice;
    }

    public void setSumPrice(int Price) { //set ราคารวมเป็นค่าใหม่
        this.sumPrice = Price;
    }

    public void calculatePrice() { //function คำนวนราคารวม
        setSumPrice(0);
        if (Q.isEmpty()) //ถ้า queue ว่างให้ return 
            return;

        //loop ทุกตัวใน Queue
        Product temp = Q.front();
        Q.deQueue();
        //set front ให้เป็นตัวสุดท้ายเพื่อทำ loop
        this.setSumPrice(getSumPrice() + temp.getQuantity() * temp.getPrice());
        Q.enQueue(temp);

        while (Q.front() != temp) {
            Product current = Q.front();
            Q.deQueue();
            this.setSumPrice(getSumPrice() + current.getQuantity() * current.getPrice()); //เพิ่มราคารวมเท่ากับราคาสินค้าคูณปริมาณ
            Q.enQueue(current);
        }
    }

    public int getNumber() { //ดูเลขตะกร้าสินค้า
        return this.number;
    }

    public void numberIncrement() { //เพิ่มเลขตะกร้าสินค้าไป 1
        this.number++;
    }

    public void clearCart() { //Method Clear ตะกร้า
        while (!Q.isEmpty()) { 
            Q.deQueue(); //Dequeue จนกว่า Queue จะว่าง
        }
        sumPrice = 0; //set ราคารวมเป็น 0
    }

    public List<Product> getAllItems() { //Mehod นำรายการสินค้าใน Queue ออกมาเป็น List เพื่อ display ผ่าน UI
        List<Product> items = new ArrayList<>(); //ประกาศ List ใหม่

        if (Q.isEmpty()) //กรณี Queue ว่าง
            return items;

        //loop ทุกตัวใน Queue 
        Product temp = Q.front();
        Q.deQueue();
        //นำ front ไปใส่ที่ท้ายสุดเพื่อเป็นเงื่อนไขจบ loop
        Q.enQueue(temp);
        items.add(temp); 

        while (Q.front() != temp) {
            Product curr = Q.front();
            Q.deQueue();
            Q.enQueue(curr);
            items.add(curr); //add Product ไปใน list 
        }

        return items;
    }
}
