package com.leno.crawler.service;

import com.leno.crawler.entity.Proxy;
import com.leno.crawler.repository.ProxyRepository;
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

    /**
     * 随机获得代理地址
     * @return
     */
    public Proxy getProxyHost(){
        Random random = new Random();
        List<Proxy> proxies = proxyRepository.findAll();
        /*
            随机选用代理地址
         */
        Proxy proxy = proxies.get(random.nextInt(proxies.size()));
        // HttpHost proxy = new HttpHost("你的代理的IP", 8080, "http");
        return proxy;
    }

    /**
     * 代理失败对Proxy进行失败记录
     * @param proxy
     */
    public void failProxy(Proxy proxy){
        if (proxy.getTryNum()>5){
            proxyRepository.delete(proxy);
        }else {
            proxy.setTryNum(proxy.getTryNum() +1);
            proxyRepository.save(proxy);
        }
    }

}
