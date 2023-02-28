package javase13;

/**
 * 线程锁和线程同步
 * 在开始讲解线程同步之前 我们需要先了解一下多线程情况下java的内存管理
 *
 * 线程之间的共享变量(比如之前悬念中value变量)存储主内存(main memory)中 每个线程都有一个私有的工作内存(本地内存)
 * 工作内存中存储了该线程以读/写共享变量的副本 它类似于我们在计算机组成原理中学习的多处理器高速缓存机制:
 *
 * 高速缓存通过保存内存中数据的副本来提供更加快速的数据访问 但是如果多个处理器的运算任务都涉及同一块内存区域
 * 就可能导致各自的高速缓存数据不一致 在写回主内存时就会发生冲突 这就是引入高速缓存引发的新问题 称之为缓存一致性
 *
 * 实际上 java的内存模型也是这样类似设计的 当我们同时去操作一个共享变量时 如果仅仅是读取还好 但是如果同时写入内容 就会出现问题
 * 好比说一个银行 如果我和我的朋友同时在银行取我账户里面的钱 难道取1000还可能吐2000出来吗 我们需要一种更加安全的机制来维持秩序 保证数据的安全性
 *
 * 悬念破案
 * 我们在来回顾一下之前留给大家的悬念:
 *                  private static int value = 0;
 *                  static void test1() {
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
 *                      try {
 *                          Thread.sleep(1000);
 *                          System.out.println(value);
 *                      } catch (InterruptedException e) {
 *                          e.printStackTrace();
 *                      }
 *
 *                  }
 *
 * 实际上 当两个线程同时读取value的时候 可能会同时拿到同样的值 而进行自增操作之后 也是同样的值 再写回主内存后 本来应该进行2次自增操作 实际上只执行了一次
 *
 * 那么要解决这样的问题 我们就必须采取某种同步机制 来限制不同线程对于共享变量的访问 我们希望的是保证共享变量value自增操作的原子性
 * (原子性是指一个操作或多个操作要么全部执行 且执行的过程不会被任何因素打断 包括其他线程 要么就都不执行)
 */
public class Main {

    private static int value = 0;
    static void test1() {

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
        try {
            Thread.sleep(1000);
            System.out.println(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        test1();
    }

}
