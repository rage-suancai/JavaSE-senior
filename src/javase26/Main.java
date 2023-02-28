package javase26;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 类加载器
 * 我们接着来介绍一下类加载器 实际上类加载器就是用于加载一个类的 但是类加载器并不是只有一个
 *
 * 思考: 既然说Class对象和加载的类唯一对应 那如果我们手动创建一个JDK包名一样 同时类型名也保持一致 JVM会加载这个类吗?
 *                  package java.lang;
 *
 *                  public class String { // JDK提供的String类也是
 *
 *                      public static void main() {
 *                          System.out.println("我姓马, 我叫马nb");
 *                      }
 *
 *                  }
 *
 * 我们发现 会出现以下报错:
 *                  错误: 在类 java.lang.String 中找不到 main 方法, 请将 main 方法定义为:
 *                     public static void main(String[] args)
 *                  否则 JavaFX 应用程序类必须扩展javafx.application.Application
 *
 * 但是我们明确在自己写的String类中定义了main方法啊 为什么会找不到此方法呢? 实际上这个ClassLoader的双亲委派机制 在保护Java程序的正常运行:
 *      实际上类最开始是由BootstrapClassLoader进行加载 BootstrapClassLoader用于加载JDK提供的类
 *      而我们自己编写的类实际上是AppClassLoader加载的 只有BootstrapClassLoader都没有加载的类 才会让AppClassLoader来加载
 *      因此我们自己编写的同名包同名类不会被加载 而实际要去启动发是真正的Strong类 也就自然找不到main方法了
 *
 *                  static void test1() {
 *
 *                      System.out.println(Main.class.getClassLoader()); // 查看当前类的类加载器
 *                      System.out.println(Main.class.getClassLoader().getParent()); // 父加载器
 *                      System.out.println(Main.class.getClassLoader().getParent().getParent()); // 爷爷加载器
 *                      System.out.println(String.class.getClassLoader()); // String类的加载器
 *
 *                  }
 *
 * 由于BootstrapClassLoader是C++编写的 我们在Java中是获取不到的
 *
 * 既然通过ClassLoader就可以加载类 那么我们可以自己手动将class文件加载到JVM中吗? 先写好我们定义的类:
 *                  package com.javase26.test;
 *
 *                  public class Test {
 *
 *                      public String text;
 *
 *                      public void javase26.test(String str) {
 *                          System.out.println(text + " > 我是测试方法" + str);
 *                      }
 *
 *                  }
 *
 * 编译后 得到一个class文件 我们把它放到根目录下 然后编写一个我们自己的ClassLoader
 * 因为普通的ClassLoader无法加载二进制文件 因此我们编写一个自定义的来让它支持:
 *                  static class MyClassLoader extends ClassLoader { // 定义一个自己的ClassLoader
 *                      public Class<?> defineClass(String name, byte[] b) {
 *                          return defineClass(name, b, 0, b.length);
 *                      }
 *                  }
 *                  static void test2() {
 *
 *                      try {
 *                          MyClassLoader classLoader = new MyClassLoader();
 *                          FileInputStream stream = new FileInputStream("Text.class");
 *                          byte[] bytes = new byte[stream.available()];
 *                          stream.read(bytes);
 *                          Class<?> clazz = classLoader.defineClass("javase26.test.Text", bytes); // 类名必须和我们定义的保持一致
 *                          System.out.println(clazz.getName()); // 成功加载外部class文件
 *
 *                          Method test = clazz.getMethod("test", String.class);
 *                          Object o = clazz.newInstance();
 *                          test.invoke(o, " xxxx");
 *                      } catch (IOException | ReflectiveOperationException e) {
 *                          e.printStackTrace();
 *                      }
 *
 *                  }
 *
 * 现在 我们将此class文件读取并解析为Class了 现在我们就可以对此类进行操作了(注意: 我们无法在代码中直接使用此类型 因为它是我们直接加载的)
 * 我们来试试看创建一个此类的对象并调用其方法:
 *                  try {
 *                      Object obj = clazz.newInstance();
 *                      Method method = clazz.getMethod("test", String.class); // 获取我们定义的test(String str)方法
 *                      method.invoke(obj, "哥们这瓜多少钱一斤?");
 *                  } catch (Exception e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 我们来试试看修改成员字段之后 再来调用此方法:
 *                  try {
 *                      Object obj = clazz.newInstance();
 *                      Field field = clazz.getField("text); // 获取成员变量 String text
 *                      field.set(obj, "华强");
 *                      Method method = clazz.getMethod("test", String.clazz); // 获取我们定义的test(String str)方法
 *                      method.invoke(obj, "哥们这瓜多少钱一斤?");
 *                  } catch (Exception e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 通过这种方式 我们就可以实现外部加载甚至是网络加载一个类 只需要把类文件传递即可 这样就无需再将代码写在本地 而是动态进行传递
 * 不仅可以一定程度上防止源代码被反编译(只是一定程度上 想破解你代码有的是方法) 而且在更多情况下 我们还可以对byte[]进行加密 保证在传递过程中的完全性
 */
public class Main {

    static void test1() {

        System.out.println(Main.class.getClassLoader());
        System.out.println(Main.class.getClassLoader().getParent());
        System.out.println(Main.class.getClassLoader().getParent().getParent());
        System.out.println(String.class.getClassLoader());

    }

    static void test2() {

        try {
            MyClassLoader classLoader = new MyClassLoader();
            FileInputStream stream = new FileInputStream("Text.class");
            byte[] bytes = new byte[stream.available()];
            stream.read(bytes);
            Class<?> clazz = classLoader.defineClass("javase26.test.Text", bytes);
            System.out.println(clazz.getName());

            Method test = clazz.getMethod("test", String.class);
            Object o = clazz.newInstance();
            test.invoke(o, " xxxx");
        } catch (IOException | ReflectiveOperationException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //test1();
        test2();
    }

    static class MyClassLoader extends ClassLoader {

        public Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }

    }

}
