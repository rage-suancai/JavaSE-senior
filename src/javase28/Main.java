package javase28;

import javax.swing.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 注解的使用
 * 我们还可以在注解中定义一些属性 注解的属性也叫做成员变量 注解只有成员变量 没有方法 注解的成员变量在注解的定义中以"无形参的方法"形式来声明
 * 其方法定义了该成员变量的名字 其返回值定义了该成员变量的类型:
 *
 *                  @Target({ElementType.METHOD, ElementType.TYPE})
 *                  @Retention(RetentionPolicy.RUNTIME)
 *                  public @interface Test{
 *
 *                      String name();
 *
 *                  }
 *
 *                  public class Main {
 *
 *                      @Test(name = "Hello")
 *                      public static void main(String[] args) {
 *
 *                      }
 *
 *                  }
 *
 * 默认只有一个属性时 我们可以将其名字设定为value 否则 我们需要在使用时手动指定注解的属性名称 使用value则无需填入:
 *
 *                  @Target({ElementType.METHOD, ElementType.TYPE})
 *                  @Retention(RetentionPolicy.RUNTIME)
 *                  public @interface Test {
 *
 *                      String value();
 *
 *                  }
 *
 *                  public class Main {
 *
 *                      @Test("Hello")
 *                      public static void main() {
 *
 *                      }
 *
 *                  }
 *
 * 我们也可以使用default关键字来为这些属性指定默认值:
 *
 *                  @Target({ElemenType.METHOD, ElementType.TYPE})
 *                  @Retention(RetentionPolicy.RUNTIME)
 *                  public @interface Test {
 *
 *                      String value() default "都看到这里了 很棒棒哦";
 *
 *                  }
 *
 * 当属性存在默认值时 使用注解的时候可以不用传入属性值 当属性为数组时呢?
 *
 *                  @Target({ElemenType.METHOD}, ElementType.TYPE)
 *                  @Retention(RetentionPolicy.RUNTIME)
 *                  public @interface Test {
 *
 *                      String[] value();
 *
 *                  }
 *
 * 当属性为数组 我们在使用注解传参时 如果数组里面有一个内容 我们可以直接传入一个值 而不是创建一个数组:
 *
 *                  @Test("value")
 *                  public static void main(String[] args) {
 *
 *                  }
 *
 *                  public class Main {
 *
 *                      @Test({"value1", "value2"}) // 多个值时就使用花括号括起来
 *                      public static void main() {
 *
 *                      }
 *
 *                  }
 *
 * 反射获取注解
 * 既然我们的注解可以保留到运行时 那么我们来看看 如何获取我们编写的注解 我们需要用到反射机制:
 *
 *                  public static void main() {
 *
 *                      Class<Student> clazz = Student.class;
 *                      for (Annotation annotation : clazz.getAnnotations()) {
 *                          System.out.println(annotation.annotationType()); // 获取类型
 *                          System.out.println(annotation.instanceof Test); // 直接判断是否为Test
 *                          Test test = (Test) annotation;
 *                          System.out.println(test.value()); // 获取我们在注解中写入的内容
 *                      }
 *
 *                  }
 *
 * 通过反射机制 我们可以快速获取到我们标记的注解 同时还能获取到注解中填入的值 那么我们来看看 方法上的标记是不是也可以通这种反射获取注解:
 *
 *                  public static void main() {
 *
 *                      Class<Student> clazz = Student.class;
 *                      for (Annotation annotation : clazz.getMethod("test").getAnnotations()) {
 *                          System.out.println(annotation.annotationType()); // 获取类型
 *                          System.out.println(annotation instanceof Test); // 直接判断是否为Test
 *                          Test test = (Test) annotation;
 *                          System.out.println(test.value()); // 获取我们在注解中写入的内容
 *                      }
 *
 *                  }
 *
 * 无论是方法 类 还是字段 都是可以使用getAnnotations()方法 (还有几个同名的)来快速获取我们标记的注解
 *
 * 所以说呢 这玩意学来有啥用? 丝毫get不到这玩意的用处 其实不是 现阶段作为初学者 还体会不到注解带来的快乐
 * 在接触到Spring和SpringBoot等大型框架后 相信各位就能感受到注解带来的魅力了
 */
@Test("Fuck World")
public class Main {

    /*@Test()
    static void test1() {

    }*/

    /*@Test({"value1", "value2"})
    static void test2() {

    }*/

    @Test("Fuck World")
    static void test3() {

    }

    public static void main(String[] args) throws NoSuchMethodException {

        //test1();

        Class<Main> mainClass = Main.class;

        /*Test annotation1 = mainClass.getAnnotation(Test.class);
        System.out.println(annotation1.value());*/

        /*Method method = mainClass.getDeclaredMethod("test3");
        Test annotation2 = method.getAnnotation(Test.class);
        System.out.println(annotation2.value());*/

        /*for (Annotation annotation : mainClass.getAnnotations()) {
            System.out.println(annotation.annotationType());
            System.out.println(annotation instanceof Test);
            Test test = (Test) annotation;
            System.out.println(test.value());
        }*/

        for (Annotation annotation : mainClass.getDeclaredMethod("test3").getAnnotations()) {
            System.out.println(annotation.annotationType());
            System.out.println(annotation instanceof Test);
            Test test = (Test) annotation;
            System.out.println(test.value());
        }

    }

}
