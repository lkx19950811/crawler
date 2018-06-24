package com.leno.crawler.schedule;

import com.leno.crawler.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Random;


/**
 * 描述: 抓取代理地址
 *
 * @author Leo
 * @create 2018-06-24 上午 3:21
 */
@Component
public class ProxySchedule {
    @Autowired
    ProxyService proxyService;
    private static String page = "1";
//    @Scheduled(initialDelay = 0, fixedDelay = 1000L * 60L * 15L)//15分钟
    @Scheduled(initialDelay = 0, fixedDelay = 5000)//5秒
    public void parseProxy() throws ParseException, InterruptedException {
        friendlyToDouban();
        Integer p = Integer.valueOf(page);
        if (p==10)this.page="0";
        this.page = (p + 1 ) + "";
        proxyService.parseProxyUrl(page);

    }

    /**
     * 验证代理状态
     * @throws InterruptedException
     */
    @Scheduled(initialDelay = 10000, fixedDelay = 1000L * 60L)//1分钟
    public void verifyProxy() throws InterruptedException {
        proxyService.verifyProxy();
    }
    /**
     * 随机停止1到10秒
     * @throws InterruptedException
     */
    private static void friendlyToDouban() throws InterruptedException {
        Thread.sleep((new Random().nextInt(10) + 1)*1000);//sleep for the random second so that avoiding to be listed into blacklist
    }
}
