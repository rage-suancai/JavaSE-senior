package javase21;

/**
 * Class类详解
 * 通过前面 我们了解了类的加载 同时会提取一个类的信息生产Class对象存放在内存中 反射机制其实就是利用这些存放的类信息
 * 来获取类的信息和操作类 那么如何获取到每个类对应的Class对象呢 我们可以通过以下方式:
 *                 Class<Student> clazz = Student.class; // 使用class关键字 通过类名获取
 *                 Class<?> clazz2 = Class.forName("java.lang.Student"); // 使用Class类静态方法forName() 通过包名.类名获取 注意: 返回值是Class<?>
 *                 Class<?> clazz3 = new Student("cpdd").getClass(); // 通过实例对象获取
 *
 * 注意: Class类也是员工泛型类 只有第一种方法 能够直接获取到对应类型的Class对象 而以下两种方法使用了?通配符作为返回值 但是实际上都和第一个返回的是同一个对象:
 *                 Class<Student> clazz = Student.class;
 *                 Class<?> clazz2 = Class.forName("java.lang.Student");
 *                 Class<?> clazz3 = new Student("cpdd").getClass();
 *
 *                 System.out.println(clazz == clazz2);
 *                 System.out.println(clazz == clazz3);
 *
 * 通过比较 验证了我们一开始的结论 在JVM中每个类始终只存在一个Class对象 无论通过上面方法获取 都是一样的 现在我们再来看看这个问题:
 *                 Class<?> clazz = int.class; // 基本数据类型有Class对象吗?
 *                 System.out.println(clazz);
 *
 * 迷了 不是每个类才有Class对象吗 基本数据类型又不是类 这也行吗? 实际上基本数据类型也有对应的Class对象
 * (反射操作可能需要用到) 而且我们不仅仅可以通过class关键字获取 其实本质是是定义在对应的包装类中的:
 *                  @SuppressWarnings("unchecked")
 *                  public static final Class<Integer>  TYPE = (Class<Integer>) Class.getPrimitiveClass("int");
 *
 *                  static native Class<?> getPrimitiveClass(String name); // C++实现 并非Java定义
 *
 * 每个包装类中(包括Void) 都有一个获取原始类型Class方法 注意: getPrimitiveClass获取的是原始类型 并不是包装类型 只是可以使用包装类来表示
 *                  Class<?> clazz = int.class;
 *                  System.out.println(Integer.TYPE == int.class);
 *
 * 通过对比 我们发现实际上包装类型都有一个TYPE 其实也就是基本类型的Class 那么包装类的Class和基本类的
 *
 * 我们发现 包装类型的对象并不是基本类型Class 数组类型也是一种类型 只是编程不可见 因此我们可以直接获取数组的Class对象:
 *                  Class<String[]> clazz = String[].class;
 *                  System.out.println(clazz.getName()); // 获取类名称(得到的是包名+类名的完整名称)
 *                  System.out.println(clazz.getSimpleName());
 *                  System.out.println(clazz.getTypeName());
 *                  System.out.println(clazz.getClassLoader()); // 获取它的类加载器
 *                  System.out.println(clazz.cast(new Integer("10"))); //
 *
 * 马上 我们将开始对Class对象的使用进行学习
 */
public class Main {

    static void test1() {

        try {
            Class<?> clazz1 = Class.forName("javase21.Student");
            Class<?> clazz2 = new Student().getClass();
            Class<Student> clazz3 = Student.class;

            System.out.println(clazz1 == clazz2);
            System.out.println(clazz1 == clazz3);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    static void test2() {

        /*Class<?> clazz = Student.class;
        System.out.println(clazz.getName());*/

        /*Class<?> clazz = int.class;
        System.out.println(clazz);*/

        /*Class<?> clazz = int.class;
        System.out.println(Integer.TYPE == int.class);*/

        System.out.println(Integer.TYPE == Integer.class);

    }

    static void test3() {

        Class<String[]> clazz = String[].class;
        System.out.println(clazz.getName());
        System.out.println(clazz.getSimpleName());
        System.out.println(clazz.getTypeName());
        System.out.println(clazz.getClassLoader());
        System.out.println(clazz.cast(new Integer("10")));

    }

    public static void main(String[] args) {
        //test1();
        //test2();
        test3();

    }

}
