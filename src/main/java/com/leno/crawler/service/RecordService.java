package com.leno.crawler.service;

import com.leno.crawler.entity.Record;
import com.leno.crawler.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author leon
 * @date 2018-06-21 16:54
 * @desc 解析额外链接
 */
@Service
public class RecordService {
    @Autowired
    RecordRepository recordRepository;

    /**
     * 从返回的html中解析链接
     * @param content 传入返回的html
     */
    public void getURL(String content){

    }
    public void initURLDouban(String seed, List<String> urls){
        Record record = recordRepository.findByUrl(seed);
        if (record.getCrawled().equals("0")){
            urls.add(seed);
        }else {
            urls = recordRepository.findAllByCrawledIs("1");
        }
    }
}
