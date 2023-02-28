package javase8;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * I/O编程实战 (图书管理系统)
 * 要求实现一个图书馆系统(控制台) 支持以下功能: 保存书籍信息(要求持久化) 查询 添加 删除 修改书籍信息
 */
public class Main {

    private static List<Book> LIST = new ArrayList<>();

    public static void main(String[] args) {

        readDate();
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("=============== 图书管理系统 ===============");
            System.out.println("1. 录入书籍信息");
            System.out.println("2. 修改书籍信息");
            System.out.println("3. 查询书籍列表");
            System.out.println("4. 删除书籍");
            System.out.println("(按0键退出管理系统)");

            String str = scanner.nextLine();
            switch (str) {
                case "1": insertBook(scanner); break;
                case "2": modifyBook(scanner); break;
                case "3": showBook(); break;
                case "4": deleteBook(scanner); break;
                case "0": saveDate(); scanner.close(); return;
            }
        }

    }

    @SuppressWarnings("unchecked")
    private static void readDate() { // IO流持久化

        File file = new File("src/javase8/data");
        if (file.exists()) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("src/javase8/data"))) {
                LIST = (List<Book>) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            LIST = new ArrayList<>();
        }

    }
    private static void saveDate() {

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("src/javase8/data"))) {
            outputStream.writeObject(LIST);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void insertBook(Scanner scanner) { // 增加

        LIST.add(new Book()
                .name(scanner.nextLine())
                .author(scanner.nextLine())
                .price(scanner.nextDouble()));

    }

    private static void modifyBook(Scanner scanner) { // 修改

        int i = 0;
        for (Book book : LIST) System.out.println(++i + "." + book);
        int index = scanner.nextInt();
        scanner.nextLine();
        if (index >= LIST.size()) System.out.println("错误的序号");
        else LIST
                    .get(index)
                    .name(scanner.nextLine())
                    .author(scanner.nextLine())
                    .price(scanner.nextDouble());

    }

    private static void showBook() { // 查询
        LIST.forEach(System.out::println);
    }

    private static void deleteBook(Scanner scanner) { // 删除

        int i = 0;
        for (Book book : LIST) System.out.println(++i + "." + book);
        int index = scanner.nextInt();
        if (index > LIST.size()) System.out.println("错误的序号");
        else LIST.remove(index);

    }

}
