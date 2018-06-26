package com.leno.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * @author leon
 * @date 2018-06-26 17:35
 * @desc
 */
public class ParseDocTest {
    @Test
    public void test1(){
        Document movieDoc = Jsoup.parse("404");
    }
}
