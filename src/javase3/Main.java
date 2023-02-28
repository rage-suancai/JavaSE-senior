package javase3;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 文件字符流
 * 字符流不同于字节 字符流是以一个具体的字符进行读取 因此它只适合读纯文本的文件 如果是其他类的文件不适用:
 *                  try(FileReader reader = new FileReader("javase26.test.txt")) {
 *                      reader.skip(1); // 现在跳过的是一个字符
 *                      System.out.println((char) reader.read()); // 现在是按字符进行读取 而不是字节 因此可以直接读取到中文字符
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 同理 字符流只支持char[]类型作为存储:
 *                  try (FileReader reader = new FileReader("javase26.test.txt")) {
 *                      char[] str = new char[10];
 *                      reader.read(str);
 *                      System.out.println(str); // 直接读取到char[]中
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 既然有了Reader肯定也有Writer:
 *                  try (FileWriter writer = new FileWriter("output.txt")) {
 *                      writer.getEncoding(); // 支持获取编码(不同的文本文件可能会有不同的编码类型)
 *                      writer.write('牛');
 *                      writer.write('牛'); // 其实功能和writer一样
 *                      writer.flush(); // 刷新
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 我们发现不仅有writer()方法 还有一个append()方法 但是实际上他们效果一样的 看源码:
 *                  public Writer append(CharSequence csq) throws IOException {
 *                      if (csq == null)
 *                          write("null");
 *                      else
 *                          write(csq.toString());
 *                      return this;
 *                  }
 *
 * append()支持SpringBuilder那样的链式调用 返回的是Writer对象本身
 *
 * 练习: 尝试一下用Reader和Writer来拷贝纯文本文件
 */
public class Main {

    static void test1() {

        try (FileReader reader = new FileReader("javase26.test.txt")) {
            reader.skip(1);
            System.out.println((char) reader.read());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void test2() {

        try (FileReader reader = new FileReader("javase26.test.txt")) {
            char[] str = new char[4];
            reader.read(str);
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void test3() {

        /*try (FileWriter writer = new FileWriter("output.txt")) {
            System.out.println(writer.getEncoding());
            writer.write('牛');
            writer.write('牛');
            // writer.append('牛');
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try (FileReader reader = new FileReader("javase26.test.txt");
             FileWriter writer = new FileWriter("output.txt")) {

            char[] chars = new char[10];
            int tmp;
            while ((tmp = reader.read(chars)) != -1) {
                writer.write(chars, 0, tmp);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        // test1();
        //test2();
        test3();
    }

}
