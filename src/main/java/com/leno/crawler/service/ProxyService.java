package com.leno.crawler.service;

import com.leno.crawler.entity.Proxy;
import com.leno.crawler.repository.ProxyRepository;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * @author leon
 * @date 2018-06-22 15:40
 * @desc
 */
@Service
public class ProxyService {
    @Autowired
    ProxyRepository proxyRepository;
    public HttpHost getProxyHost(){
        Random random = new Random();
        List<Proxy> proxies = proxyRepository.findAll();
        /*
            随机选用代理地址
         */
        Proxy proxy = proxies.get(random.nextInt(proxies.size()));
        // HttpHost proxy = new HttpHost("你的代理的IP", 8080, "http");
        return new HttpHost(proxy.getIp(),proxy.getPort(),proxy.getType());
    }
}
