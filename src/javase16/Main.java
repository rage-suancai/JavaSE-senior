package javase16;

/**
 * ThreadLocal的使用
 * 既然每个线程都有一个自己的工作内存 那么能否只在自己的工作内存中创建变量仅供线程自己使用呢?
 *
 * 我们可以是ThreadLocal类 来创建工作内存中的变量 它将我们的变量值存储在内部(只能存储一个变量)
 * 不同的变量访问到ThreadLocal对象时 都只能获取到自己线程所属的变量
 *                  ThreadLocal<String> local = new ThreadLocal<>(); // 注意: 这是一个泛型类 存储类型为我们要存放的变量类型
 *
 *                  Thread thread1 = new Thread(() -> {
 *                      local.set("yxsnb"); // 将变量的值给予ThreadLocal
 *                      System.out.println("变量值已设定");
 *                      System.out.println(local.get()); // 尝试获取ThreadLocal中存放的变量
 *                  });
 *                  Thread thread2 = new Thread(() -> {
 *                      System.out.println(local.get()); // 尝试获取ThreadLocal中存放的变量
 *                  });
 *                  try {
 *                      thread1.start();
 *                      Thread.sleep(3000); // 间隔三秒
 *                      thread2.start();
 *                  } catch (InterruptedException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 上面的例子中 我们开启两个线程分别去访问ThreadLocal对象 我们发现 第一个线程存放的内容 第一个线程可以获取
 * 但是第二个线程无法获取 我们再来看看第一个线程存入后 第二个线程也存放 是否会覆盖第一个线程存放的内容:
 *                  Thread thread1 = new Thread(() -> {
 *                      local.set("yxsnb");
 *                      System.out.println("线程一变量值已设定");
 *                      try {
 *                          Thread.sleep(2000);
 *                      } catch (InterruptedException e) {
 *                          e.printStackTrace();
 *                      }
 *                      System.out.println("线程一读取变量值");
 *                      System.out.println(local.get()); // 尝试获取ThreadLocal中存放的变量
 *                  });
 *                  Thread thread2 = new Thread(() -> {
 *                      local.set("yyds"); // 将变量的值给予ThreadLocal
 *                      System.out.println("线程二变量值已经设定");
 *                      System.out.println("线程二读取变量值"); // 尝试获取ThreadLocal中存放的变量
 *                      System.out.println(local.get());
 *                  });
 *                  try {
 *                      thread1.start();
 *                      Thread.sleep(1000);
 *                      thread2.start();
 *                  } catch (InterruptedException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 我们发现 即使线程而重新设定了值 也没有影响到线程一存放的值 所以说 不同线程向ThreadLocal存放数据
 * 只会存放在线程自己的工作空间中 而不会直接存放到主内存中 因此各个线程直接存放的内容互不干扰
 *
 * 我们发现在线程中创建的子线程 无法获得父线程工作内存中的变量:
 *                  ThreadLocal<String> local = new ThreadLocal<>();
 *
 *                  Thread thread = new Thread(() -> {
 *                      local.set("yxsnb");
 *                      new Thread(() -> {
 *                          System.out.println(local.get());
 *                      }).start();
 *                  });
 *                  thread.start();
 *
 * 我们可以使用InheritableThreadLocal来解决:
 *                  ThreadLocal<String> local = new InheritableThreadLocal<>();
 *
 *                  Thread thread = new Thread(() -> {
 *                      local.set("yxsnb");
 *                      new Thread(() -> {
 *                          System.out.println(local.get());
 *                      }).start();
 *                  });
 *                  thread.start();
 *
 * 在InheritableThreadLocal存放的内容 会自动向子线程传递
 */
public class Main {

    static void test1() {

        ThreadLocal<String> local = new ThreadLocal<>();

        /*Thread thread1 = new Thread(() -> {
            local.set("yxsnb");
            System.out.println("变量值已设定");
            System.out.println(local.get());
        });
        Thread thread2 = new Thread(() -> {
            System.out.println(local.get());
        });
        try {
            thread1.start();
            Thread.sleep(3000);
            thread2.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        Thread thread1 = new Thread(() -> {
            local.set("yxsnb");
            System.out.println("线程一变量值已设定");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程一读取变量值");
            System.out.println(local.get());
        });
        Thread thread2 = new Thread(() -> {
            local.set("yyds");
            System.out.println("线程二变量值已经设定");
            System.out.println("线程二读取变量值");
            System.out.println(local.get());
        });
        try {
            thread1.start();
            Thread.sleep(1000);
            thread2.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    static void test2() {

        ThreadLocal<String> local1 = new ThreadLocal<>();
        ThreadLocal<String> local2 = new InheritableThreadLocal<>();

        Thread thread = new Thread(() -> {
            local2.set("yxsnb");
            new Thread(() -> {
                System.out.println(local2.get());
            }).start();
        });
        thread.start();

    }

    public static void main(String[] args) {
        //test1();
        test2();
    }

}
