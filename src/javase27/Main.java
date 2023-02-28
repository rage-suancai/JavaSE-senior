package javase27;

/**
 * 注解
 * 注意: 注解跟我们之前讲解的注释完全不是一个概念 不要搞混了
 *
 * 其实我们在之前就接触到注解了 比如@Override 表示重写父类方法(当然不加效果也是一样的 此注解在编译时会被自动丢弃) 注解本质上也是一个类型 只不过它的用法比较特殊
 *
 * 注解可以被标注在任意地方 包括方法上 类名上 参数上 成员属性上 注解定义上等 就像注释一样 它相当于我们对于某样东西的一个标记
 * 而与注释不同的是 注解可以通过反射在运行时获取 注解也可以选择是否保留到运行时
 *
 * 预设注解
 * JDK预设了以下注解 作用于代码:
 *
 *      > @Override - 检查(仅仅是检查 不保留到运行时) 该方法是是否是重写方法 如果发现其父类 或者是引用的接口中并没有该方法时 会报编译错误
 *      > @Deprecated - 标记过时方法 如果使用该方法 会报编译警告
 *      > @SuppressWarnings - 指示编译器去忽略注解中声明的警告(仅仅编译阶段 不保留到运行时)
 *      > @FunctionalInterface - Java8开始支持 标识一个匿名函数或函数式接口
 *      > @SafeVarargs - Java7开始支持 忽略任何使用参数为泛型变量的方法或构造函数调用产生的警告
 *
 * 元注解
 * 元注解是作用于注解上的注解 用于我们编写自定义的注解:
 *
 *      > @Retention - 标识这个注解这么保存 是只在代码中 还是编入class文件中 或者是在运行时可以通过反射访问
 *      > @Documented - 标记这些注解是否包含在用户文档中
 *      > @Target - 标记这个注解应该是哪种Java成员
 *      > @Inherited - 标记这个注解是继承于哪个注解类(默认 注解并没有继续于任何子类)
 *      > @Repeatable - Java8开发支持 标识某注解可以在同一个声明上使用多次
 *
 * 看了这么多预设的注解 你们肯定眼花缭乱了 那我们来看看@Overribe是如何定义的:
 *
 *                  @Target(ElementType.METHOD)
 *                  @Retention()
 *                  public @interface Override {
 *
 *                  }
 *
 * 该注解由@Target限定为只能作用于方法上 ElementType是一个枚举类型 用于表示此枚举的作作用域 一个注解可以由很多个作用域 @Retention表示此注解的保留策略
 * 包括三种策略 在上述中有写到 而这里定义为只在代码中 一般情况下 自定义的注解需要定义一个@Retention和1-n个@Target
 *
 * 既然了解了元注解的使用和注解的定义方式 我们就来尝试定义一个自己的注解:
 *
 *                  @Target(ElementType.METHOD)
 *                  @Retention(RetentionPolicy.RUNTIME)
 *                  public @interface Test {
 *
 *                  }
 *
 * 这里定义一个Test注解 并将其保留到运行时 同时此注解可以作用于方法或是类上:
 *
 *                  @Test
 *                  public class Main {
 *
 *                      @Test
 *                      public static void main(String[] args) {
 *
 *                      }
 *
 *                  }
 *
 * 这样 一个最简单的注解就被我们创建了
 */
//@Test
public class Main {

    /*@Deprecated
    static void test1() {

        System.out.println("已弃用");

    }*/

    @Test
    public static void main(String[] args) {

    }

}
