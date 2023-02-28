package javase22;

import java.lang.reflect.Type;

/**
 * Class对象与多态
 * 正常情况下 我们使用Instanceof进行类型比较:
 *                  String str = "";
 *                  System.out.println(str instanceof String);
 *
 * 它可以判断一个对象是否为此接口或是类的实现或是子类 而现在我们有了更多的方式去判断类型:
 *                  String str = "";
 *                  System.out.println(str.getClass() == String.class); // 直接判断是否为这个类型
 *
 * 如果需要判断是否为子类或是接口/抽象类的实现 我们可以使用asSubClass()方法:
 *                  Integer i = 10;
 *                  i.getClass().asSubclass(Number.class); // 当Integer不是Number的子类时 会产生异常
 *
 * 通过getSuperclass()方法 我们可以获取到父类的Class对象:
 *                  Integer i = 10;
 *                  System.out.println(i.getClass().getSuperclass());
 *
 * 也可以通过getGenericSuperclass()获取父类的原始类型的Type:
 *                  Integer i = 10;
 *                  Type type = i.getClass().getGenericSuperclass();
 *                  System.out.println(type);
 *                  System.out.println(type instanceof Class);
 *
 * 我们发现Type实际上是Class类的父接口 但是获取到的Type的实现并不一定是Class
 *
 * 同理 我们也可以像上面这样获取父接口:
 *                  Integer i = 10;
 *                  for (Class<?> anInterface : i.getClass().getInterfaces()) {
 *                      System.out.println(anInterface.getName());
 *                  }
 *                  for (Type genericInterface : i.getClass().getGenericInterfaces()) {
 *                      System.out.println(genericInterface.getTypeName());
 *                  }
 */
public class Main {

    static void test1() {

        /*String str = "";
        System.out.println(str instanceof String);
        System.out.println(str.getClass() == String.class);*/

        Integer i = 10;
        i.getClass().asSubclass(Number.class);
        System.out.println(i.getClass().getSuperclass());

    }

    static void test2() {

        /*Class<?>[] clazz = ArrayList.class.getInterfaces();
        for (Class<?> aClass : clazz) {
            System.out.println(aClass);
        }*/

        /*Integer i = 10;
        Type type = i.getClass().getGenericSuperclass();
        System.out.println(type);
        System.out.println(type instanceof Class);*/

        /*ParameterizedType type = (ParameterizedType) ArrayList.class.getGenericSuperclass();
        Type[] types = type.getActualTypeArguments();
        for (Type type1 : types) {
            System.out.println(type1);
        }*/

        Integer i = 10;
        for (Class<?> anInterface : i.getClass().getInterfaces()) {
            System.out.println(anInterface.getName());
        }
        for (Type genericInterface : i.getClass().getGenericInterfaces()) {
            System.out.println(genericInterface.getTypeName());
        }

    }

    public static void main(String[] args) {
        //test1();
        test2();
    }

}
