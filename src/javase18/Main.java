package javase18;

import java.util.Arrays;

/**
 * 守护线程
 * 不要把守护进程和守护线程相提并论 守护进程在后台运行 不需要和用户交互 本质和普通进程类似 而守护线程就不一样了 当其他所有的非守护线程结束之后
 * 守护线程是自动结束 也就是说 java中所有的线程都执行完毕后 守护线程自动结束 因此守护线程不适合进行IO操作 只适合打打杂:
 *                  Thread thread = new Thread(() -> {
 *                      while (true) {
 *                          try {
 *                              System.out.println("程序正常运行中...");
 *                              Thread.sleep(1000);
 *                          } catch (InterruptedException e) {
 *                              e.printStackTrace();
 *                          }
 *                      }
 *                  });
 *                  thread.setDaemon(true); // 设置为守护线程(必须在开始之前 中途是不允许转换的)
 *                  thread.start();
 *                  for (int i = 0; i < 5; i++) {
 *                      try {
 *                          Thread.sleep(1000);
 *                      } catch (InterruptedException e) {
 *                          e.printStackTrace();
 *                      }
 *                  }
 *
 * 在守护线程中产生的新线程也是守护的:
 *                  Thread thread1 = new Thread(() -> {
 *                      Thread it = new Thread(() -> {
 *                          while (true) {
 *                              try {
 *                                  System.out.println("程序正常运行中...");
 *                                  Thread.sleep(1000);
 *                              } catch (InterruptedException e) {
 *                                  e.printStackTrace();
 *                              }
 *                          }
 *                      });
 *                      it.start();
 *                  });
 *                  thread1.setDaemon(true);
 *                  thread1.start();
 *                  for (int i = 0; i < 5; i++) {
 *                      try {
 *                          Thread.sleep(1000);
 *                      } catch (InterruptedException e) {
 *                          e.printStackTrace();
 *                      }
 *                  }
 *
 * 再谈集合类并行方法
 * 其实我们之前再讲解集合类的根接口时 就发现有这样一个方法:
 *                  default Stream<E> parallelStream() {
 *                      return StreamSupport.stream(spliterator(), true);
 *                  }
 *
 * 并行流 其实就是一个多线程执行的流 它通过默认的ForkJoinPool实现(这里不讲解原理) 它可以提高你的多线程任务的速度
 *                  static void test2() {
 *
 *                      List<Integer> list = new ArrayList<>(Arrays.asList(1, 4, 5, 2, 8, 3, 6, 0));
 *                      list
 *                              .parallelStream() // 获得并行流
 *                              .forEach(i -> System.out.println(Thread.currentThread().getName() + " -> " + i));
 *
 *                  }
 *
 * 我们发现 froEach操作的顺序 并不是我们实际List中的顺序 同时每次打印也是不同的线程在执行 我们可以通过调用forEachOrdered()方法来使用单线程维持原本的顺序:
 *                  static void test2() {
 *
 *                      List<Integer> list = new ArrayList<>(Arrays.asList(1, 4, 5, 2, 8, 3, 6, 0));
 *                      list
 *                              .parallelStream()
 *                              .forEachOrdered(System.out::print);
 *
 *                  }
 *
 * 我们之前还发现 再Arrays数组工具类中 也包含大量的并行方法:
 *                  int[] arr = new int[] {1, 4, 5, 2, 9, 3, 6, 0};
 *                  Arrays.parallelSort(arr);
 *                  System.out.println(Arrays.toString(arr));
 *
 * 更多地使用并行方法 可以更加充分地发挥现代计算机多核心的优势 但是同时需要注意多线程产生的异步问题
 *                  int[] arr = new int[] {1, 4, 5, 2, 9, 3, 6, 0};
 *                  Arrays.parallelSetAll(arr, i -> {
 *                      System.out.println(Thread.currentThread().getName());
 *                      return arr[i];
 *                  });
 *                  System.out.println(Arrays.toString(arr));
 *
 * 通过对java多线程的了解 我们就具备了利用多线程解决问题的思维
 */
public class Main {

    static void test1() {

        /*Thread thread = new Thread(() -> {
            while (true) {
                try {
                    System.out.println("程序正常运行中...");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

        Thread thread1 = new Thread(() -> {
            Thread it = new Thread(() -> {
                while (true) {
                    try {
                        System.out.println("程序正常运行中...");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            it.start();
        });
        thread1.setDaemon(true);
        thread1.start();
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    static void test2() {

        /*List<Integer> list = new ArrayList<>(Arrays.asList(1, 4, 5, 2, 8, 3, 6, 0));
        list
                .parallelStream()
                .forEach(i -> System.out.println(Thread.currentThread().getName() + " -> " + i));
                //.forEachOrdered(System.out::print);*/

        /*int[] arr = new int[] {1, 4, 5, 2, 9, 3, 6, 0};
        Arrays.parallelSort(arr);
        System.out.println(Arrays.toString(arr));*/

        int[] arr = new int[] {1, 4, 5, 2, 9, 3, 6, 0};
        Arrays.parallelSetAll(arr, i -> {
            System.out.println(Thread.currentThread().getName());
            return arr[i];
        });
        System.out.println(Arrays.toString(arr));

    }

    public static void main(String[] args) {
        //test1();
        test2();
    }

}
