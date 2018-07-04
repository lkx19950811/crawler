package com.leno.crawler;

import com.leno.crawler.util.HttpUtils;
import org.apache.http.HttpHost;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
 
public class CallableFutureTest {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws ExecutionException,
            InterruptedException {
        CallableFutureTest test = new CallableFutureTest();
 
        // 创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(2);
        // 创建两个有返回值的任务
        Callable c1 = test.new MyCallable("A");
        Callable c2 = test.new MyCallable("B");
        Callable c3 = test.new MyCallable("C");
        List<Callable> list = Arrays.asList(c1,c2,c3);
        for (Callable callable:list){
            Future future = pool.submit(callable);
            System.out.println(future.get());
            System.out.println(callable);
        }
 
        // 关闭线程池
        pool.shutdown();
    }
 
    @SuppressWarnings("unchecked")
    class MyCallable implements Callable {
        private String name;
 
        MyCallable(String name) {
            this.name = name;
        }
 
        public Object call() throws Exception {
            HttpHost porxy = new HttpHost("114.225.170.92",53128,"https");
            String res = HttpUtils.proxyGet("https://www.douban.com",porxy);
            System.out.println(name + "任务返回的内容");
            return res;
        }
    }
}