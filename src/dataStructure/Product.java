package dataStructure;
public class Product {
    //Product class ของสินค้าซึ่งจะใช้เป็น node ประกอบไปด้วย
    private int id; // id สินค้าแต่ละชนิด
    private String name; //ชื่อสินค้า
    private int quantity; //จำนวนสินค้า
    private int total_sale; //ยอดขาย
    private int price; //ราคาสินค้า
    private Product next; // ใช้บอก node ถัดไป
    private int priority; //priority ของสินค้า 2 คือสินค้าใหม่, 1 คือสินค้าขายดี, 0 คือสินค้าปกติ, และ -1 คือสินค้าหมด
    public String imagePath; //บอกตำแหน่ง file ในเครื่องเพื่อใช้ในการโหลดรูปภาพสินค้า

    //Constructor สร้างทุกอย่างยกเว้น nextnode และ ยอดขาย
    public Product(int id, String name, int  price, int quantity, int priority, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.total_sale = 0;
        this.next = null;
        this.priority = priority;
        this.imagePath = imagePath;
    }

    //นำค่า id ออกมา
    public int getId() {
        return this.id;
    }
    //นำค่าราคา ออกมา
    public int getPrice() {
        return this.price;
    }
    //ตั้งราคาใหม่
    public void setPrice(int n) {
        this.price = n;
    }
    //นำชื่อสินค้าออกมา
    public String getName() {
        return this.name;
    }
    //ดูจำนวนสินค้า
    public int getQuantity() {
        return this.quantity;
    }
    //ดู priority ของสินค้า
    public int getPriority() {
        return this.priority;
    }
    //ปรับ priority ของสินค้า
    public void setPriority(int n) {
        this.priority = n;
    }
    //เพิ่มจำนวนสินค้า
    public int addQuantity(int n) {
        this.quantity = this.quantity + n;
        return this.quantity;
    }
    //Update เพิ่มยอดขายสินค้า
    public int UpdateSale(int n) {
        this.total_sale = this.total_sale + n;
        return this.total_sale;
    }
    //ดูยอดขายรวม
    public int getTotalSale() {
        return this.total_sale;
    }
    //ดู product ถัดไป
    public Product getNext() {
        return this.next;
    }
    //กำหนด product ตัวถัดไป
    public void setNext(Product P) {
        this.next = P;
    }
    //ดู Image path
    public String getImagePath() {
        return this.imagePath;
    }
    //เปลี่ยน Image path ของสินค้า
    public void setImagePath(String path) {
        this.imagePath = path;
    }
}
