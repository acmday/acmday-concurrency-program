package com.acmday.concurrency.program.print.number;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author acmday
 * @date 2023/4/21 上午10:33
 */
@Slf4j
public class Main {

    static AlternatelyPrintHandler handler = new AlternatelyPrintHandler();

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.printerA();
            }
        }, "A").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.printerB();
            }
        }, "B").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.printerC();
            }
        }, "C").start();
    }

}
