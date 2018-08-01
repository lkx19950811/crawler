package com.leno.crawler.service;

import com.leno.crawler.common.BaseService;
import com.leno.crawler.common.Constants;
import com.leno.crawler.entity.Movie;
import com.leno.crawler.repository.MovieRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author leon
 * @date 2018-06-21 16:54
 * @desc 解析Movie
 */
@Service
public class MovieService extends BaseService<Movie> {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    MovieRepository movieRepository;

    /**
     * 开始初始化解析电影
     * @param content 请求到的页面
     */
    @Async
    public void parseMovie(String content,String url){
        logger.info("========== Parse Movie:" + url);
        //parse movie detail page
        Pattern moviePattern = Pattern.compile(Constants.MOVIE_REGULAR_EXP);//初始化正则
        Matcher movieMatcher = moviePattern.matcher(url);
        if (movieMatcher.find()) {
            Document movieDoc = Jsoup.parse(content);
            parseIt(movieDoc);
        }
    }

    /**
     *
     * @param doc 两个传入参数虽然内容不同
     */
    @Async
    public void parseIt(Document doc){
        Movie movie = new Movie();
        if (doc.html().contains("导演") && doc.html().contains("主演") && doc.html().contains("类型") &&
                doc.html().contains("语言") && doc.getElementById("info") != null) {
            Elements infos = doc.getElementById("info").children();
            for (Element info : infos) {
                if (info.childNodeSize() > 0) {
                    String key = info.getElementsByAttributeValue("class", "pl").text();
                    if ("导演".equals(key)) {
                        movie.setDirector(info.getElementsByAttributeValue("class", "attrs").text());
                    } else if ("编剧".equals(key)) {
                        movie.setScenarist(info.getElementsByAttributeValue("class", "attrs").text());
                    } else if ("主演".equals(key)) {
                        movie.setActors(info.getElementsByAttributeValue("class", "attrs").text());
                    } else if ("类型:".equals(key)) {
                        movie.setType(doc.getElementsByAttributeValue("property", "v:genre").text());
                    } else if ("制片国家/地区:".equals(key)) {
                        Pattern patternCountry = Pattern.compile(".制片国家/地区:</span>.+[\\u4e00-\\u9fa5]+.+[\\u4e00-\\u9fa5]+\\s+<br>");
                        Matcher matcherCountry = patternCountry.matcher(doc.html());
                        if (matcherCountry.find()) {
                            movie.setCountry(matcherCountry.group().split("</span>")[1].split("<br>")[0].trim());// for example: >制片国家/地区:</span> 中国大陆 / 香港     <br>
                        }
                    } else if ("语言:".equals(key)) {
                        Pattern patternLanguage = Pattern.compile(".语言:</span>.+[\\u4e00-\\u9fa5]+.+[\\u4e00-\\u9fa5]+\\s+<br>");
                        Matcher matcherLanguage = patternLanguage.matcher(doc.html());
                        if (matcherLanguage.find()) {
                            movie.setLanguage(matcherLanguage.group().split("</span>")[1].split("<br>")[0].trim());
                        }
                    } else if ("上映日期:".equals(key)) {
                        movie.setReleaseDate(doc.getElementsByAttributeValue("property", "v:initialReleaseDate").text());
                    } else if ("片长:".equals(key)) {
                        movie.setRuntime(doc.getElementsByAttributeValue("property", "v:runtime").text());
                    }
                }
            }
            movie.setTags(doc.getElementsByClass("tags-body").text());
            movie.setName(doc.getElementsByAttributeValue("property", "v:itemreviewed").text());
            movie.setRatingNum(doc.getElementsByAttributeValue("property", "v:average").text());
        }
        String movieName = movie.getName();
        if (!StringUtils.isEmpty(movieName)){
            List list = movieRepository.findByName(movieName);
            if (list.size()>0)return;//如果存在则丢弃
            movieRepository.save(movie);
            logger.info(">>>>>>saving " + movie.getName() + "<<<<<<");
        }
    }
}
