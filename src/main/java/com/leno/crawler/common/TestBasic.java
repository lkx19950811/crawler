package com.leno.crawler.common;

import com.leno.crawler.CrawlerApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * 描述: 测试的基类
 *
 * @author Leo
 * @create 2017-12-17 下午 11:03
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CrawlerApplication.class)
@WebAppConfiguration
public class TestBasic {
}
