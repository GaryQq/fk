package com.fengkuang.author.service;

import com.fengkuang.author.bean.Article;
import com.fengkuang.author.bean.Author;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface ArticleService {

    Map<String, Object> insertArticle(Article article);

    List<Map<String, Object>> articleList(Author author);

    Article getArticle(String postId);

    Article getArticle(Long id);

    int deleteArticle(String postId);

    PageInfo<Map<String, Object>> articleListByPage(Author author, int pageNum, int pageSize, int status, String month);
    PageInfo<Map<String, Object>> noticeListByPage(int pageNum, int pageSize);

    List<String> getMonthesList(String userName, int status);

}
