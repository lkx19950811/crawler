package com.leno.crawler.entity;

import javax.persistence.*;

/**
 * 描述:
 * 爬取的链接
 *
 * @author Leo
 * @create 2017-12-27 上午 2:40
 */
@Entity(name = "record")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recordId")
    private Long recordId;
    /**
     * 爬取的url链接
     */
    @Column(name = "URL")
    private String url;
    /**
     * 是否被爬过
     */
    @Column(name = "crawled")
    private String crawled;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCrawled() {
        return crawled;
    }

    public void setCrawled(String crawled) {
        this.crawled = crawled;
    }
}
