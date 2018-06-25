package com.leno.crawler.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author leon
 * @date 2018-06-22 14:37
 * @desc Http代理
 */
@Entity
public class Proxy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**
     * 唯一ID,自增
     */
    private Long id;
    /**
     * 代理ip
     */
    private String ip;
    /**
     *端口
     */
    private Integer port;
    /**
     * 代理类型
     */
    private String type;

    /**
     * 尝试次数连接
     */
    private Integer tryNum = 0;
    /**
     * 最后验证时间
     */
    private Date conDate;
    /**
     * 代理状态
     */
    private String status = "可用";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getConDate() {
        return conDate;
    }

    public void setConDate(Date conDate) {
        this.conDate = conDate;
    }

    public Integer getTryNum() {
        return tryNum;
    }

    public void setTryNum(Integer tryNum) {
        this.tryNum = tryNum;
    }

    public Proxy() {
    }

    public Proxy(String ip, Integer port, String type) {
        this.ip = ip;
        this.port = port;
        this.type = type;
    }
    public Proxy(String address){
        String ip = address.split(":")[0];
        Integer port = Integer.valueOf(address.split(":")[1]);
        this.ip = ip;
        this.port = port;
        this.type = "http";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getType() {
        return type;
    }

    public void setType(String types) {
        this.type = types;
    }

    @Override
    public String toString() {
        return "Proxy{" +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", types='" + type + '\'' +
                '}';
    }
}
