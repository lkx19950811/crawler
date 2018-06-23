package com.leno.crawler.schedule;

import com.leno.crawler.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


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
    @Scheduled(initialDelay = 0, fixedDelay = 1000L * 60L * 15L)
    public void parseProxy(){
        proxyService.parseProxyUrl();
    }
}
