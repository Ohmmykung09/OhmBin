package model;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import dataStructure.Product;

public class VendingMachine {
    // Class Vending Machine เชื่อมระหว่างตะกร้ากับรายการสินค้า
    private Screen Scr; // รายการสินค้า
    private Cart cart; // ตะกร้า

    // Constructor สร้างรายการ และ ตะกร้าใหม่
    public VendingMachine() {
        this.Scr = new Screen();
        this.cart = new Cart();
    }

    public void AddToCart(Product P) { // Method เพิ่มสินค้าลงตะกร้า
        if (P.getQuantity() <= 0) { // กรณีสินค้าจำนวน <= 0
            JOptionPane.showMessageDialog(null, "This product is out of stock!", "Error",
                    JOptionPane.ERROR_MESSAGE); // แจ้ง error
            return;
        }

        List<Product> cartItems = cart.getAllItems();
        for (Product cartItem : cartItems) { // loop Product ในตะกร้าทุกตัว
            if (cartItem.getId() == P.getId()) { // กรณีเจอสินค้าเดียวกัน
                if (cartItem.getQuantity() + 1 > P.getQuantity()) { // Valitdate จำนวนสินค้า
                    JOptionPane.showMessageDialog(null, "Not enough stock available!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                cartItem.addQuantity(1); // ถ้า Validate ผ่านให้เพิ่มสินค้าในตะกร้า
                cart.calculatePrice(); // คำนวนราคารวมของตะกร้าใหม่
                return;
            }
        }
        // กรณีไม่เจอ
        Product Added = new Product(P.getId(), P.getName(), P.getPrice(), 1, P.getPriority(),
                P.getImagePath());
        cart.addToCart(Added); // Add สินค้าใหม่ลงไปในตะกร้าด้วย quantity = 1
        cart.calculatePrice(); // คำนวนราคารวมของตะกร้าใหม่
    }

    public void DeleteFromCart(Product P) { // Method ลบสินค้าออกจากตะกร้า
        cart.removeFromCart(P); // ลบสินค้า
        cart.calculatePrice(); // คำนวนราคารวมของตะกร้าใหม่
    }

    public void cashOut() { // Method การคิดเงิน
        List<Product> cartItems = cart.getAllItems();
        for (Product cartItem : cartItems) { // loop สินค้าทุกตัวในตะกร้า
            for (Product product : getAllProducts()) {
                if (product.getId() == cartItem.getId()) { // กรณีเจอสินค้า Id เดียวกันในรายการสินค้า
                    product.addQuantity(-cartItem.getQuantity()); // ลดสินค้าใน Stock
                    if (product.getQuantity() <= 0) { // ถ้าจำนวน <= 0
                        product.setPriority(-1); // ปรับ Priority เป็น -1
                        Scr.removeProduct(product.getName()); // Dequeue Product
                        Scr.addProduct(product.getPrice(), product.getName(), product.getQuantity(),
                                product.getPriority(), product.getImagePath()); // Enqueue ใหม่
                    }
                    product.UpdateSale(cartItem.getQuantity()); // Update ยอดขายรวม
                    break;
                }
            }
        }
        cart.clearCart(); // Clear ตะกร้า
    }

    // Method แก้ไขข้อมูลสินค้า
    public void editProduct(int id, int price, String name, int quantity, int priority,
            String imagePath) {
        Scr.editProduct(id, price, name, quantity, priority, imagePath); // แก้ไขสินค้าในรายการ
    }

    // Method ลบสินค้าจากรายการ ผ่าน ชื่อ
    public void removeProductFromScreen(String name) {
        Scr.removeProduct(name);
    }

    public Product getProductAt(int index) { // Method หา Product ตาม index
        int i = 0;
        Product current = Scr.getFrontProduct(); // สร้าง list ของ product

        while (current != null) {
            if (i == index) {
                return current; // ถ้าเจอให้ return Product นั้นออกมา
            }
            current = current.getNext();
            i++;
        }

        return null; // กรณีไม่เจอ return null
    }

    public List<Product> getAllProducts() { // Method นำรายการสินค้าออกมาเป็น List เพื่อแสดงผล
        List<Product> products = new ArrayList<>();
        Product current = Scr.getFrontProduct(); // สร้าง Current เป็น front ของรายการสินค้า

        while (current != null) { // loop จนครบ
            products.add(current); // เก็บลงใน List
            current = current.getNext();
        }

        return products;
    }

    public Cart getCart() { // เข้าถึงตะกร้าสินค้า
        return this.cart;
    }

    public Screen getScreen() { // เข้าถึงรายการสินค้า
        return this.Scr;
    }

    public void addProduct(int price, String name, int quantity, int priority, String imagePath) { // Add
                                                                                                   // สินค้าใหม่ลงในรายการสินค้า
        Scr.addProduct(price, name, quantity, priority, imagePath);
    }

    public void removeProduct(String name) { // ลบสินค้าจากรายการสินค้า
        Scr.removeProduct(name);
    }
}
