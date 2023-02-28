package javase7;

import javase7.people.People;

import java.io.*;

/**
 * 数据流
 * 数据流DataInputStream也是FilterInputStream的子类 同样采用装饰者模式 最大的不同是它支持基本数据类型的直接读取:
 *                  try (DataInputStream dataInputStream1 = new DataInputStream(new FileInputStream("javase26.test.txt"))) {
 *                      System.out.println(dataInputStream1.readBoolean()); // 直接将数据读取为任意基本数据类型
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 用于写入基本数据类型:
 *                  try (DataOutputStream dataInputStream2 = new DataOutputStream(new FileOutputStream("text.txt"))) {
 *                      dataInputStream2.writeBoolean(false);
 *                  } catch (IOException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 注意: 写入的是二进制数据 并不是写入的字符串 使用DataInputStream可以读取 一般他们是配合一起使用的
 *
 * 对象流
 * 虽然基本数据类型能够读取写入基本数据类型 那么能否将对象也支持呢? ObjectOutputStream不仅支持基本数据类型
 * 通过对对象的序列化操作 以某种格式保存对象 来支持对象类型的IO 注意: 它不是继承自FilterInputStream的
 *                  try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("output.txt"));
 *                       ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("output.txt"))) {
 *
 *                      People people = new People("yxs");
 *                      outputStream.writeObject(people);
 *                      outputStream.flush();
 *                      people = (People) inputStream.readObject();
 *                      System.out.println(people.name);
 *                  } catch (IOException | ClassNotFoundException e) {
 *                      e.printStackTrace();
 *                  }
 *
 *                  public class People implements Serializable { // 必须实现Serializable才能实现序列化
 *
 *                      String name;
 *
 *                      public People(String name) {
 *                          this.name = name;
 *                      }
 *
 *                  }
 *
 * 在我们后续的操作中 有可能会使得这个类的一些结构发生变化 而原来保存的数据只适用于之前版本的那个类 因此我们需要一种方法来区分类的不同版本:
 *                  public class People implements Serializable {
 *
 *                      private static final long serialVersionUID = 1234567; // 在序列化时 会被自动添加这个属性 它代表当前类的版本 我们也可以手动指定版本
 *
 *                      public String name;
 *
 *                      public People(String name) {
 *                          this.name = name;
 *                      }
 *
 *                  }
 *
 * 当发生版本不匹配时 会无法反序列化为对象
 *
 * 如果我们不希望某些属性参与到序列化中进行保存 我们可以添加transient关键字:
 *                  public class People implements Serializable {
 *
 *                      transient String name; // 虽然能得到对象 但是name属性并没有保存 因此为null
 *
 *                      public People(String name) {
 *                          this.name = name;
 *                      }
 *
 *                  }
 *
 * 其实我们可以看到 在一些JDK内部的源码中 也存在大量的transient关键字 使得某些属性不参与序列化 取消这些不必要保存的属性 可以节省数据空间占用以及减少序列化时间
 */
public class Main {

    static void test1() {

        try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream("text.txt"));
             DataInputStream dataInputStream = new DataInputStream(new FileInputStream("text.txt"))) {

            dataOutputStream.writeFloat(1.58F);
            System.out.println(dataInputStream.readFloat());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void test2() {

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("output.txt"));
             ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("output.txt"))) {

            People people = new People("yxs");
            outputStream.writeObject(people);
            outputStream.flush();
            people = (People) inputStream.readObject();
            System.out.println(people.name);
            System.out.println(people.age);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //test1();
        test2();
    }

}
