package com.leno.crawler.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 线程池工具类,使用单例模式,线程安全
 */
public class ThreadManager {
    static private Logger logger = LoggerFactory.getLogger(ThreadManager.class);
    public static ThreadPool instance;
    private ThreadPoolExecutor longExecutor; // 耗时比较长的线程池   用来请求网络
    private ThreadPoolExecutor shortExecutor; // 比较短的线程池    用来加载本地数据

    // 获取单例的线程池对象
    public static ThreadPool getInstance() {
        if (instance == null) {
            synchronized (ThreadManager.class) {
                if (instance == null) {
                    int cpuNum = Runtime.getRuntime().availableProcessors();// 获取处理器数量
                    int threadNum = cpuNum * 2 + 1;// 根据cpu数量,计算出合理的线程并发数
                    logger.info("当前PC的核心数为:{},创建线程并发数:{}",cpuNum,threadNum);
                    instance = new ThreadPool(threadNum-1, threadNum, Integer.MAX_VALUE);//默认是双核的cpu 每个核心走一个线程 一个等待线程
                }
            }
        }
        return instance;
    }

    public static class ThreadPool {
        private ThreadPoolExecutor mExecutor;
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;

        private ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        public void execute(Runnable runnable) {
            if (runnable == null) {
                return;
            }
            if (mExecutor == null) {
                mExecutor = new ThreadPoolExecutor(corePoolSize,// 核心线程数
                        maximumPoolSize, // 最大线程数
                        keepAliveTime, // 闲置线程存活时间
                        TimeUnit.MILLISECONDS,// 时间单位
                        new LinkedBlockingDeque<>(Integer.MAX_VALUE),// 线程队列
                        Executors.defaultThreadFactory(),// 线程工厂
                        new ThreadPoolExecutor.AbortPolicy() {// 队列已满,而且当前线程数已经超过最大线程数时的异常处理策略
                            @Override
                            public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                                super.rejectedExecution(r, e);
                            }
                        }
                );
            }
            mExecutor.execute(runnable);
        }

          public <T> Future<T> submit(Callable<T> task){
            if (task == null) {
                return null;
            }
            if (mExecutor == null) {
                mExecutor = new ThreadPoolExecutor(corePoolSize,// 核心线程数
                        maximumPoolSize, // 最大线程数
                        keepAliveTime, // 闲置线程存活时间
                        TimeUnit.MILLISECONDS,// 时间单位
                        new LinkedBlockingDeque<>(Integer.MAX_VALUE),// 线程队列
                        Executors.defaultThreadFactory(),// 线程工厂
                        new ThreadPoolExecutor.AbortPolicy() {// 队列已满,而且当前线程数已经超过最大线程数时的异常处理策略
                            @Override
                            public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                                super.rejectedExecution(r, e);
                            }
                        }
                );
            }
            return mExecutor.submit(task);
        }

        // 从线程队列中移除对象
        public void cancel(Runnable runnable) {
            if (mExecutor != null) {
                mExecutor.getQueue().remove(runnable);
            }
        }
    }
}