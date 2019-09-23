package com.fengkuang.author.dao;

import com.fengkuang.author.bean.Author;

import java.util.Map;

public interface AuthorDao extends GenericDao<Author> {

    Map<String, Object> authorSummary(String userName);

    Map<String, Object> totalIncome(Map<String, Object> params);

    int getAuthorNameCount(Author author);

}
