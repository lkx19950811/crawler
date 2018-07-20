package com.leno.crawler;


import java.util.concurrent.atomic.AtomicInteger;

public class Finalizable {
    static AtomicInteger aliveCount = new AtomicInteger(0);

    Finalizable() {
        //如果注释掉改行，在GC日志中仅能看到简单的新生代GC，程序不会因为内存问题停止
        //如果未注释，程序跑上几分钟就挂掉了，因为生产和消费的能力不对等。GC日志中大部分是Full GC。
//        aliveCount.incrementAndGet();
    }

    @Override
    protected void finalize() throws Throwable {
        Finalizable.aliveCount.decrementAndGet();
    }

    public static void main(String args[]) {
        for (int i = 0;; i++) {
            Finalizable f = new Finalizable();
            if ((i % 100_000) == 0) {
                System.out.format("After creating %d objects, %d are still alive.%n", new Object[] {i, Finalizable.aliveCount.get() });
            }
        }
    }
}