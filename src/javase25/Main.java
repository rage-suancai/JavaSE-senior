package javase25;

import javase25.entity.Student;
import sun.security.jca.GetInstance;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * 反射修改类的属性
 * 我们还可以通过反射访问一个类中定义的成员字段也可以修改一个类的对象中的成员字段值 通过getField()方法来获取一个类定义的指定字段:
 *                  try {
 *                      Class<?> clazz = Class.forName("javase25.entity.Student");
 *                      Object instance = clazz.newInstance();
 *                      Field field = clazz.getField("i"); // 获取类的成员字段i
 *                      field.set(instance, 100); // 将类实例instance的成员字段i设置为100
 *                      Method method = clazz.getMethod("javase26.test")
 *                      method.invoke(instance);
 *                  } catch (ReflectiveOperationException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 在得到Field之后 我们就可以直接通过set()方法为某个对象 设定此属性的值 比如上面
 * 我们就为instance对象设定值为100 当访问private字段时 同样可以按照上面的操作进行越来越权访问:
 *                  try {
 *                      Class<?> clazz = Class.forName("javase25.entity.Student");
 *                      Object instance = clazz.newInstance();
 *                      Field field = clazz.getDeclaredField("i"); // 获取类的成员字段i
 *                      field.setAccessible(true);
 *                      field.set(instance, 100); // 将类实例instance的成员字段i设置为100
 *                      Method method = clazz.getMethod("javase26.test");
 *                      method.invoke(instance);
 *                  } catch (ReflectiveOperationException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 现在我们已经知道 反射几乎可以把一个类的老底都给扒出来 如何属性 任何内容 都可以被反射修改 无论权限修饰符是什么
 * 那么 如果我们的字段被标记为final呢? 现在在字段i前面添加final关键字 我再来看看效果:
 *                  private final int i = 10;
 *
 * 这时 当字段为final时 就修改失败了 当然 通过反射可以直接将final修饰符直接去除 去除后 就可以所随意修改内容了 我们来尝试修改integer的value值:
 *                  try {
 *                      Integer i = 10;
 *                      Field field = Integer.class.getDeclaredField("value");
 *                      Field modifiers = Field.class.getDeclaredField("modifiers"); // 这里要获取Field类的modifiers字段进行修改
 *                      modifiers.setAccessible(true);
 *                      modifiers.setInt(field,field.getModifiers() &~ Modifier.FINAL); // 去除final标记
 *                      field.setAccessible(true);
 *                      field.set(i, 100); // 强行设置值
 *                      System.out.println(i);
 *                  } catch (ReflectiveOperationException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 我们可以发现 反射非常暴力 就连被定义为final字段的值都能强行修改 几乎能够无视一切阻拦 我们来试试看修改一些其他的类型:
 *                  try {
 *                      ArrayList<String> i = new ArrayList<>();
 *                      Field field = ArrayList.class.getDeclaredField("size");
 *                      field.setAccessible(true);
 *                      field.set(i, 10);
 *                      i.add("测试"); // 只添加一个元素
 *                      System.out.println(i.size()); // 大小直接变成11
 *                      i.remove(10); // 瞎移除都不带报错的 淦
 *                  } catch (ReflectiveOperationException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 实际上 整个ArrayList体系由于我们的反射操作 导致被破坏 因此它已经无法正常工作了
 *
 * 再次强调 在进行反射操作时 必须注意是否安全 虽然拥有了创始主的能力 但是我们不能滥用 我们只能把它当做一个不得已才去使用的工具
 */
public class Main {

    static void test1() {

        /*try {
            Class<?> clazz = Class.forName("javase25.entity.Student");
            //Field name = clazz.getDeclaredField("name");
            Field age = clazz.getDeclaredField("age");
            age.setAccessible(true);
            System.out.println(age.get(new Student("马牛逼", 20)));
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }*/

        try {
            Integer i = new Integer(88);
            Field value = Integer.class.getDeclaredField("value");
            value.setAccessible(true);
            //System.out.println(value.get(i));
            value.set(i, 99);
            System.out.println(i);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

    }

    static void test2() {

        /*try {
            Student student = new Student("马牛逼", 20);
            Field age = Student.class.getDeclaredField("age");
            age.setAccessible(true);
            age.set(student, 30);
            student.javase26.test();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }*/

        try {
            Integer i = 10;
            Field field = Integer.class.getDeclaredField("value");
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() &~ Modifier.FINAL);
            field.setAccessible(true);
            field.set(i, 100);
            System.out.println(i);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

    }

    static void test3() {

        try {
            ArrayList<String> i = new ArrayList<>();
            Field field = ArrayList.class.getDeclaredField("size");
            field.setAccessible(true);
            field.set(i, 10);
            i.add("测试");
            System.out.println(i.size());
            i.remove(10);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //test1();
        //test2();
        test3();
    }

}
