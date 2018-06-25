package com.leno.crawler;

import com.leno.crawler.common.TestBasic;
import com.leno.crawler.entity.Proxy;
import com.leno.crawler.repository.ProxyRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @author leon
 * @date 2018-06-22 15:12
 * @desc 初始化时将代理插入到数据库
 */
public class ProxyInsert extends TestBasic {
    @Autowired
    ProxyRepository proxyRepository;
    @Test
    public void insert(){
        Proxy proxy = new Proxy("117.86.164.48:18118");
        Proxy proxy1 = new Proxy("222.85.39.86:808");
        Proxy proxy2 = new Proxy("182.90.9.97:53");
        Proxy proxy3 = new Proxy("118.190.95.35:9001");
        Proxy proxy4 = new Proxy("114.115.182.59:3128");
        Proxy proxy5 = new Proxy("61.135.217.7:80");
        Proxy proxy6 = new Proxy("111.231.115.150:8888");
        Proxy proxy7 = new Proxy("118.190.95.43:9001");
        Proxy proxy8 = new Proxy("122.114.31.177:808");
        Proxy proxy9 = new Proxy("49.84.47.65:31017");
        Proxy proxy10 = new Proxy("117.86.23.67:18118");
        Proxy proxy11 = new Proxy("125.111.119.125:40996");
        Proxy proxy12 = new Proxy("112.115.57.20:3128");
        Proxy proxy13 = new Proxy("101.236.35.98:8866");
        Proxy proxy14 = new Proxy("111.155.116.234:8123");
        Proxy proxy15 = new Proxy("218.9.83.78:61202");
        Proxy proxy16 = new Proxy("121.231.32.179:6666");
        Proxy proxy17 = new Proxy("115.192.246.138:8118");
        Proxy proxy18 = new Proxy("110.73.0.224:8123");
        Proxy proxy19 = new Proxy("125.111.118.134:20047");
        List<Proxy> proxies = Arrays.asList(proxy,proxy1,proxy2,proxy3,proxy4,proxy5,
                                            proxy6,proxy7,proxy8,proxy9,proxy10,
                                            proxy11,proxy12,proxy13,proxy14,proxy15,
                                            proxy16,proxy17,proxy18,proxy19);
        proxyRepository.saveAll(proxies);
    }
}
