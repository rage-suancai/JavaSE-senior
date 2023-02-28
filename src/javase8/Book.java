package javase8;

import java.io.Serializable;

public class Book implements Serializable { // 实现序列化接口

    String name;
    String author;
    double price;

    public Book name(String name) {
        this.name = name;
        return this;
    }
    public Book author(String author) {
        this.author = author;
        return this;
    }
    public Book price(double price) {
        this.price = price;
        return this;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                '}';
    }

}
