package javase11;

/**
 * 线程的休眠和中断
 * 我们前面提到 一个线程处于运行状态下 线程的下一个状态会出现以下情况:
 *      > 当CPU给予的运行时间结束时 会从运行状态回到就绪(可运行)状态 等待下一次获得CPU资源
 *      > 当线程进入休眠/阻塞(如等待IO请求)/手动调用wait()方法时 会使用得线程处于等待状态 当等待状态结束后会回到就绪状态
 *      > 当线程出现异常或错误/被stop()方法强行停止/所有代码执行结束时 会使得线程的运行终止
 *
 * 而这个部分我们着重了解一下线程的休眠和中断 首先我们来了解一下如何使得线程进入休眠状态:
 *                  Thread thread = new Thread(() -> {
 *                      try {
 *                          System.out.println("N");
 *                          Thread.sleep(1000); // sleep方法是Thread静态方法 它只作用于当前线程(它知道当前线程是哪个)
 *                          System.out.println("B"); // 调用sleep后 线程会直接进入到等待状态 直到时间结束
 *                      } catch (InterruptedException e) {
 *                          e.printStackTrace();
 *                      }
 *                  });
 *                  thread.start();
 *
 * 通过调用sleep()方法来将当前线程进入休眠 使得线程处于等待状态一段时间 我们发现 此方法显示声明了会抛出一个InterruptedException异常 那么这个异常在什么时候会发生呢?
 *                  Thread thread = new Thread(() -> {
 *                      try {
 *                          Thread.sleep(10000); // 休眠10秒
 *                      } catch (InterruptedException e) {
 *                          e.printStackTrace();
 *                      }
 *                  });
 *                  thread.start();
 *                  try {
 *                      Thread.sleep(3000); // 休眠3秒 一定比线程thread先醒来
 *                      thread.interrupt(); // 调用thread的interrupt方法
 *                  } catch (InterruptedException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 我们发现 每一个Thread对象中 都有一个interrupt()方法 调用后此方法后 会给指定线程添加一个中断标记以告知线程需要立即停止运行或是进行其他操作
 * 由线程来响应此中断并进行相应的处理 我们前面提到的stop()方法是强制终止线程 这样的做法虽然简单粗暴 但是很有可能导致资源不能完全释放
 * 而这样的发生通知来告知线程需要中断 让线程自行处理后续 会更加合理一些 也是更加推荐的做法 我们来看看interrupt的用法:
 *                  Thread thread = new Thread(() -> {
 *                      System.out.println("线程开始运行");
 *                      while (true) { // 无限循环
 *                          if (Thread.currentThread().isInterrupted()) { // 判断是否存在中断标志
 *                              break; // 响应中断
 *                          }
 *                      }
 *                      System.out.println("线程被中断了");
 *                  });
 *                  thread.start();
 *                  try {
 *                      Thread.sleep(3000); // 休眠3秒 一定比线程thread先醒来
 *                      thread.interrupt(); // 调用thread的interrupt方法
 *                  } catch (InterruptedException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 通过isInterrupted()可以判断线程是否存在中断标志 如果存在 说明外部希望当前线程立即停止 也有可能是给当前线程发送一个其他的信号
 * 如果我们并不是希望收到中断信号就是结束程序 而是通知程序做其他事情 我们可以在收到中断信号后 复位中断标记 然后继续做我们的事情:
 *                  Thread thread = new Thread(() -> {
 *                      System.out.println("线程开始运行");
 *                      while (true) {
 *                          if (Thread.currentThread().isInterrupted()) { // 判断是否存在中断标志
 *                              System.out.println("发现中断信号中 复位成功继续运行...");
 *                              Thread.interrupted(); // 复位中断标记(返回值是当前是否由中断标记 这里不用管)
 *                          }
 *                      }
 *                  });
 *                  thread.start();
 *                  try {
 *                      Thread.sleep(3000); // 休眠3秒 一定比线程thread先醒来
 *                      thread.interrupt(); // 调用thread的interrupt方法
 *                  } catch (InterruptedException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 复位中断标记后 会立即清楚中断标记 那么 如果现在我们想要暂停线程呢? 我们希望线程暂时停下 比如等待其他线程执行完成后 再继续运行 那这样的操作怎么实现呢?
 *                  Thread thread = new Thread(() -> {
 *                      System.out.println("线程开始运行");
 *                      Thread.currentThread().suspend(); // 暂停此线程
 *                      System.out.println("线程继续运行");
 *                  });
 *                  thread.start();
 *                  try {
 *                      Thread.sleep(3000); // 休眠3秒 一定比线程thread先醒来
 *                      thread.resume(); // 复位此线程
 *                  } catch (InterruptedException e) {
 *                      e.printStackTrace();
 *                  }
 *
 * 虽然这样很方便地控制了线程的暂停状态 但是这两个方法我们发现实际上也是不推荐的做法 它很容易导致死锁 有关为什么被弃用的原因 我们会在线程锁继续探讨
 */
public class Main {

    static void test1() {

        /*Thread thread = new Thread(() -> {
            try {
                System.out.print("N");
                Thread.sleep(1000);
                System.out.println("B");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();*/

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            Thread.sleep(3000);
            thread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    static void test2() {

        /*Thread thread = new Thread(() -> {
            System.out.println("线程开始运行");
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
            System.out.println("线程被中断了");
        });
        thread.start();
        try {
            Thread.sleep(5000);
            thread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        Thread thread = new Thread(() -> {
            System.out.println("线程开始运行");
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("发现中断信号中 复位成功继续运行...");
                    Thread.interrupted();
                }
            }
        });
        thread.start();
        try {
            Thread.sleep(3000);
            thread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    static void test3() {

        Thread thread = new Thread(() -> {
            System.out.println("线程开始运行");
            Thread.currentThread().suspend();
            System.out.println("线程继续运行");
        });
        thread.start();
        try {
            Thread.sleep(3000);
            thread.resume();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //test1();
        //test2();
        test3();
    }

}
