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
    static Object lock = new Object();


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
//                    if(index > 4) {
//                        index = 1;
//                    }
                    lock.notifyAll();
                }
            }
        }
    }

    /**
     * 打印偶数
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
//                    if(index > 4) {
//                        index = 1;
//                    }
                    lock.notifyAll();
                }
            }
        }
    }
}
