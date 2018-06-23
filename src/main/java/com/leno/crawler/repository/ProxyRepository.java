package com.leno.crawler.repository;

import com.leno.crawler.common.Repository;
import com.leno.crawler.entity.Proxy;

/**
 * @author leon
 * @date 2018-06-22 15:08
 * @desc
 */
public interface ProxyRepository extends Repository<Proxy> {
    Proxy findByIp(String ip);
}
