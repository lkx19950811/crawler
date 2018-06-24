package com.leno.crawler.repository;

import com.leno.crawler.common.Repository;
import com.leno.crawler.entity.Proxy;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author leon
 * @date 2018-06-22 15:08
 * @desc
 */
public interface ProxyRepository extends Repository<Proxy> {
    Proxy findByIp(String ip);

    /**
     * 按照验证时间 由近至远排序,并排除传入ip
     * @return
     */
    @Query("select p from Proxy p where p.ip not in ?1 order by p.conDate desc ")
    List<Proxy> findAllByIpNotInOrderByConDateDesc(String ip);

    /**
     * 单纯按照时间排序
     * @return
     */
    @Query("select p from Proxy p order by p.conDate desc ")
    List<Proxy> findAllByOrOrderByConDate();
}
