package javase10;

/**
 * 线程的创建和启动
 * 通过创建Thread对象来创建一个新的线程 Thread构造方法中需要传入一个Runnable接口的实现
 * (其实就是编写要在另一个线程执行的内容逻辑) 同时Runnable只要一个未实现方法 因此可直接使用lambda表达式:
 *                  @FunctionlInterface
 *                  public interface Runnable {
 *                      public abstract void run();
 *                  }
 *
 * 创建好后 通过调用start()方法来运行此线程:
 *                  Thread thread = new Thread(() -> { // 直接编写逻辑
 *                      System.out.println("我是另一个线程");
 *                  });
 *                  thread.start(); // 调用此方法来开始执行线程
 *
 * 可能上面的例子看起来和普通的单线程没两样 那我们先来看看下面这段代码的运行结果:
 *                  Thread thread = new Thread(() -> {
 *                      System.out.println("我是线程: " + Thread.currentThread().getName());
 *                      System.out.println("我正在计算 0-10000 之间所有数的和....");
 *                      int sum = 0;
 *                      for (int i = 0; i < 10000; i++) {
 *                          sum += i;
 *                      }
 *                      System.out.println("结果是: " + sum);
 *                  });
 *                  thread.start();
 *                  System.out.println("我是主线程");
 *
 * 我们发现 这段代码执行输出结果并不是按照从上往下的顺序了 因为他们分别位于两个线程 他们是同时进行的 如果你还是觉得很疑惑我们接着来看下面的代码运行结果:
 *                  Thread thread1 = new Thread(() -> {
 *                      for (int i = 0; i < 50; i++) {
 *                          System.out.println("我是一号线程: " + i);
 *                      }
 *                  });
 *                  Thread thread2 = new Thread(() ->{
 *                      for (int i = 0; i < 50; i++) {
 *                          System.out.println("我是二号线程: " + i);
 *                      }
 *                  });
 *                  thread1.start();
 *                  thread2.start();
 *
 * 我们可以看到打印实际上是在交替进行的 也证明了它们是在同时运行
 *
 * 注意: 我们发现还有一个run方法 也能执行线程里面定义的内容 但是run是直接在当前线程执行 并不是创建一个线程执行
 *
 * 实际上 线程和进程差不多 也会等待获取CPU资源 一旦获取到 就开始按顺序执行我们给定的程序 当需要等待外部IO操作
 * (比如Scanner获取输入的文本) 就会暂时处于休眠状态 等待通知 或是调用sleep()方法来让当前线程休眠一段时间:
 *                  System.out.print("Y");
 *                  Thread.sleep(2000); // 休眠时间 以毫秒为单位 1000ms = 1s
 *                  System.out.print("X");
 *                  Thread.sleep(2000);
 *                  System.out.print("S");
 *                  Thread.sleep(2000);
 *                  System.out.println("NB");
 *
 * 我们也可以使用stop()方法来强行终止此线程:
 *                  Thread thread = new Thread(() -> {
 *                      Thread me = Thread.currentThread(); // 获取当前线程对象
 *                      for (int i = 0; i < 50; i++) {
 *                          System.out.println("打印: " + i);
 *                          if (i == 20) me.stop(); // 此方法会直接终止此线程
 *                      }
 *                  });
 *                  thread.start();
 *
 * 虽然stop()方法能够终止此线程 但是并不是所推荐的做法 有关线程中断相关问题 我们会在后面继续了解
 *
 * 思考: 猜猜以下程序输出结果:
 *                  private static int value = 0;
 *                  static void test5() throws InterruptedException {
 *
 *                      Thread thread1 = new Thread(() -> {
 *                          for (int i = 0; i < 10000; i++) value++;
 *                          System.out.println("线程一完成");
 *                      });
 *                      Thread thread2 = new Thread(() -> {
 *                          for (int i = 0; i < 10000; i++) value++;
 *                          System.out.println("线程二完成");
 *                      });
 *                      thread1.start();
 *                      thread2.start();
 *                      Thread.sleep(1000); // 主线程停止1秒 保证两个线程执行完成
 *                      System.out.println(value);
 *
 *                  }
 */
public class Main {

    static void test1(){

        Thread thread = new Thread(() -> {
            System.out.println("我是另一个线程: " + Thread.currentThread().getName());
        });
        thread.start();

    }

    static void test2() {

        Thread thread = new Thread(() -> {
            System.out.println("我是线程: " + Thread.currentThread().getName());
            System.out.println("我正在计算 0-10000 之间所有数的和....");
            int sum = 0;
            for (int i = 0; i < 10000; i++) {
                sum += i;
            }
            System.out.println("结果是: " + sum);
        });
        thread.start();
        System.out.println("我是主线程");

    }

    static void test3() {

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 200; i++) {
                System.out.println("我是一号线程: " + i);
            }
        });
        Thread thread2 = new Thread(() ->{
            for (int i = 0; i < 200; i++) {
                System.out.println("我是二号线程: " + i);
            }
        });
        thread1.start();
        thread2.start();

    }

    static void test4() throws InterruptedException {

        /*System.out.print("Y");
        Thread.sleep(1000);
        System.out.print("X");
        Thread.sleep(1000);
        System.out.print("S");
        Thread.sleep(1000);
        System.out.println("NB");*/

        Thread thread = new Thread(() -> {
            Thread me = Thread.currentThread();
            for (int i = 0; i < 50; i++) {
                System.out.println("打印: " + i);
                if (i == 20) me.stop();
            }
        });
        thread.start();

    }

    private static int value = 0;
    static void test5() throws InterruptedException {

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) value++;
            System.out.println("线程一完成");
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) value++;
            System.out.println("线程二完成");
        });
        thread1.start();
        thread2.start();
        Thread.sleep(1000);
        System.out.println(value);

    }

    public static void main(String[] args) {

        test1();
        //test2();
        //test3();
        /*try {
            //test4();
            test5();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    }

}
