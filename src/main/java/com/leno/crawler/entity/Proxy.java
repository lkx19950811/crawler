package com.leno.crawler.entity;

import javax.persistence.*;

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
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", types='" + type + '\'' +
                '}';
    }
}
