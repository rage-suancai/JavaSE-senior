package javase14;

/**
 * 线程锁
 * 通过synchronized关键字来创造一个线程锁 首先我们来认识一下synchronized代码块
 *                  private static int value = 0;
 *                  static void test1() {
 *
 *                      Thread thread1 = new Thread(() -> {
 *                          for (int i = 0; i < 10000; i++) {
 *                              synchronized (Main.class) {
 *                                  value++;
 *                              }
 *                          }
 *                          System.out.println("线程一完成");
 *                      });
 *                      Thread thread2 = new Thread(() ->{
 *                          for (int i = 0; i < 10000; i++) {
 *                              synchronized (Main.class) {
 *                                  value++;
 *                              }
 *                          }
 *                          System.out.println("线程二完成");
 *                      });
 *                      thread1.start();
 *                      thread2.start();
 *                      try {
 *                          Thread.sleep(1000); // 主线程停止1秒 保证两个线程执行完成
 *                          System.out.println(value);
 *                      } catch (InterruptedException e) {
 *                          e.printStackTrace();
 *                      }
 *
 *                  }
 *
 * 我们发现 现在得到的结果就是我们想要的内容了 因为在同步代码块执行过程中 拿到了我们传入对象或类的锁(传入的如果是对象 就是对象锁 不同的对象代表不同的对象锁
 * 如果是类 就是类锁 类锁只有一个 实际上类锁也是对象锁 是Class类实例 但是Class类实例同样的类无论怎么获取都是同一个) 但是注意两个线程必须使用同一个把锁
 *
 * 当一个线程进入到同步代码块时 会获取到当前的锁 而这时如果其他使用同样的锁的同步代码块也想执行内容 就必须等待当前同步代码块的内容执行完毕 在执行完毕后会自动释放这把锁
 * 而其他的线程才能拿到这把锁并开始执行同步代码里面的内容 (实际上synchronized是一种悲观锁 随时都认为有其他线程在对数据进行修改 后面有机会我们还会讲到乐观锁 如CAS算法)
 *
 * 那么我们来看看 如果使用的是不同对象的锁 那么还能顺利进行吗?
 *                  Main main1 = new Main();
 *                  Main main2 = new Main();
 *
 *                  Thread thread1 = new Thread(() -> {
 *                      for (int i = 0; i < 20000; i++) {
 *                          synchronized (main1) {
 *                              value++;
 *                          }
 *                      }
 *                      System.out.println("线程一完成");
 *                  });
 *                  Thread thread2 = new Thread(() -> {
 *                      for (int i = 0; i < 20000; i++) {
 *                          synchronized (main2) {
 *                              value++;
 *                          }
 *                      }
 *                      System.out.println("线程二完成");
 *                  });
 *                  thread1.start();
 *                  thread2.start();
 *                  try {
 *                      Thread.sleep(1000);
 *                      System.out.println(value);
 *                  } catch (InterruptedException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 当对象不同时 获取到的是不同的锁 因此并不能保证自增操作的原子性 最后也得不到想要的结果
 *
 * synchronized关键字也可以用之于方法上 调用此方法时也会获取锁:
 *
 *                  private static synchronized void add() {
 *                      value++;
 *                  }
 *
 *                  Thread thread1 = new Thread(() -> {
 *                      for (int i = 0; i < 10000; i++) add();
 *                          System.out.println("线程一完成");
 *                  });
 *                  Thread thread2 = new Thread(() -> {
 *                      for (int i = 0; i < 10000; i++) add();
 *                          System.out.println("线程二完成");
 *                  });
 *                  thread1.start();
 *                  thread2.start();
 *                  try {
 *                      Thread.sleep(1000);
 *                      System.out.println(value);
 *                  } catch (InterruptedException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 我们发现实际上效果是相同的 只不过这个锁不用你去给 如果是静态方法 就是使用类锁 而如果是普通成员方法
 * 就是使用的对象锁 通过灵活使用synchronized就能很好地解决我们之前提到的问题了
 *
 * 死锁
 * 其实锁的概念就是操作系统中也有提及 它是指两个线程相互持有对方需要的锁 但是又迟迟不释放 导致程序卡住
 *
 * 我们发现 线程A和线程B都需要对方的锁 但是又被对方牢牢把握 由于线程被无限期地阻塞 因此程序不可能正常终止 我们来看看以下这段代码会得到什么结果:
 *                  Object o1 = new Object();
 *                  Object o2 = new Object();
 *
 *                  Thread thread1 = new Thread(() -> {
 *                      synchronized (o1) {
 *                          try {
 *                              Thread.sleep(1000);
 *                              synchronized (o2) {
 *                                  System.out.println("线程一");
 *                              }
 *                          } catch (InterruptedException e) {
 *                              e.printStackTrace();
 *                          }
 *                      }
 *                  });
 *                  Thread thread2 = new Thread(() -> {
 *                      synchronized (o2) {
 *                          try {
 *                              Thread.sleep(1000);
 *                              synchronized (o1) {
 *                                  System.out.println("线程二");
 *                              }
 *                          } catch (InterruptedException e) {
 *                              e.printStackTrace();
 *                          }
 *                      }
 *                  });
 *                  thread1.start();
 *                  thread2.start();
 *
 * 那么我们如何去检查死锁呢 我们可以利用stack命令来检测死锁 首先利用jps找到我们的java进程
 *
 * jstack自动帮助我们找到了一个死锁 并打印出了相关线程的栈追踪信息
 *
 * 不推荐使用suspend()去挂起线程的原因 是因为suspend()在使线程暂停的同时 并不会释放任何锁资源 其他线程都无法服务被它占用的锁
 * 直到对应的线程执行resume()方法后 被挂起的线程才能继续 从而其他被阻塞在这个锁的线程才可以继续执行 但是
 * 如果resume()操作出现在suspend()之前执行 那么线程将一直处于挂起状态 同时一直占用锁 这就产生了死锁
 */
public class Main {

    private static int value = 0;

    static void test1() {

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                synchronized (Main.class) {
                    value++;
                }
            }
            System.out.println("线程一完成");
        });
        Thread thread2 = new Thread(() ->{
            for (int i = 0; i < 10000; i++) {
                synchronized (Main.class) {
                    value++;
                }
            }
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

    static void test2() {

        Main main1 = new Main();
        Main main2 = new Main();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                synchronized (main1) {
                    value++;
                }
            }
            System.out.println("线程一完成");
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                synchronized (main2) {
                    value++;
                }
            }
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

    private static synchronized void add() {
        value++;
    }
    static void test3() {

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) add();
                System.out.println("线程一完成");
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) add();
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

    static void test4() {

        Object o1 = new Object();
        Object o2 = new Object();

        Thread thread1 = new Thread(() -> {
            synchronized (o1) {
                try {
                    Thread.sleep(1000);
                    synchronized (o2) {
                        System.out.println("线程一");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (o2) {
                try {
                    Thread.sleep(1000);
                    synchronized (o1) {
                        System.out.println("线程二");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();
        thread2.start();

    }

    public static void main(String[] args) {
        test1();
        //test2();
        //test3();
        //test4();
    }

}
