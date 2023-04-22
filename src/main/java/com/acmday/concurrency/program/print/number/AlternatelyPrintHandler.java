package com.acmday.concurrency.program.print.number;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author acmday
 * @date 2023/4/21 上午10:34
 */
@Slf4j
public class AlternatelyPrintHandler{

    static volatile int number = 1;
    static volatile int index = 1;
    static final Object lock = new Object();

    /**
     * A线程打印的逻辑
     */
    public void printerA() {
        synchronized (lock) {
            while (true) {
                if(number > 100) {
                    lock.notifyAll();
                    return;
                }
                if(!Objects.equals(1, index)) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        log.error("系统异常！", e);
                    }
                } else {
                    log.info(number+"("+Thread.currentThread().getName()+")");
                    number++;
                    index++;
                    lock.notifyAll();
                }
            }
        }
    }

    /**
     * B线程打印逻辑（打印偶数）
     */
    public void printerB() {
        synchronized (lock) {
            while (true) {
                if(number > 100) {
                    lock.notifyAll();
                    return;
                }
                if(!Objects.equals(0, index & 1)) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        log.error("系统异常！", e);
                    }
                } else {
                    log.info("  "+number+"("+Thread.currentThread().getName()+")");
                    number++;
                    index++;
                    if(index > 4) {
                        index = 1;
                    }
                    lock.notifyAll();
                }
            }
        }
    }

    /**
     * C线程的打印逻辑
     */
    public void printerC() {
        synchronized (lock) {
            while (true) {
                if(number > 100) {
                    lock.notifyAll();
                    return;
                }
                if(!Objects.equals(3, index)) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        log.error("系统异常！", e);
                    }
                } else {
                    log.info("      "+number+"("+Thread.currentThread().getName()+")");
                    number++;
                    index++;
                    lock.notifyAll();
                }
            }
        }
    }
}
