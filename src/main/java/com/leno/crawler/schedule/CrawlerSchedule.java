package com.leno.crawler.schedule;

import com.leno.crawler.repository.ProxyRepository;
import com.leno.crawler.repository.RecordRepository;
import com.leno.crawler.service.CommentService;
import com.leno.crawler.service.MovieService;
import com.leno.crawler.service.ProxyService;
import com.leno.crawler.service.RecordService;
import com.leno.crawler.util.HttpUtils;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author leon
 * @date 2018-06-21 17:45
 * @desc
 */
@Component
public class CrawlerSchedule {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    RecordRepository recordRepository;
    @Autowired
    ProxyService proxyService;
    @Autowired
    RecordService recordService;
    @Autowired
    MovieService movieService;
    @Autowired
    CommentService commentService;
    @Value("${crawler.seed}")
    private String seed;
    @Scheduled(cron = "0/5 * * * * ?")
    public void CrawlerDouban() throws Exception {
//        friendlyToDouban();//先停几秒
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>    开始爬取        <<<<<<<<<<<<<<<<<");
        List<String> urls = new ArrayList<>();
        /*
        初始化循环的链接
         */
        recordService.initURLDouban(seed,urls);
        //获取代理
        HttpHost proxyHost =  proxyService.getProxyHost();
        for (String url : urls){
            String content = HttpUtils.proxyGet(url,proxyHost);
//            String content = HttpUtils.get(url);
            recordService.parseUrl(content);
            movieService.parseMovie(content,url);
            commentService.parseComment(content,url);
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
