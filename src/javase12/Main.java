package javase12;

/**
 * 线程的优先级
 * 实际上 java程序中的每个线程并不是平均分配CPU时间的 为了使得线程资源分配更加合理 java采用的是抢占式调度方式 优先级越高的线程
 * 优先使用CPU资源 我们希望CPU花费更多时间去处理更重要的任务 而不太重要的任务 则可以先让出一部分资源 线程的优先级一般分为以下三种:
 *      > MIN_PRIORITY 最低优先级
 *      > MAX_PRIORITY 最高优先级
 *      > NOM_PRIORITY 常规优先级
 *                  Thread thread1 = new Thread(() -> {
 *                      System.out.println("线程开始运行");
 *                  });
 *                  thread1.start();
 *                  thread1.setPriority(Thread.MIN_PRIORITY); // 通过使用setPriority方法来设定优先级
 *
 * 优先级越高的线程 获得CPU资源的概率会越大 并不是说一定优先级越高的线程先执行
 *
 * 线程的礼让和加入
 * 我们还可以在当前线程的工作不重要时 将CPU资源让位给其他线程 通过使用yield()方法来将当前资源让位给其他同优先级线程:
 *                  Thread thread1 = new Thread(() -> {
 *                      System.out.println("线程一开始运行");
 *                      for (int i = 0; i < 50; i++) {
 *                          if (i % 5 == 0) {
 *                              System.out.println("让位");
 *                              Thread.yield();
 *                          }
 *                          System.out.println("一打印: " + i);
 *                      }
 *                      System.out.println("线程一结束");
 *                  });
 *                  Thread thread2 = new Thread(() -> {
 *                      System.out.println("线程二开始运行");
 *                      for (int i = 0; i < 50; i++) {
 *                          System.out.println("二打印: " + i);
 *                      }
 *                  });
 *                  thread1.start();
 *                  thread2.start();
 *
 * 观察结果 我们发现 在让位之后 尽可能多的在执行线程二的内容
 *
 * 当我们希望一个线程等待另一个线程执行完成后再继续进行 我们可以使用join()方法来实现线程的加入:
 *                  Thread thread1 = new Thread(() -> {
 *                      System.out.println("线程一开始运行");
 *                      for (int i = 0; i < 50; i++) {
 *                          System.out.println("一打印: " + i);
 *                      }
 *                      System.out.println("线程一结束");
 *                  });
 *                  Thread thread2 = new Thread(() -> {
 *                      System.out.println("线程二开始运行");
 *                      for (int i = 0; i < 50; i++) {
 *                          System.out.println("二打印: " + i);
 *                          if (i == 10) {
 *                              try {
 *                                  System.out.println("线程一加入到此线程");
 *                                  thread1.join(); // 在i==10时 让线程一加入 先完成线程一的内容 在继续当前内容
 *                              } catch (InterruptedException e) {
 *                                  e.printStackTrace();
 *                              }
 *                          }
 *                      }
 *                      System.out.println("线程二结束");
 *                  });
 *                  thread1.start();
 *                  thread2.start();
 *
 * 我们发现 线程一加入后 线程二等待线程一执行的内容全部完成之后 再继续执行线程二的内容
 * 注意: 线程的加入只是等待另一个线程的完成 并不是将另一个线程和当前线程合并 我们来来看看:
 *                  Thread thread1 = new Thread(() -> {
 *                      System.out.println(Thread.currentThread().getName() + "开始运行");
 *                      for (int i = 0; i < 50; i++) {
 *                          System.out.println(Thread.currentThread().getName() + "打印: " + i);
 *                      }
 *                      System.out.println(Thread.currentThread().getName() + "线程结束");
 *                  });
 *                  Thread thread2 = new Thread(() -> {
 *                      System.out.println(Thread.currentThread().getName() + "开始运行");
 *                      for (int i = 0; i < 50; i++) {
 *                          System.out.println(Thread.currentThread().getName() + i);
 *                          if (i == 10) {
 *                              try {
 *                                  System.out.println("线程一加入此线程");
 *                                  thread1.join(); // 在i==10时 让线程一加入 先完成线程一的内容 在继续当前内容
 *                              } catch (InterruptedException e) {
 *                                  e.printStackTrace();
 *                              }
 *                          }
 *                      }
 *                      System.out.println(Thread.currentThread() + "线程结束");
 *                  });
 *                  thread1.start();
 *                  thread2.start();
 *
 * 实际上 thread2线程只是暂时处于等待状态 当thread1执行结束时 thread2才开始继续执行 只是在效果上看起来好像两个线程合并为一个线程执行而已
 */
public class Main {

    static void test1() {

        Thread thread = new Thread(() -> {
            System.out.println("线程一开始运行");
        });
        thread.start();
        thread.setPriority(Thread.MIN_PRIORITY);

    }

    static void test2() {

        Thread thread1 = new Thread(() -> {
            System.out.println("线程一开始运行");
            for (int i = 0; i < 50; i++) {
                if (i % 5 == 0) {
                    System.out.println("让位");
                    Thread.yield();
                }
                System.out.println("一打印: " + i);
            }
            System.out.println("线程一结束");
        });
        Thread thread2 = new Thread(() -> {
            System.out.println("线程二开始运行");
            for (int i = 0; i < 50; i++) {
                System.out.println("二打印: " + i);
            }
        });
        thread1.start();
        thread2.start();

    }

    static void test3() {

        Thread thread1 = new Thread(() -> {
            System.out.println("线程一开始运行");
            for (int i = 0; i < 50; i++) {
                System.out.println("一打印: " + i);
            }
            System.out.println("线程一结束");
        });
        Thread thread2 = new Thread(() -> {
            System.out.println("线程二开始运行");
            for (int i = 0; i < 50; i++) {
                System.out.println("二打印: " + i);
                if (i == 10) {
                    try {
                        System.out.println("线程一加入到此线程");
                        thread1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("线程二结束");
        });
        thread1.start();
        thread2.start();

    }

    static void test4() {

        Thread thread1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "开始运行");
            for (int i = 0; i < 50; i++) {
                System.out.println(Thread.currentThread().getName() + " 打印: " + i);
            }
            System.out.println(Thread.currentThread().getName() + "线程结束");
        });
        Thread thread2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "开始运行");
            for (int i = 0; i < 50; i++) {
                System.out.println(Thread.currentThread().getName() + " 打印: " + i);
                if (i == 10) {
                    try {
                        System.out.println("线程一加入此线程");
                        thread1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(Thread.currentThread() + "线程结束");
        });
        thread1.start();
        thread2.start();

    }

    public static void main(String[] args) {
        //test1();
        //test2();
        //test3();
        test4();

    }

}
