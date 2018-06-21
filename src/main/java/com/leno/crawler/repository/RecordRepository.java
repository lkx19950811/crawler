package com.leno.crawler.repository;

import com.leno.crawler.common.Repository;
import com.leno.crawler.entity.Record;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author leon
 * @date 2018-06-21 17:18
 * @desc
 */
public interface RecordRepository extends Repository<Record> {
    Record findByUrl(String url);
    @Query("select r.url from Record r where r.crawled=:status")
    List<String> findAllByCrawledIs(@Param("status") String status);
}
