package com.leno.crawler.schedule;

import com.leno.crawler.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${proxyPage}")
    Integer proxyPage;
    private static String page = "0";

    /**
     * 从免费代理网站抓取地址
     * @throws ParseException
     * @throws InterruptedException
     */
    @Scheduled(initialDelay = 0, fixedDelay = 1000L * 60L * 15L)//启动延迟0,间隔15分钟
//    @Scheduled(initialDelay = 0, fixedDelay = 5000)//5秒
    public void parseProxy() throws ParseException, InterruptedException {
        friendlyToDouban();
        Integer p = Integer.valueOf(page);
        if (p>=proxyPage)this.page="0";
        this.page = (p + 1 ) + "";
        proxyService.parseProxyUrl(page);

    }

    /**
     * 验证代理状态
     * @throws InterruptedException
     */
    @Scheduled(initialDelay = 1000 * 15, fixedDelay = 1000L * 60L * 5L)//启动延迟15秒,验证间隔5分钟
    public void verifyProxy() throws InterruptedException, ParseException {
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
