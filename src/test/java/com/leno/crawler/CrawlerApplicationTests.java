package com.leno.crawler;

import com.leno.crawler.repository.RecordRepository;
import com.leno.crawler.service.RecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CrawlerApplication.class)
@WebAppConfiguration
public class CrawlerApplicationTests {
    @Autowired
    RecordRepository recordRepository;
    @Autowired
    RecordService service;
    @Test
    public void contextLoads() {
        recordRepository.findUrlbyStatus("0");
//        recordRepository.findAll();
    }
    @Test
    public void test1(){
        List<String> urls = new ArrayList<>();
        service.initURLDouban("https://movie.douban.com/",urls);
        System.out.println(urls);
    }
    @Test
    public void test2(){
        recordRepository.setRecordONE("https://movie.douban.com/");
    }
    @Test
    public void test3(){
        List list = recordRepository.findByUrl("https://movie.douban4.com/");
        System.out.println(list);
    }

}
