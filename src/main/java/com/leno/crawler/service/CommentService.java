package com.leno.crawler.service;

import com.leno.crawler.common.BaseService;
import com.leno.crawler.common.Constants;
import com.leno.crawler.entity.Comments;
import com.leno.crawler.repository.CommentRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author leon
 * @date 2018-06-21 16:55
 * @desc 解析短评
 */
@Service
public class CommentService extends BaseService<Comments> {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * 解析短评
     * @param content 页面内容
     * @param url 解析的地址
     */
    @Async
    public void parseComment(String content,String url){
        logger.info("==========   Parse Comment:" + url);
        //parse comment page
        Pattern commentPattern = Pattern.compile(Constants.COMMENT_REGULAR_EXP);
        Matcher commentMatcher = commentPattern.matcher(url);
        if (commentMatcher.find()) {
            Document commentDoc = Jsoup.parse(content);
            parse(commentDoc);
        }
    }

    /**
     * 解析过程
      * @param commentDoc 解析的文本
     */
    @Async
    public void parse(Document commentDoc){
        if (commentDoc.getElementById("comments") != null) { // add to avoid exception like https://movie.douban.com/subject/25842478/comments
            Elements commentsElements = commentDoc.getElementById("comments").children();
            for (Element comment : commentsElements) {
                Comments comments = new Comments();
                if (comment.getElementsByClass("fold-bd").size() < 1 && comment.children().get(1).getElementsByTag("p").size() > 0) {
                    // to make sure the current item is the comment item rather than other info item      &&      检测fold-bd是查看是否有折叠，如果是折叠的评论则有fold-bd，折叠评论是指账号有异常的
                    String[] movies = commentDoc.getElementsByTag("h1").text().replace(" ", "").split("短评");
                    String commentForMovie = null;
                    for (String movie : movies) {
                        commentForMovie = movie;
                    }
                    comments.setCommentForMovie(commentForMovie);
                    comments.setCommentInfo(comment.children().get(1).getElementsByTag("p").text());//use "comment.children().get(1).text()" can get all commentInfo like "1819 有用 桃桃淘电影 2016-10-29 即便评分再高也完全喜欢不来。我们还是太热衷主题与意义了，以至于忽视了传递主题的方式与合理性。影片为了所谓的人性深度，而刻意设计剧情和人物转折，忽视基本的人物行为轨迹，都非常让人不舒服。喜欢有深度的电影，但希望能以更巧妙的方式讲出来，而不该是现在这样。以及形式上，这不就是舞台搬演么"
                    if (comment.getElementsByAttributeValue("class", "votes pr5").text().length() > 0) {
                        comments.setCommentVote(comment.getElementsByAttributeValue("class", "votes pr5").text());
                    }
                    comments.setCommentAuthor(comment.getElementsByAttribute("href").get(2).text());
                    comments.setCommentAuthorImgUrl(comment.getElementsByAttribute("href").get(2).attr("href"));

                    //save comment record
                    //TODO 有些评论里带表情,需要验证是否带emoji表情,我觉得最好是过滤,不然要改mysql,跟换血一样
                    Optional<Comments> optionalComments = Optional.ofNullable(commentRepository.findByCommentInfo(comments.getCommentInfo()));
                    if (!optionalComments.isPresent()){//如果该条评论不存在
                        try {
                            commentRepository.save(comments);
                            logger.info(">>>>>>saving comment for " + comments.getCommentForMovie() + " by:" + comments.getCommentAuthor() + "<<<<<<");
                        }catch (JpaSystemException e){
                            logger.error("这玩意里面有emoji表情,所以导致出错了  {}",comments.toString());
                        }
                    }
                }
            }
        }
    }
}
