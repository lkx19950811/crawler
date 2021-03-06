package com.leno.crawler.repository;

import com.leno.crawler.common.Repository;
import com.leno.crawler.entity.Record;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author leon
 * @date 2018-06-21 17:18
 * @desc
 */
public interface RecordRepository extends Repository<Record> {
    /**
     * 根据url查询记录
     * @param url
     * @return
     */
    List<Record> findByUrl(String url);

    /**
     *  hsql中表名要与 Entity中的table名对应,如果没有table名,则默认类名为表名(区分大小写)
     * @param status
     * @return
     */
    @Query("SELECT r.url from record r where r.crawled=?1")
    List<String> findUrlbyStatus(String status);

    @Modifying
    @Query("update record r set r.crawled = '1' where r.url=?1")
    int setRecordONE(String url);
}
