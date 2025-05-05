package model;
import java.util.ArrayList;
import java.util.List;

import dataStructure.MyQueue;
import dataStructure.Product;

public class Cart {
    private MyQueue Q;
    private int number;
    private int sumPrice;

    public Cart() {
        this.Q = new MyQueue();
        this.number = 1;
        this.sumPrice = 0;
    }

    public void addToCart(Product P) {
        if (Q.isEmpty()) {
            Q.enQueue(P);
        } else if (P.getId() == Q.front().getId()) {
            Q.front().addQuantity(P.getQuantity());
        } else {
            Boolean find = false;
            Product temp = Q.front();
            Q.deQueue();
            Q.enQueue(temp);
            while (Q.front() != temp) {
                Product current = Q.front();
                Q.deQueue();
                if (current.getId() == P.getId()) {
                    current.addQuantity(P.getQuantity());
                    find = true;
                }
                Q.enQueue(current);
            }
            if (!find) {
                Q.enQueue(P);
            }
        }
    }

    public void removeFromCart(Product P) {
        if (Q.isEmpty())
            return;

        if (Q.front().getId() == P.getId()) {
            Q.deQueue();
            calculatePrice();
            return;
        }

        Product temp = Q.front();
        Q.deQueue();
        Q.enQueue(temp);

        while (Q.front() != temp) {
            Product current = Q.front();
            Q.deQueue();
            if (current.getId() == P.getId()) {
                calculatePrice();
                return;
            } else {
                Q.enQueue(current);
            }
        }
    }

    public int getSumPrice() {
        return this.sumPrice;
    }

    public void setSumPrice(int Price) {
        this.sumPrice = Price;
    }

    public void calculatePrice() {
        setSumPrice(0);
        if (Q.isEmpty())
            return;

        Product temp = Q.front();
        Q.deQueue();
        this.setSumPrice(getSumPrice() + temp.getQuantity() * temp.getPrice());
        Q.enQueue(temp);

        while (Q.front() != temp) {
            Product current = Q.front();
            Q.deQueue();
            this.setSumPrice(getSumPrice() + current.getQuantity() * current.getPrice());
            Q.enQueue(current);
        }
    }

    public int getNumber() {
        return this.number;
    }

    public void numberIncrement() {
        this.number++;
    }

    public void clearCart() {
        while (!Q.isEmpty()) {
            Q.deQueue();
        }
        sumPrice = 0;
    }

    public List<Product> getAllItems() {
        List<Product> items = new ArrayList<>();

        if (Q.isEmpty())
            return items;

        Product temp = Q.front();
        Q.deQueue();
        Q.enQueue(temp);
        items.add(temp);

        while (Q.front() != temp) {
            Product curr = Q.front();
            Q.deQueue();
            Q.enQueue(curr);
            items.add(curr);
        }

        return items;
    }
}
