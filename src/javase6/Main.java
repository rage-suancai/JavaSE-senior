package javase6;

import java.io.*;

/**
 * 转换流
 * 有时会遇到这样一个很麻烦的问题: 我这里读取的是一个字符串或是一个字符 但是我只能往一个OutputStream里输出
 * 但是OutputStream又只支持Byte类型 如果要往里面写入内容 进行数据转换就会很麻烦 那么能否有更加简便的方式来做这样的事情呢
 *                  try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("javase26.test.txt"))) { // 虽然给定的是FileOutputStream 但是现在支持以Writer的方式进行写入
 *                      writer.write("yxsnb"); // 以操作writer的样子写入OutputStream
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *     l             }
 *
 * 同样的 我们现在只拿到了一个inputStream 但是我们希望能够按字符的方式读取 我们就可以使用InputStreamReader来帮助我们实现:
 *                  try (InputStreamReader reader = new InputStreamReader(new FileInputStream("javase26.test.txt"))) { // 虽然给定的是FileInputStream 但是现在支持以Reader的方式进行读取
 *                      System.out.println((char) reader.read());
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * InputStreamReader和OutputSteamWriter本质上是Reader和Writer 因此可以直接放入BufferedReader来实现更加方便的操作
 *
 * 打印流
 * 打印流其实我们从一开始就在使用了 比如System.out就是一个PrintStream PrintSteam也继承自FilterOutputStream类因此依然是装饰我们传入的输出流
 * 但是它存在自动刷新机制 例如当向PrintStream流中写入一个字数组后自动调用flush()方法 PrintStream也永远不会抛出异常
 * 而是使用内部检查机制checkError()方法进行错误检查 最方便的是 它能够格式化任意的类型 将它们以字符串的形式写入到输出流
 *                  public final static PrintStream out = null;
 *
 * 可以看到System.out也是PrintStream 不过默认的是向控制台打印 我们也可以让它向文件中打印:
 *                  try (PrintStream stream = new PrintStream(new FileOutputStream("javase26.test.txt"))) {
 *                      stream.println("yxsnb"); // 其实System.out就是一个PrintStream
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 我们平时使用的println方法就是PrintStream中的方法 它会直接打印基本数据类型或是调用对象的toString()方法得到一个字符串 并将字符串转换为字符 放入缓冲区再经过转换流输出到给定的输出流上
 *
 * 因此实际上内部还包含这两个内容:
 *                  private BufferedWriter textOut;
 *                  private OutputStreamWriter charOut;
 *
 * 与此相同的还有一个PrintWriter 不过他们的功能基本一致 PrintWriter的构造方法可以接受一个Writer作为参数 这里就不再做过多阐述了
 */
public class Main {

    static void test1() {

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("javase26.test.txt"))) {
            writer.write("叶");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream("javase26.test.txt"))) {
            System.out.println((char) reader.read());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void test2() {

        try (PrintStream stream = new PrintStream(new FileOutputStream("javase26.test.txt"))) {
            stream.println("yxsnb");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //test1();
        test2();
    }

}
