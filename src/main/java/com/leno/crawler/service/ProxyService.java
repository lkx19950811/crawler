package com.leno.crawler.service;

import com.leno.crawler.entity.Proxy;
import com.leno.crawler.repository.ProxyRepository;
import com.leno.crawler.util.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author leon
 * @date 2018-06-22 15:40
 * @desc
 */
@Service
public class ProxyService {
    private static final Logger logger = LoggerFactory.getLogger(ProxyService.class);
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
        if (proxy.getTryNum()>3){
            proxyRepository.delete(proxy);
        }else {
            proxy.setTryNum(proxy.getTryNum() +1);
            proxyRepository.save(proxy);
        }
    }

    /**
     * 抓取代理
     */
    public void parseProxyUrl(){
        String content = HttpUtils.get("http://www.xicidaili.com/wt/");
        Document document = Jsoup.parse(content);
        Elements elements = document.getElementById("ip_list").getElementsByTag("tbody");
        if (elements.size()==1){
            Elements trs = elements.get(0).children();
            for (Element tr : trs){
                Proxy proxy = new Proxy();
                if (tr.toString().contains("国家")){
                    continue;
                }else {
                    String ipReg = "^(\\d+\\.\\d+\\.\\d+\\.\\d+).*";
                    Pattern r = Pattern.compile(ipReg);
                    Matcher m = r.matcher(tr.text());
                    if (m.find()){
                        String ip = m.group(1);
                        Optional optional = Optional.ofNullable(proxyRepository.findByIp(ip));
                        if (optional.isPresent()){
                            continue;
                        }else {
                            proxy.setIp(ip);
                        }
                    }
                    String portReg = "[\\s](\\d+)[\\s]";
                    r = Pattern.compile(portReg);
                    m = r.matcher(tr.text());
                    if (m.find()){
                        String port = m.group(1);
                        proxy.setPort(Integer.valueOf(port));
                    }
                    proxy.setType("http");
                    proxy.setTryNum(0);
                    String secReg = ".*(\\d+\\.\\d+).*";
                    r = Pattern.compile(secReg);
                    m = r.matcher(tr.children().get(6).toString());
                    if (m.find()){
                        Double sec = Double.valueOf(m.group(1));
                        if (sec>1.0)continue;
                    }
                    if (!tr.child(4).text().equals("高匿")){
                        continue;
                    }
                    proxyRepository.save(proxy);
                    logger.info("Saving >>>>>>>>>>>>>>>> {}:{}",proxy.getIp(),proxy.getPort());
                }
            }
        }
    }

}
