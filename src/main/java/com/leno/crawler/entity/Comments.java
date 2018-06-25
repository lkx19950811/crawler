package com.leno.crawler.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * 描述:
 * 评论类
 *
 * @author Leo
 * @create 2017-12-26 下午 9:45
 */
@Entity(name = "comments")
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**
     * 评论唯一ID,自增
     */
    @Column(name = "commentId")
    private Long commentId;
    /**
     * 评论内容
     */
    @Column(name = "commentInfo",columnDefinition = "text")
    private String commentInfo;
    /**
     * 评论者
     */
    @Column(name = "commentAuthor")
    private String commentAuthor;
    /**
     * 评论者头像链接
     */
    @Column(name = "commentAuthorImgUrl")
    private String commentAuthorImgUrl;
    /**
     * 评论点赞数量
     */
    @Column(name = "commentVote")
    private String commentVote = "0";
    /**
     * 评论的电影名
     */
    @Column(name = "commentForMovie")
    private String commentForMovie;
    /**
     * 外键 recordId
     */
    @Column(name = "recordId")
    private String recordId;
    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getCommentInfo() {
        return commentInfo;
    }

    public void setCommentInfo(String commentInfo) {
        this.commentInfo = commentInfo;
    }

    public String getCommentAuthor() {
        return commentAuthor;
    }

    public void setCommentAuthor(String commentAuthor) {
        this.commentAuthor = commentAuthor;
    }

    public String getCommentAuthorImgUrl() {
        return commentAuthorImgUrl;
    }

    public void setCommentAuthorImgUrl(String commentAuthorImgUrl) {
        this.commentAuthorImgUrl = commentAuthorImgUrl;
    }

    public String getCommentVote() {
        return commentVote;
    }

    public void setCommentVote(String commentVote) {
        this.commentVote = commentVote;
    }

    public String getCommentForMovie() {
        return commentForMovie;
    }

    public void setCommentForMovie(String commentForMovie) {
        this.commentForMovie = commentForMovie;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }


}
