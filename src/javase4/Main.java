package javase4;

import java.io.*;
import java.util.Arrays;

/**
 * File类
 * File类专门用于表示一个文件或文件夹 只不过它只是代表这个文件 但并不是这个文件本身 通过File对象 可以更好地管理和操作硬盘上的文件
 *                  File file = new File("javase26.test.txt"); // 直接创建文件对象 可以是相对路径 也可以是绝对路径
 *                  System.out.println(file.exists()); // 此文件是否存在
 *                  System.out.println(file.length()); // 获取文件的大小
 *                  System.out.println(file.isDirectory()); // 是否为一个文件夹
 *                  System.out.println(file.canRead());  // 是否可读
 *                  System.out.println(file.canWrite()); // 是否可写
 *                  System.out.println(file.canExecute()); // 是否可执行
 *
 * 通过File对象 我们就能快速得到文件的所有信息 如果是文件夹 还可以获取文件夹内部的文件列表等内容:
 *                  File file = new File("/");
 *                  System.out.println(Arrays.toString(file.list())); // 快速获取文件夹下的文件名称列表
 *                  for (File f : file.listFiles()) { // 所有子文件的File对象
 *                      System.out.println(f.getAbsolutePath()); // 获取文件的绝对路径
 *                  }
 *
 * 如果我们希望读取某个文件的内容 可以直接将File作为参数传入字节流或是字符流:
 *                  File file = new File("javase26.test.txt");
 *                  try (FileInputStream inputStream = new FileInputStream(file)) {
 *                      System.out.println(inputStream.available());
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 练习: 尝试拷贝文件夹下的所有文件到另一个文件夹
 */
public class Main {

    static void test1() {

        File file = new File("javase26.test.txt");
        System.out.println(file.exists());
        System.out.println(file.length());
        System.out.println(file.isDirectory());
        System.out.println(file.canRead());
        System.out.println(file.canWrite());
        System.out.println(file.canExecute());

    }

    static void test2() {

        File file = new File("/");
        System.out.println(Arrays.toString(file.list()));
        for (File f : file.listFiles()) {
            System.out.println(f.getAbsolutePath());
        }

    }

    static void test3() {

        File file = new File("javase26.test.txt");
        try (FileInputStream inputStream = new FileInputStream(file)) {
            System.out.println(inputStream.available());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void test4() {

        File file = new File("YXS/");
        File target = new File("newYXS/");
        target.mkdir();
        for (File f : file.listFiles()) {
            try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(f));
                 BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(target.getPath() + f.getName()))) {

                byte[] bytes = new byte[1024];
                int tmp;
                while ((tmp = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, tmp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        //test1();
        //test2();
        //test3();
        test4();
    }

}
