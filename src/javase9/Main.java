package javase9;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * java多线程
 * 注意: 本章节会涉及到操作系统相关知识
 *
 * 在了解多线程之前 让我们回顾一下操作系统中提到的进程概念:
 *      进程是程序执行的实体 每一个进程都是一个应用程序(比如我们运行QQ 浏览器 LOL 网易云音乐等软件) 都有自己的内存空间
 *      CPU一个核心同时只能处理一件事情 当出现多个进程需要同时运行时 CPU一般通过时间片轮转调度算法 来实现多个进程的同时运行
 *
 * 在早期的计算机中 进程是拥有资源和独立运行的最小单位 也是程序执行的最小单位 但是 如果我希望两个任务同时进行 就必须运行两个进程 由于每个进程都有一个自己的内存空间
 * 进程之间的通信就变得非常麻烦(比如要共享某些数据) 而执行不同进程会产生上下文切换 非常耗时 那么能否实现一个进程中就能够执行多个任务呢?
 *
 * 后来 线程横空出世 一个进程可以有多个线程 线程是程序执行中一个单一的顺序控制流程 现在线程才是程序执行流的最小单元
 * 各个线程之间共享程序的内存空间(也就是所在进程的内存空间) 上下文切换速度也高于进程
 *
 * 在java中 我们从开始 一直以来编写的都是单线程应用程序(运行main()方法的内容) 也就是说只能同时执行一个任务(无论你是调用方法 还是进行计算 始终都是依次进行的 也就是同步的)
 * 而如果我们希望同时执行多个任务(两个方法同时在运行或者是两个计算同时在运行 也就是异步的) 就需要用到java多线程框架 实际上一个java程序启动后 会创建很多线程 不仅仅只运行一个主线程:
 *                  ThreadMXBean bean = ManagementFactory.getThreadMXBean();
 *                  long[] ids = bean.getAllThreadIds();
 *                  ThreadInfo[] infos = bean.getThreadInfo(ids);
 *
 *                  for (ThreadInfo info : infos) {
 *                      System.out.println(info.getThreadName());
 *                  }
 *
 * 关于除了main线程1默认以外的线程 涉及到JVM相关底层原理 在这里不做讲解 了解就行
 */
public class Main {

    public static void main(String[] args) {

        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long[] ids = bean.getAllThreadIds();
        ThreadInfo[] infos = bean.getThreadInfo(ids);

        for (ThreadInfo info : infos) {
            System.out.println(info.getThreadName());
        }

    }

}
