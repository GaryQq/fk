package com.fengkuang.author.dao;

import com.fengkuang.author.bean.Article;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ArticleDao extends GenericDao<Article> {

    List<Map<String, Object>> getListByParams(Map<String, Object> params);

    List<Map<String, Object>> getList(Map<String, Object> params);

    Article getDetailByPostId(String postId);

    int updateByPostId(Map<String, Object> params);

    List<Map<String, Object>> getAuthorIncomeDetail(Map<String, Object> params);

    List<String> getArticleMonthes(Map<String, Object> params);
}
