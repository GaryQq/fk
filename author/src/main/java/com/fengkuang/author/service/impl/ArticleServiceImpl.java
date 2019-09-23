package com.fengkuang.author.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengkuang.author.Constants;
import com.fengkuang.author.bean.*;
import com.fengkuang.author.dao.ArticleDao;
import com.fengkuang.author.dao.AuthorDao;
import com.fengkuang.author.dao.InfoChannelDao;
import com.fengkuang.author.dao.InfoPostDao;
import com.fengkuang.author.enums.ResultCode;
import com.fengkuang.author.service.ArticleService;
import com.fengkuang.author.util.CommonUtil;
import com.fengkuang.author.util.HttpRequestProxy;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Service
public class ArticleServiceImpl implements ArticleService {

    private Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Resource
    private ArticleDao articleDao;

    @Resource
    private AuthorDao authorDao;

    @Resource
    private InfoPostDao infoPostDao;

    @Resource
    private InfoChannelDao infoChannelDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> insertArticle(Article article) {

        Map<String, Object> result;

        String title = article.getTitle();
        Integer channelId = article.getChannelId();
        String content = article.getContent();
        Integer payType = article.getPayType();

        if (StringUtils.isBlank(title)) {
            result = CommonUtil.makeResult(ResultCode.CODE_0002);
            return result;
        } else if (title.length() > Constants.TITLE_MAX_LENGTH) {
            result = CommonUtil.makeResult(ResultCode.CODE_0003);
            return result;
        } else if (StringUtils.isBlank(content)) {
            result = CommonUtil.makeResult(ResultCode.CODE_0004);
            return result;
        } else if (null == channelId || 0 == channelId) {
            result = CommonUtil.makeResult(ResultCode.CODE_0005);
            return result;
        } else if (null == payType) {
            result = CommonUtil.makeResult(ResultCode.CODE_0006);
            return result;
        }

        // 获取作者对象
        Author author = authorDao.getByUserName(article.getUserName());

        if (null == author) {
            result = CommonUtil.makeResult(ResultCode.CODE_0007);
            return result;
        }

        Integer status = author.getStatus();
        if (1 != status) {
            result = CommonUtil.makeResult(ResultCode.CODE_0008);
            return result;
        }

        int saveCount;

        /**
         * newFlag 标识是否是新插入文章，如果jsp传参包含postId，说明是已经同步到CMS的文章，进行更新操作，否则插入
         */
        boolean newFlag = StringUtils.isBlank(article.getPostId()) ? true : false;
        if (null != article.getId()) {
            // 更新
            saveCount = articleDao.update(article);
        } else {
            // 插入
            saveCount = articleDao.insert(article);
        }

        if (saveCount <= 0) {
            logger.error("文章入库失败，Article=" + article.toString());
            result = CommonUtil.makeResult(ResultCode.CODE_9999);
            return result;
        } else {
            if (article.getStatus() == 1) {
                if (newFlag) {
                    // 新插入文章
                    // 同步到CMS并更新数据库
                    Map<String, String> paramMap = new HashMap<>(16);
                    paramMap.put("ChannelID", "14181");
                    paramMap.put("Title", article.getTitle());//标题
                    paramMap.put("nickname", author.getAuthorName());//用户昵称
                    paramMap.put("categoryid", article.getChannelId() + "");//频道
                    paramMap.put("sourceid", System.currentTimeMillis() + "");//帖子编号
                    paramMap.put("Summary", "");//摘要
                    paramMap.put("Token", "TKOSUELQOELF6534");//
                    paramMap.put("Content", article.getContent());//内容简介
                    paramMap.put("documenttype", "1");//文章类型：1、文章；2、图集；3、视频
                    paramMap.put("isquick", "1");//是否快讯 1：否；2：是
                    paramMap.put("video", "");//视频地址
                    paramMap.put("duration", "0");//视频时长
                    paramMap.put("pictype", "1");//标题图片类型 1、单图；3、无图
                    paramMap.put("Photo", article.getImageUrl());//封面地址
                    paramMap.put("PublishDate", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间

                    logger.info("同步文章到CMS传参:" + paramMap);
                    String res = HttpRequestProxy.doGet(Constants.CMS_SYN_URL, paramMap, "utf-8", "utf-8");
                    logger.info("同步文章到CMS返回内容：" + res);
                    if (StringUtils.isNotBlank(res)) {
                        JSONObject jsonObject = JSON.parseObject(res);
                        String sta = jsonObject.getString("status");
                        String globalid = jsonObject.getString("globalid");
                        String href = jsonObject.getString("href");
                        if (StringUtils.equals("1", sta)) {
                            // 更新文章表
                            Article updateArticle = new Article(article.getId(), 1, globalid);
                            int updateCount = articleDao.update(updateArticle);
                            if (updateCount != 1) {
                                logger.error("修改文章失败，Article=" + article.toString());
                                result = CommonUtil.makeResult(ResultCode.CODE_9999);
                                return result;
                            }

                            // 成功后插入info_post表和关联表
                            InfoPost post = new InfoPost(Long.valueOf(channelId), globalid, title, article.getImageUrl(), 0, globalid, author.getUserName(), author.getAuthorName(), -1, href, 1, StringUtils.isNotBlank(article.getImageUrl()) ? 1 : 3, payType == 0 ? 1 : 0, article.getWordCount());
                            int insertCount = infoPostDao.insert(post);
                            if (insertCount != 1) {
                                logger.error("插入INFO_POST失败，Article=" + article.toString());
                                throw new RuntimeException();
                            }
                            Long id = post.getId();
                            InfoChannel infoChannel = new InfoChannel(id, Long.valueOf(article.getChannelId()));
                            int insert = infoChannelDao.insert(infoChannel);
                            if (insert != 1) {
                                logger.error("插入INFO_CHANNEL_RELEVANCE失败，Article=" + article.toString());
                                throw new RuntimeException();
                            }

                        } else {
                            logger.error("文章同步到CMS失败！！！");
                            result = CommonUtil.makeResult(ResultCode.CODE_9999);
                            return result;
                        }
                    } else {
                        logger.error("文章同步到CMS返回空！！！");
                        result = CommonUtil.makeResult(ResultCode.CODE_9999);
                        return result;
                    }
                } else {
                    // 修改已发布的文章
                    Map<String, String> paramMap = new HashMap<>(16);
                    paramMap.put("ChannelID", "14181");
                    paramMap.put("Title", article.getTitle());//标题
                    paramMap.put("categoryid", article.getChannelId() + "");//频道
                    paramMap.put("GlobalID", article.getPostId());//帖子编号
                    paramMap.put("Token", "TKOSUELQOELF6534");//
                    paramMap.put("Photo", article.getImageUrl());
                    paramMap.put("Content", article.getContent());//内容

                    logger.info("修改文章内容传参:" + paramMap);
                    String res = HttpRequestProxy.doGet(Constants.CMS_UPDATE_URL, paramMap, "utf-8", "utf-8");
                    logger.info("修改文章内容返回内容：" + res);
                    if (StringUtils.isNotBlank(res)) {
                        JSONObject jsonObject = JSON.parseObject(res);
                        String sta = jsonObject.getString("status");
                        if (StringUtils.equals("1", sta)) {
                            // 修改info_post表
                            Map<String, Object> param = new HashMap<>(6);
                            param.put("channelId", channelId);
                            param.put("free", payType == 0 ? 1 : 0);
                            param.put("title", title);
                            param.put("wordCount", article.getWordCount());
                            param.put("postId", article.getPostId());
                            param.put("pic", article.getImageUrl());

                            int updateCount = infoPostDao.updateByPostId(param);
                            if (updateCount != 1) {
                                logger.error("更新INFO_POST失败，Article=" + article.toString());
                                throw new RuntimeException();
                            }

                            // 修改关联表
                            int upCount = infoChannelDao.updateByPostId(param);
                            if (upCount != 1) {
                                logger.error("更新INFO_CHANNEL_RELEVANCE失败，Article=" + article.toString());
                                throw new RuntimeException();
                            }
                        }
                    } else {
                        logger.error("修改文章内容返回空！！！");
                        result = CommonUtil.makeResult(ResultCode.CODE_9999);
                        return result;
                    }
                }
            }
        }
        result = CommonUtil.makeResult(ResultCode.CODE_0000, article);
        return result;
    }

    @Override
    public List<Map<String, Object>> articleList(Author author) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("userName", author.getUserName());
        params.put("status", 1);
        List<Map<String, Object>> articleList = articleDao.getListByParams(params);
        return articleList;
    }

    @Override
    public Article getArticle(String postId) {
        return articleDao.getDetailByPostId(postId);
    }

    @Override
    public Article getArticle(Long id) {
        return articleDao.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteArticle(String postId) {
        Map<String, Object> paramMap = new HashMap<>(2);
        paramMap.put("postId", postId);
        paramMap.put("status", -2);

        int count1 = infoPostDao.updateByPostId(paramMap);
        int count2 = articleDao.updateByPostId(paramMap);

        if (count1 * count2 != 1) {
            throw new RuntimeException();
        }
        return 1;
    }

    @Override
    public PageInfo<Map<String, Object>> articleListByPage(Author author, int pageNum, int pageSize, int status, String month) {
        PageHelper.startPage(pageNum, pageSize);
        Map<String, Object> params = new HashMap<>(4);
        Long startTime = 0L;
        Long endTime = 99999999999999L;
        month = month.replaceAll("\\D", "");
        if (StringUtils.isNotBlank(month)) {
            startTime = Long.valueOf(month + "00000000");
            endTime = Long.valueOf(month + "99999999");
        }

        params.put("userName", author.getUserName());
        params.put("status", status);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        List<Map<String, Object>> articleList;
        if (0 == status) {
            articleList = articleDao.getList(params);
        } else {
            articleList = articleDao.getListByParams(params);
        }
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(articleList);
        return pageInfo;
    }

    @Override
    public List<String> getMonthesList(String userName, int status) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("userName", userName);
        params.put("status", status);
        return articleDao.getArticleMonthes(params);
    }

    @Override
    public PageInfo<Map<String, Object>> noticeListByPage(int pageNum, int pageSize) {
        List<Map<String, Object>> noticeList = new ArrayList<>();
        Map<String, Object> notice = new HashMap<>(6);
        notice.put("title", "疯狂号自律组织公约");
        notice.put("createTime", "2019-01-25 12:10:00");
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(noticeList);
        return pageInfo;
    }
}
