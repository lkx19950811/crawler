package com.leno.crawler.schedule;

import com.leno.crawler.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author leon
 * @date 2018-06-21 17:45
 * @desc
 */
@Component
public class CrawlerSchedule {
    @Autowired
    RecordRepository recordRepository;
    @Value("${crawler.seed}")
    private String seed;
    @Scheduled(cron = "0/5 * * * * ?",fixedDelay = 5000)
    public void CrawlerDouban() throws InterruptedException {
        friendlyToDouban();

    }

    /**
     * 随机停止1到10秒
     * @throws InterruptedException
     */
    private static void friendlyToDouban() throws InterruptedException {
        Thread.sleep((new Random().nextInt(10) + 1)*1000);//sleep for the random second so that avoiding to be listed into blacklist
    }
}
