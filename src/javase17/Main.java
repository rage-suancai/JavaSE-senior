package javase17;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时器
 * 我们有时候会有这样的需求 我希望定时执行任务 比如三秒后执行 其实我们可以通过使用Thread.sleep()来实现:
 *                      static void test1() {
 *                          new TimerTask(() -> System.out.println("我是定时器"), 3000).start(); // 创建并启动此定时任务
 *                      }
 *
 *                      public class TimerTask {
 *                      Runnable task;
 *                      long time;
 *
 *                      public TimerTask(Runnable runnable, long time) {
 *                          this.task = runnable;
 *                          this.time = time;
 *                      }
 *
 *                      public void start() {
 *                          new Thread(() -> {
 *                              try {
 *                                  Thread.sleep(time);
 *                                  task.run(); // 休眠后在运行
 *                              } catch (InterruptedException e) {
 *                                  e.printStackTrace();
 *                              }
 *                          }).start();
 *                      }
 *
 *                  }
 *
 * 我们通过自行封装一个TimeTask类 并在启动时 先休眠三秒钟 在执行我们传入的内容
 * 那么现在我们希望 能否循环执行一个任务呢? 比如我希望每隔一秒执行异常代码 这样该怎么做呢?
 *                  public void startRun() {
 *
 *                      new Thread(() -> {
 *                          try {
 *                              while (true) { // 无限循环执行
 *                                  Thread.sleep(time);
 *                                  task.run(); // 休眠后再运行
 *                              }
 *                          } catch (InterruptedException e) {
 *                              e.printStackTrace();
 *                          }
 *                      }).start();
 *
 *                  }
 *
 * 现在我们将单次执行放入到一个无限循环中 这样就能一直执行了 并且按照我们的间隔时间进行
 *
 * 但是终究是我们自己实现 可能很多方面还没考虑到 java也为我们提供了一套自己的框架用于处理定时任务:
 *                  static void test3() {
 *
 *                      Timer timer = new Timer(); // 创建定时器对象
 *                      timer.schedule(new TimerTask() { // 注意: 这个是一个抽象类 不是接口 无法使用lambda表达式简化 只能使用匿名内部类
 *                          @Override
 *                          public void run() {
 *                              System.out.println(Thread.currentThread().getName()); // 打印当前线程名称
 *                          }
 *                      }, 1000); // 执行一个延时任务
 *
 *                  }
 *
 * 我们可以通过创建一个Timer类来让它进行定时任务调度 我们可以通过此对象来创建任意类型的定时任务 包括延时任务 循环定时任务等
 * 我们发现 虽然任务执行完成了 但是我们的程序并没有停止 这是因为Timer内存维护了一个任务队列和一个工作线程:
 *                  public class Timer {
 *
 *                      private final TaskQueue queue=new TaskQueue()
 *
 *                      private final TimerThread thread=new TimerThread(queue);
 *
 *                      ...
 *
 *                  }
 *
 * TimerThread继承自Thread 是一个新创建的线程 在构造时自动启动:
 *                  public Timer(String name) {
 *                      thread.setName(name);
 *                      thread.start();
 *                  }
 *
 * 而它的run方法会循环地读取队列中是否还有任务 如果有任务依次执行 没有的话就暂时处于休眠状态:
 *                  public void run() {
 *                      try {
 *                          mainLoop();
 *                      } finally {
 *                          // Someone killed this Thread, behave as if Timer cancelled
 *                          synchronized(queue) {
 *                              newTasksMayBeScheduled = false;
 *                              queue.clear(); // Eliminate obsolete references
 *                          }
 *                      }
 *                  }
 *
 *                  private void mainLoop() {
 *                      while (true) {
 *                          try {
 *                              TimerTask task;
 *                              boolean taskFired;
 *                              synchronized(queue) {
 *                                  // Wait for queue to become non-empty
 *                                  while (queue.isEmpty() && newTasksMayBeScheduled) // 当队列为空同时没有被关闭时 会调用wait()方法暂时处于等待状态 当有新的任务时 会被唤醒
 *                                      queue.wait();
 *                                  if (queue.isEmpty())
 *                                      break; // 当被唤醒后都没有任务时 就会结束循环 也就是结束工作线程
 *                                          ...
 *                  }
 *
 * newTasksMayBeScheduled实际上即使标记当前定时器是否关闭 当它为false时 表示已经不会在有新的任务到来 也就是关闭 我们可以通过调用cancel()方法来关闭它的工作线程:
 *                  public void cancel() {
 *                      synchronized(queue) {
 *                          thread.newTasksMayBeScheduled = false;
 *                          queue.clear();
 *                          queue.notify();  // 唤醒wait使得工作线程结束
 *                      }
 *                  }
 *
 * 因此 我们可以在使用完成后 调用Timer的cancel()方法以正常退出我们的程序:
 *                  Timer timer = new Timer();
 *                  timer.schedule(new TimerTask() {
 *                      @Override
 *                      public void run() {
 *                          System.out.println(Thread.currentThread().getName());
 *                          timer.cancel(); // 结束
 *                      }
 *                  }, 1000);
 */
public class Main {

    static void test1() {
        new TimerTaskMe(() -> System.out.println("我是定时器"), 3000).start();
    }

    static void test2() {
        new TimerTaskMe(() -> System.out.println("我是定时器"), 1000).startRun();
    }

    static void test3() {

        /*Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        }, 1000);*/

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
                timer.cancel();
            }
        }, 1000);

    }

    public static void main(String[] args) {
        //test1();
        //test2();
        test3();
    }

}
