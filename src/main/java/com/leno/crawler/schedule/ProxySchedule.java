package com.leno.crawler.schedule;

import com.leno.crawler.service.ProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Random;
import java.util.concurrent.ExecutionException;


/**
 * 描述: 抓取代理地址
 *
 * @author Leo
 * @create 2018-06-24 上午 3:21
 */
@Component
public class ProxySchedule {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ProxyService proxyService;
    @Value("${proxyPage}")
    Integer proxyPage;
    @Value("${getProxy}")
    Boolean isgetProxy;
    private static String page = "0";
    @Value("${verifyProxy}")
    Boolean isverifyProxy;

    @Autowired
    public ProxySchedule(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    /**
     * 从免费代理网站抓取地址
     * @throws ParseException 页面解析异常
     * @throws InterruptedException 中断异常,因为使用了Thread.sleep
     */
    @Scheduled(initialDelay = 0, fixedDelay = 1000L * 60L * 15L)//启动延迟0,间隔15分钟
//    @Scheduled(initialDelay = 0, fixedDelay = 5000)//5秒
    public void parseProxy() throws ParseException, InterruptedException {
        if (isgetProxy){
            friendlyToDouban();
            Integer p = Integer.valueOf(page);
            if (p>=proxyPage)page="0";
            page = (p + 1 ) + "";
            proxyService.parseProxyUrl(page);
        }else {
            logger.info("自动抓取代理未开启");
        }
    }

    /**
     * 验证代理状态
     * @throws InterruptedException
     */
    @Scheduled(initialDelay = 1000 * 45, fixedDelay = 1000L * 60L * 5L)//启动延迟45秒,验证间隔5分钟
    public void verifyProxy() throws InterruptedException, ExecutionException {
        if (isverifyProxy){
            proxyService.verifyProxy();
        }else {
            logger.info(">>>>>>>>>>> 自动验证代理未开启 <<<<<<<<<<<");
        }
    }
    /**
     * 随机停止1到10秒
     * @throws InterruptedException
     */
    private static void friendlyToDouban() throws InterruptedException {
        Thread.sleep((new Random().nextInt(10) + 1)*1000);//sleep for the random second so that avoiding to be listed into blacklist
    }
}
