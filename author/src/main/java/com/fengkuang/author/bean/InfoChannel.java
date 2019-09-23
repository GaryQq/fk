package com.fengkuang.author.bean;

public class InfoChannel {

    private Long postId;
    private Long channelId;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public InfoChannel(Long postId, Long channelId) {
        this.postId = postId;
        this.channelId = channelId;
    }
}
