package javase23;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 反射创建类对象
 * 既然我们拿到了类的定义 那么是否可以通过Class对象来创建对象 调用方法 修改变量呢? 当然是可以的 那我们首先来探讨一下如何创建一个类的对象:
 *                  static void test1() {
 *
 *                      Class<Student> clazz = Student.class;
 *                      try {
 *                          Student student = clazz.newInstance();
 *                          student.yes();
 *                      } catch (InstantiationException | IllegalAccessException e) {
 *                          e.printStackTrace();
 *                      }
 *
 *                  }
 *
 *                  public class Student {
 *
 *                      public void yes() {
 *                          System.out.println("萨日朗");
 *                      }
 *
 *                  }
 *
 * 通过使用newInstance()方法来创建对应类型的实例 返回泛型T
 * 注意: 它会抛出InstantiationException和IllegalAccessException异常 那么什么情况下会出现异常呢?
 *                  public class Student {
 *
 *                      public Student(String text) {
 *
 *                      }
 *
 *                      public void yes() {
 *                          System.out.println("萨日朗");
 *                      }
 *
 *                  }
 *
 *                  static void test1() {
 *
 *                      Class<Student> clazz = Student.class;
 *                      try {
 *                         Student student = clazz.newInstance();
 *                         student.yes();
 *                      } catch (InstantiationException | IllegalAccessException e) {
 *                          e.printStackTrace();
 *                      }
 *
 *                  }
 *
 * 当类默认的构造方法被带参构造覆盖时 会出现InstantiationException异常 因为newInstance()只适用于默认无参构造
 *                  public class Student() {
 *
 *                      private Student(){}
 *
 *                      public void yes() {
 *                          System.out.println("萨日朗");
 *                      }
 *
 *                  }
 *
 *                  static void test1() {
 *
 *                      Class<Student> clazz = Student.class;
 *                      try {
 *                         Student student = clazz.newInstance();
 *                         student.yes();
 *                      } catch (InstantiationException | IllegalAccessException e) {
 *                          e.printStackTrace();
 *                      }
 *
 *                  }
 *
 * 当默认无参构造的权限不是public时 会出现IllegalAccessException异常 表示我们无权去调用默认构造方法
 * 在JDK9之后 不再推荐使用newInstance()方法了 而是使用我们接下来要介绍到的 通过获取构造器 来实例化对象:
 *                  public class Student() {
 *
 *                      public Student(String str) {
 *                          System.out.println(str);
 *                      }
 *
 *                      public void yes() {
 *                          System.out.println("萨日朗");
 *                      }
 *
 *                  }
 *
 *                  static void test2() {
 *
 *                      Class<Student> clazz = Student.class;
 *                      try {
 *                          Student student = clazz.getConstructor(String.class).newInstance("what's up");
 *                          student.yes();
 *                      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
 *                          e.printStackTrace();
 *                      }
 *
 *                  }
 * 
 * 通过获取类的构造方法(构造器)来创建对象实例 会更加合理 我们可以使用getConstructor()方法来获取类的构造方法
 * 同时我们需要向其中填入参数 也就是构造方法需要的类型 当然这里只演示了 那么 当服务权限不是public的时候呢?
 *                  private Student(String text) {
 *                      System.out.println(text);
 *                  }
 *                  private Student(String name, int age) {
 *                      this.name = name;
 *                      this.age = age;
 *                  }
 *
 *                  Class<Student> clazz = Student.class;
 *                  try {
 *                      //Student student = clazz.getConstructor(String.class).newInstance("what's up");
 *                      student.yes();
 *                  } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 我们发现 当访问权限不足时 会无法找到此构造方法 那么如何找到非public的构造方法呢?
 *                  Class<Student> clazz = Student.class;
 *                  Constructor<Student> constructor = clazz.getDeclaredConstructor(String.class, int.class);
 *                  constructor.setAccessible(true); // 修改访问权限
 *                  Student student = constructor.newInstance("what's up", 20);
 *                  student.yes();
 *
 * 使用getDeclaredConstructor()方法可以找到类中的非public构造方法 但是在使用之前 外面需要先修改访问权限
 * 在修改访问权限之后 就可以使用非public方法了(这意味着 反射可以无视权限修饰符访问类的内容)
 */
public class Main {

    static void test1() {

        Class<Student> clazz = Student.class;
        try {
           Student student = clazz.newInstance();
           student.yes();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    static void test2() {

        /*Class<Student> clazz = Student.class;
        try {
            //Student student = clazz.getConstructor(String.class).newInstance("what's up");
            Student student = clazz.getConstructor(String.class, int.class).newInstance("what's up", 20);
            student.yes();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }*/

        Class<Student> clazz = Student.class;
        try {
            Constructor<Student> constructor = clazz.getDeclaredConstructor(String.class, int.class);
            constructor.setAccessible(true);
            Student student = constructor.newInstance("what's up", 20);
            student.yes();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //test1();
        test2();
    }

}
