package com.leno.crawler.service;

import com.leno.crawler.common.Constants;
import com.leno.crawler.entity.Record;
import com.leno.crawler.repository.RecordRepository;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author leon
 * @date 2018-06-21 16:54
 * @desc 解析额外链接
 */
@Service
public class RecordService {
    Logger logger = LoggerFactory.getLogger(RecordService.class);
    @Autowired
    RecordRepository recordRepository;
    @Value("${maxLinkNum}")
    Long maxCycle;//爬取的链接最大数量

    /**
     * 从返回的html中解析链接
     * @param content 传入返回的html
     */
    public void parseUrl(String content)  {
        Long countNum = recordRepository.count();
        try {
            Parser parser = new Parser(content);
            HasAttributeFilter filter = new HasAttributeFilter("href");//提取页面中的链接
            List<String> nextLinkList = new ArrayList<>();
            if (countNum < maxCycle){//限制数据库中链接的数量
                NodeList list = parser.parse(filter);
                int count = list.size();
                for (int i=0;i<count;i++){
                    Node node = list.elementAt(i);
                    parseLink(nextLinkList,node);
                }
            }
            if (nextLinkList.size()>0){//如果提取到链接,则存入数据库
//                List<Record> records = nextLinkList.stream().map(url->{
//                    Record record = new Record(url);
//                    return record;
//                }).collect(Collectors.toList());
//                Integer result = recordRepository.saveAll(records).size();
                logger.info(">>>>>>>>>>>>>存入{}条链接<<<<<<<<<<<<<",nextLinkList.size());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 初始化爬取链接
     * @param seed
     * @param urls
     */
    public void initURLDouban(String seed, List<String> urls){
        Record record = recordRepository.findByUrl(seed);
        if (record.getCrawled().equals("0")){
            urls.add(seed);
        }else {
            urls.addAll(recordRepository.findUrlbyStatus("0"));
        }
    }

    /**
     * 将爬过的链接置为1
     * @param url
     */
    public void setRecordONE(String url){
        recordRepository.setRecordONE(url);
    }
    //TODO 这个有问题!!!!!!!!!!!!!
    /**
     * 解析单个节点
     * @param nextLinkList
     * @param node
     */
    public void parseLink(List<String> nextLinkList,Node node){
        if (node instanceof LinkTag){
            LinkTag link = (LinkTag) node;
            String nextLink = link.extractLink();
            if (nextLink.startsWith(Constants.MAINURL)){//如果是以豆瓣为开头的网址
                Optional<Record> recordOptional = Optional.ofNullable(recordRepository.findByUrl(nextLink));
                if (!recordOptional.isPresent()){//如果该条url不存在
                    //正则匹配短评和电影链接
                    Pattern moviePattern = Pattern.compile(Constants.MOVIE_REGULAR_EXP);
                    Matcher movieMatcher = moviePattern.matcher(nextLink);
                    Pattern commentPattern = Pattern.compile(Constants.COMMENT_REGULAR_EXP);
                    Matcher commentMatcher = commentPattern.matcher(nextLink);
                    if (movieMatcher.find() || commentMatcher.find()){//找到短评和电影链接,则存入list
                        nextLinkList.add(nextLink);
                        recordRepository.save(new Record(nextLink));//如果循环存入list入库,可能会造成重复几率大大增加
                    }
                }
            }
        }
    }
}
