package javase19;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 * java多线程编程实战
 * 这是整个篇章最后一个编程实战内容了 下一章节为反射一般开发者使用比较少 属于选学内容 不编排实战
 *
 * 生产者与消费者
 * 所谓的生产者消费者模型 是通过一个容器来解决生产者和消费者的强耦合问题 通俗的讲 就是生产者在不断的生产 消费者也在不断的消费
 * 可是消费者消费的产品是生产者生产的 这就必然存在一个中间容器 我们可以把这个容器想象成是一个货架 当货架空的时候 生产者要生产产品
 * 此时消费者在等待生产者往货架上生产产品 而当货架有货物的时候 消费者可以从货架上拿走商品 生产者此时等待货架出现空位 进而补货 这样不断的循环
 *
 * 通过多线程编程 来模拟一个餐厅的2个厨师和3个顾客 假设厨师炒出一个菜的时间为3秒 顾客吃掉菜品的时间为4秒
 */
public class Main {

    private static Queue<Object> queue = new LinkedList<>();

    static void Cs() {
        new Thread(Main::add, "厨师一号").start();
        new Thread(Main::add, "厨师二号").start();
    }
    static void add() {

        while (true) {
            try {
                Thread.sleep(3000);
                synchronized (queue) {
                    String name = Thread.currentThread().getName();
                    System.out.println(new Date() + " " + name + "出餐了");
                    queue.offer(new Object());
                    queue.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    static void Gk() {
        new Thread(Main::take, "顾客一号").start();
        new Thread(Main::take, "顾客二号").start();
        new Thread(Main::take, "顾客三号").start();
    }
    static void take() {

        while (true) {
            try {
                synchronized (queue) {
                    while (queue.isEmpty()) queue.wait();
                    queue.poll();
                    String name = Thread.currentThread().getName();
                    System.out.println(new Date() + " " + name + "拿到了餐品 正在吃饭...");
                }
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        Cs();
        Gk();
    }

}
