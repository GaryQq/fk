package com.fengkuang.author.service;

import com.fengkuang.author.bean.Author;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface AuthorService {

    Author getAuthorByUserName(String userName);

    int saveAuthorApply(Author author);

    Map<String, Object> authorSummary(String userName);

    PageInfo<Map<String, Object>> incomeListByPage(String userName, int pageNum, int pageSize);

    String authorTotalIncome(String userName, String month);

    int getAuthorNameCount(Author author);


}
