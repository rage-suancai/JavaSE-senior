package javase5;

import java.io.*;

/**
 * 缓冲流
 * 虽然普通的文件流读取文件数据非常便捷 但是每次都需要从外部I/O设备去获取数据 由于外部I/O设备的速度一般都达不到内存的读取速度
 * 很有可能造成程序反应迟钝 因此性能还不够高 而缓冲流正如其名称一样 它能够提供一个缓冲 提前将部分内容存入内存(缓冲区) 在下次读取时
 * 如果缓冲区中存在此数据 则无需再去请求外部设备 同理 当向外部设备写入数据时 也是由缓冲区处理 而不是直接向外部设备写入
 *
 * 缓冲字节流
 * 要创建一个缓冲字节流 只需要将原本的流作为构造参数传入BufferedInputStreamStream即可:
 *                  try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("javase26.test.txt"))) {
 *                      System.out.println((char) bufferedInputStream.read());
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 实际上进行I/O操作的并不是BufferedInputStream 而是我们传入的FileInputStream 而BufferedInputStream虽然有着同样的方法
 * 但是进行了一些额外的处理然后再调用FileInputStream的同名方法 这样的写法称为装饰者模式
 *                  public void close() throws IOException {
 *
 *                      byte[] buffer;
 *                      while ((buffer = buf) != null) {
 *                          if (bufUpdater.compareAndSet(this, buffer, null)) { // CAS无锁算法 并发会用到 暂时不管
 *                              InputStream input = in;
 *                              in = null;
 *                              if (input != null)
 *                                  input.close();
 *                              return;
 *                          }
 *                          // Else retry in case a new buf was CASed in fill()
 *                      }
 *
 *                  }
 *
 * 实际上这种模式是父类FilterInputStream提供的规范 后面我们还会讲到更多FilterInputStream的子类
 *
 * 我们可以发现再BufferedInputStream中还存在一个专门用于缓存的数组:
 *                  protected volatile byte buf[];
 *
 * I/O操作一般不能重复读取内容(比如键盘发送的信号 主机接收了就没了) 而缓冲流提供了缓冲机制 一部分内容可以被暂时保存
 * BufferedInputStream支持 reset()和mark()操作 首先我们来看看mark()方法的介绍:
 *                  public synchronized void mark(int readlimit) {
 *                      marklimit = readlimit;
 *                      markpos = pos;
 *                  }
 *
 * 当调用mark()之后 输入流会以某种方式保留之后读取的readlimit数量的内容 当读取的内容数量超过readlimit则之后的内容不会
 *                  try(BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("javase26.test.txt"))) {
 *                      bufferedInputStream.mark(1);
 *                      System.out.println((char) bufferedInputStream.read());
 *                      //System.out.println((char) bufferedInputStream.read());
 *                      bufferedInputStream.reset();
 *                      System.out.println((char) bufferedInputStream.read());
 *                      //System.out.println((char) bufferedInputStream.read());
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 我们发现虽然后面的部分没有保存 但是依然能够正常读取 其实mark()后保存的读取内容是取readlimit和BufferedInputStream类的缓冲区大小两者中最大的值
 * 而并发完全由readlimit确定 因此我们限制一下缓冲区大小 再来观察一下结果:
 *                  try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("javase26.test.txt"), 1)) { // 将缓冲区大小设置为1
 *                      bufferedInputStream.mark(1); // 只保留之后的1个字符
 *                      System.out.println((char) bufferedInputStream.read());
 *                      System.out.println((char) bufferedInputStream.read()); // 已经超过了readlimit 继续读取会导致mark失效
 *                      bufferedInputStream.reset(); // mark已经失效 无法reset()
 *                      System.out.println((char) bufferedInputStream.read());
 *                      System.out.println((char) bufferedInputStream.read());
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 了解完bufferedInputStream之后 我们再来看看BufferedOutputStream 其实和BufferedInputSteam原理差不多 只是反向操作:
 *                  try (BufferedOutputStream bufferedInputStream = new BufferedOutputStream(new FileOutputStream("javase26.test.txt"))) {
 *                      bufferedInputStream.write("yxsnb".getBytes());
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 操作和FileOutputStream一致 这里就不多做介绍了
 *
 * 缓冲字符流
 * 缓冲字符流和缓冲字节流一样 也也有一个专门的缓冲区 BufferedReader构造时需要传入一个Reader对象:
 *                  try (BufferedReader reader = new BufferedReader(new FileReader("javase26.test.txt"))) {
 *                      System.out.println((char) reader.read());
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 使用和reader也是一样的 内部也包含一个缓存数组:
 *                  private char cb[];
 *
 * 相比Reader更方便的是 它支持按行读取:
 *                  try (BufferedReader bufferedReader = new BufferedReader(new FileReader("javase26.test.txt"))) {
 *                      System.out.println(bufferedReader.readLine());
 *                      System.out.println(bufferedReader.readLine()); // 按行读取
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 读取后直接得到一个字符串 当然 它还能把每一行内容依次转换为集合类提到的Stream流:
 *                  try (BufferedReader reader = new BufferedReader(new FileReader("javase26.test.txt"))) {
 *                      reader
 *                              .lines()
 *                              .limit(2)
 *                              .distinct()
 *                              .sorted()
 *                              .forEach(System.out::println);
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 它同样支持mark()和reset()操作:
 *                  try (BufferedReader reader = new BufferedReader(new FileReader("javase26.test.txt"))) {
 *                      reader.mark(1);
 *                      System.out.println((char) reader.read());
 *                      System.out.println((char) reader.read());
 *                      reader.reset();
 *                      System.out.println((char) reader.read());
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * BufferedReader处理纯文本时就更加方便了 BufferedWriter在处理时也同样方便:
 *                  try (BufferedWriter reader = new BufferedWriter(new FileWriter("javase26.test.txt"))) {
 *                      reader.newLine(); // 使用newLine进行换行
 *                      reader.write("汉堡做的行不行"); // 可以直接写入一个字符串
 *                      reader.flush(); // 清空缓冲区
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 */
public class Main {

    static void test1() {

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("javase26.test.txt"))) { // 传入FileInputStream
            System.out.println((char) bufferedInputStream.read()); // 操作和原来的流是一样的
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void test2() {

        /*try(BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("javase26.test.txt"))) {
            bufferedInputStream.mark(1);
            System.out.println((char) bufferedInputStream.read());
            //System.out.println((char) bufferedInputStream.read());
            bufferedInputStream.reset();
            System.out.println((char) bufferedInputStream.read());
            //System.out.println((char) bufferedInputStream.read());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("javase26.test.txt"), 1)) {
            bufferedInputStream.mark(1);
            System.out.println((char) bufferedInputStream.read());
            System.out.println((char) bufferedInputStream.read());
            bufferedInputStream.reset();
            System.out.println((char) bufferedInputStream.read());
            System.out.println((char) bufferedInputStream.read());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void test3() {

        try (BufferedOutputStream bufferedInputStream = new BufferedOutputStream(new FileOutputStream("javase26.test.txt"))) {
            bufferedInputStream.write("yxsnb".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void test4() {

        /*try (BufferedReader reader = new BufferedReader(new FileReader("javase26.test.txt"))) {
            System.out.println((char) reader.read());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("javase26.test.txt"))) {
            System.out.println(bufferedReader.readLine());
            System.out.println(bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void test5() {

        /*try (BufferedReader reader = new BufferedReader(new FileReader("javase26.test.txt"))) {
            reader
                    .lines()
                    .limit(2)
                    .distinct()
                    .sorted()
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*try (BufferedReader reader = new BufferedReader(new FileReader("javase26.test.txt"))) {
            reader.mark(1);
            System.out.println((char) reader.read());
            System.out.println((char) reader.read());
            reader.reset();
            System.out.println((char) reader.read());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try (BufferedWriter reader = new BufferedWriter(new FileWriter("javase26.test.txt"))) {
            reader.newLine();
            reader.write("汉堡做的行不行");
            reader.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //test1();
        //test2();
        //test3();
        //test4();
        test5();
    }

}
