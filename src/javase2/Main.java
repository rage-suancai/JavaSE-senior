package javase2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件流
 * 要学习和使用IO 首先就要从最易于理解的读取文件开始说起
 *
 * 文件字节流
 * 首先介绍一下FileInputStream 通过它来获取文件的输入流
 *                  try {
 *                      FileInputStream inputStream = new FileInputStream("javase26.test.txt");
 *                      // 路径支持相对路径和绝对路径
 *                  } catch (FileNotFoundException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 相对路径是在当前运行的路径下寻找文件 而绝对路径 是从根目录开始寻找 路径分割符支持使用/或是'\\' 但是不能写为\因为它是转义字符
 *
 * 在使用完成一个流动之后 必须要关闭在流来完成对资源的释放 否则资源会被一直占用
 *                  FileInputStream inputStream = null; // 定义可以先放在try外部
 *                  try {
 *                      inputStream = new FileInputStream("javase26.test.txt");
 *                  } catch(IOException e) {
 *                      e.printStackTrace();
 *                  } finally {
 *                      if (inputStream != null) {
 *                          try { // 建议在finally中进行 因为这个是任何情况都必须要执行的
 *                              inputStream.close();
 *                          } catch (IOException e) {
 *                              e.printStackTrace();
 *                          }
 *                      }
 *                  }
 *
 * 虽然这样的写法才是最保险的 但是显得过于繁琐了 尤其是finally中再次嵌套了一个try-catch块
 * 因此在JDK1.7新增了try-with-resource语法 用于简化这样的写法(本质上还是和这样的操作一样 只是换了个写法)
 *                  // 注意 这种语法只支持实现了AutoCloseable接口的类
 *                  try (FileInputStream inputStream = new FileInputStream("javase26.test.txt")){ // 直接在try()中定义要在完成之后释放的资源
 *
 *                  } catch(IOException e) { // 这里变成IOException是因为调用close()可能会出现 而FileNotFoundException是继承自IOException的
 *                      e.printStackTrace();
 *                  }
 *                  // 无需再编写finally语句块 因为最后自动帮我们调用了close()
 *
 * 之后为了方便 我们都使用此语法进行操作
 *                  try (FileInputStream inputStream = new FileInputStream("javase26.test.txt")){
 *                      // 使用read()方法进行字符读取
 *                      System.out.println((char) inputStream.read()); // 读取一个字节的数据(英文字母只占1字节 中文占用2字)
 *                      System.out.println(inputStream.read()); // 唯一一个字节的内容已经读完了 再次读取返回-1表示没有内容了
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 使用read可以直接读取一个字节的数据 注意: 流的内容是有限的 读取一个少一个 我们如果想一次性读取全部的话 可以直接用一个while循环来完成:
 *                  try (FileInputStream inputStream = new FileInputStream("javase26.test.txt")){
 *                      int tmp;
 *                      while ((tmp = inputStream.read()) != -1) { // 通过while循环来一次性读完内容
 *                          System.out.println((char) tmp);
 *                      }
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 使用方法能查看当前可读的剩余字节数量(注意: 并不一定真实的数据量就是这么多 尤其是在网络I/O操作时 这个方法只能进行一个预估也可以说暂时能一次性读取的数量)
 *                  try (FileInputStream inputStream = new FileInputStream("javase26.test.txt")) {
 *                      System.out.println(inputStream.available()); // 查看剩余数量
 *                  }catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 当然 一个一个读取的效率太低了 那能否一次性全部读取呢 我们可以预置一个合适容量的byte数组来存放
 *                  try (FileInputStream inputStream = new FileInputStream("javase26.test.txt")) {
 *                      byte[] bytes = new byte[inputStream.available()]; // 我们可以提前准备好合适容量的byte数组来存放
 *                      System.out.println(inputStream.read(bytes)); // 一次性读取全部内容(返回值是读取的字节数)
 *                      System.out.println(new String(bytes)); // 通过String(byte[])构造方法得到字符串
 *                  }catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 也可以控制要读取的数量:
 *                  System.out.println(inputStream.read(bytes, 1, 2)); // 第二个参数是从给定数组的哪个位置开始放入内容 第三个参数是读取流中的字符串
 *
 * 注意: 一次性读取同单个读取一样 当没有任何数据可读时 依然会返回-1
 *
 * 通过skip()方法可以跳过指定数量的字节:
 *                  try (FileInputStream inputStream = new FileInputStream("javase26.test.txt")) {
 *                      System.out.println(inputStream.skip(1));
 *                      System.out.println((char) inputStream.read()); // 跳过了一个字节
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 注意: FileInputStream是不支持reset()的 虽然有这个方法 但是这里先不提及
 *
 * 既然有输入流 那么文件输出流也是必不可少的:
 *                  // 输出流也需要最后调用close()方法 并且同样支持try-with-resource
 *                  try (FileOutputStream outputStream = new FileOutputStream("output.txt")) {
 *                      // 注意: 若此文件不存在 会直接创建这个文件
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 输出流没有read()操作而是write()操作 使用方法同输入流一样 只不过现在的方向变为我们向文件里写入内容:
 *                  try (FileOutputStream outputStream = new FileOutputStream("output.txt")) {
 *                      outputStream.write('c'); // 同read一样 可以直接写入内容
 *                      outputStream.write("yxsnb".getBytes()); // 也可以直接写入byte[]
 *                      outputStream.write("yxsnb".getBytes(), 0, 1); // 同上输入流
 *                      outputStream.flush(); // 建议在最后执行一次刷新操作(强制写入)来保证数据正确写入到硬盘文件中
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 那么如果是我只想在文件尾部进行追加写入数据呢? 我们可以调用另一个构造方法来实现:
 *                  try (FileOutputStream outputStream = new FileOutputStream("output.txt", true)) {
 *                      outputStream.write("lb".getBytes()); // 现在只会进行追加写入 而不是直接替换原文件内容
 *                      outputStream.flush();
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 利用输入流和输出流 就可以轻松实现文件的拷贝了:
 *                  try (FileOutputStream outputStream = new FileOutputStream("output.txt", true)) {
 *                      FileInputStream inputStream = new FileInputStream("javase26.test.txt"); // 可以写入多个
 *                      byte[] bytes = new byte[10]; // 使用长度为10的byte[]做传输媒介
 *                      int tmp; // 存储本地读取字节数
 *                      while ((tmp = inputStream.read(bytes)) != -1) { // 直到读取完成为止
 *                          outputStream.write(bytes, 0 ,tmp); // 写入对应长度的数据到输出流
 *                      }
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 */
public class Main {

    public static void test1() {

        /*try {
            FileInputStream inputStream = new FileInputStream("javase26.test.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

        /*FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream("javase26.test.txt");
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/

        try (FileInputStream inputStream = new FileInputStream("javase26.test.txt")){

        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    static void test2() {

        /*try (FileInputStream inputStream = new FileInputStream("javase26.test.txt")){
            System.out.println((char) inputStream.read());
            System.out.println((char) inputStream.read());
            System.out.println((char) inputStream.read());
            System.out.println(inputStream.read());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*try (FileInputStream inputStream = new FileInputStream("javase26.test.txt")){
            int tmp;
            while ((tmp = inputStream.read()) != -1) {
                System.out.println((char) tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try (FileInputStream inputStream = new FileInputStream("javase26.test.txt")) {
            System.out.println(inputStream.available());
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void test3() {

        /*try (FileInputStream inputStream = new FileInputStream("javase26.test.txt")) {
            byte[] bytes = new byte[inputStream.available()];
            System.out.println(inputStream.read(bytes));
            //System.out.println(inputStream.read(bytes, 1, 2));
            System.out.println(new String(bytes));
        }catch (IOException e) {
            e.printStackTrace();
        }*/

        try (FileInputStream inputStream = new FileInputStream("javase26.test.txt")) {
            System.out.println(inputStream.skip(1));
            System.out.println((char) inputStream.read());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void test4() {

        /*try (FileOutputStream outputStream = new FileOutputStream("output.txt")) {
            outputStream.write('N');
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*try (FileOutputStream outputStream = new FileOutputStream("output.txt")) {
            outputStream.write("yxsnb".getBytes());
            outputStream.write("yxsnb".getBytes(), 0, 1);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*try (FileOutputStream outputStream = new FileOutputStream("output.txt", true)) {
            outputStream.write("lb".getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try (FileOutputStream outputStream = new FileOutputStream("output.txt", true)) {
            FileInputStream inputStream = new FileInputStream("javase26.test.txt");
            byte[] bytes = new byte[10];
            int tmp;
            while ((tmp = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0 ,tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //test1();
        //test2();
        //test3();
        test4();
    }

}
