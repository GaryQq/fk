package com.fengkuang.author.bean;

public class InfoPost {

    private Long id;
    private Long channelId;
    private String postId;
    private String title;
    private String pic;
    private Integer flag;
    private String lastId;
    private String userName;
    private String nickName;
    private Integer type;
    private String content;
    private Integer status;
    private Integer imageFlag;
    private Integer free;
    private Integer wordCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getLastId() {
        return lastId;
    }

    public void setLastId(String lastId) {
        this.lastId = lastId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getImageFlag() {
        return imageFlag;
    }

    public void setImageFlag(Integer imageFlag) {
        this.imageFlag = imageFlag;
    }

    public Integer getFree() {
        return free;
    }

    public void setFree(Integer free) {
        this.free = free;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public InfoPost(Long channelId, String postId, String title, String pic, Integer flag, String lastId, String userName, String nickName, Integer type, String content, Integer status, Integer imageFlag, Integer free, Integer wordCount) {
        this.channelId = channelId;
        this.postId = postId;
        this.title = title;
        this.pic = pic;
        this.flag = flag;
        this.lastId = lastId;
        this.userName = userName;
        this.nickName = nickName;
        this.type = type;
        this.content = content;
        this.status = status;
        this.imageFlag = imageFlag;
        this.free = free;
        this.wordCount = wordCount;
    }
}
