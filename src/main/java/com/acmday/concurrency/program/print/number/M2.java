package com.acmday.concurrency.program.print.number;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author acmday
 * @date 2023/4/22 下午8:19
 *
 * 另一种实现方式
 */
public class M2 {
    static Lock lock = new ReentrantLock();
    static Condition cond = lock.newCondition();
    static volatile int startFlag = 0;
    static volatile int num = 1;
    static volatile String threadFlag = "A";

    public static void main(String[] args) throws InterruptedException {
        Thread a = new Thread(new A(), "A");
        a.start();
        Thread b = new Thread(new B(), "B");
        b.start();
        Thread c = new Thread(new C(), "C");
        c.start();

    }

    public static class A implements Runnable {
        @Override
        public void run() {
            lock.lock();
            try {
                // 保证三个线程同时进入锁队列
                startFlag = startFlag | 1;
                if (startFlag != 7) {
                    cond.await();
                } else {
                    cond.signalAll();
                }
                while (num < 101) {
                    // 判断当前线程是否可以执行
                    while (!threadFlag.equals("A")) {
                        if (num > 100) {
                            break;
                        }
                        cond.await();
                    }
                    if (num > 100) {
                        break;
                    }
                    System.out.println(num++ + "("+Thread.currentThread().getName()+")");
                    // 切换线程B
                    threadFlag = "B";
                    cond.signalAll();
                }
                cond.signalAll();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }

    public static class B implements Runnable {
        private String condFlag = "C";

        @Override
        public void run() {
            lock.lock();
            try {
                startFlag = startFlag | 2;
                if (startFlag != 7) {
                    cond.await();
                } else {
                    cond.signalAll();
                }
                while (num < 101) {
                    while (!threadFlag.equals("B")) {
                        if (num > 100) {
                            break;
                        }
                        cond.await();
                    }
                    if (num > 100) {
                        break;
                    }
                    System.out.println("    " + num++ + "("+Thread.currentThread().getName()+")");
                    if (condFlag.equals("C")) {
                        // 记录下一次唤醒的线程A
                        condFlag = "A";
                        // 切换到线程C
                        threadFlag = "C";
                    } else {
                        // 记录下一次唤醒的线程C
                        condFlag = "C";
                        // 切换到线程A
                        threadFlag = "A";
                    }
                    cond.signalAll();
                }
                cond.signalAll();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }

    public static class C implements Runnable {
        @Override
        public void run() {
            lock.lock();
            try {
                startFlag = startFlag | 4;
                if (startFlag != 7) {
                    cond.await();
                } else {
                    cond.signalAll();
                }
                while (num < 101) {
                    while (!threadFlag.equals("C")) {
                        if (num > 100) {
                            break;
                        }
                        cond.await();
                    }
                    if (num > 100) {
                        break;
                    }
                    System.out.println("        " + num++ + "("+Thread.currentThread().getName()+")");
                    threadFlag = "B";
                    cond.signalAll();
                }
                cond.signalAll();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }
}