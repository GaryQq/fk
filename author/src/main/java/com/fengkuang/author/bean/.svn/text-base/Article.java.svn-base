package com.fengkuang.author.bean;

import java.util.Date;

/**
 * @author zhaichong
 * 文章bean
 */
public class Article {

    private Long id;
    private String userName;
    private Author author;
    private String title;
    private Date createTime;
    private Date modifyTime;
    private Integer status;
    private String content;
    private String postId;
    private Integer channelId;
    /**
     * 是否收费：1、收费；0、免费
     */
    private Integer payType;
    private Integer wordCount;
    /**
     * 封面图URL
     */
    private String imageUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Article(Long id, Integer status, String postId) {
        this.id = id;
        this.status = status;
        this.postId = postId;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", author=" + author +
                ", title='" + title + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", status=" + status +
                ", content='" + content + '\'' +
                ", postId='" + postId + '\'' +
                ", channelId=" + channelId +
                ", payType=" + payType +
                ", wordCount=" + wordCount +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public Article(Long id, String userName, String title, Date createTime, Date modifyTime, Integer status, String content, String postId, Integer channelId, Integer payType, Integer wordCount) {
        this.id = id;
        this.userName = userName;
        this.title = title;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.status = status;
        this.content = content;
        this.postId = postId;
        this.channelId = channelId;
        this.payType = payType;
        this.wordCount = wordCount;
    }

    public Article(Long id, String userName, Author author, String title, Date createTime, Date modifyTime, Integer status, String content, String postId, Integer channelId, Integer payType, Integer wordCount, String imageUrl) {
        this.id = id;
        this.userName = userName;
        this.author = author;
        this.title = title;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.status = status;
        this.content = content;
        this.postId = postId;
        this.channelId = channelId;
        this.payType = payType;
        this.wordCount = wordCount;
        this.imageUrl = imageUrl;
    }

    public Article() {
    }
}
