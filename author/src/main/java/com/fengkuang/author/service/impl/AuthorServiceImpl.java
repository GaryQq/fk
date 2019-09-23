package com.fengkuang.author.service.impl;

import com.fengkuang.author.bean.Article;
import com.fengkuang.author.bean.Author;
import com.fengkuang.author.dao.ArticleDao;
import com.fengkuang.author.dao.AuthorDao;
import com.fengkuang.author.dao.VipOrderDao;
import com.fengkuang.author.service.AuthorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Resource
    private AuthorDao authorDao;

    @Resource
    private ArticleDao articleDao;

    @Override
    public Author getAuthorByUserName(String userName) {
        return authorDao.getByUserName(userName);
    }

    @Override
    public int saveAuthorApply(Author author) {
        return authorDao.insert(author);
    }

    @Override
    public Map<String, Object> authorSummary(String userName) {
        return authorDao.authorSummary(userName);
    }

    @Override
    public PageInfo<Map<String, Object>> incomeListByPage(String userName, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Map<String, Object> params = new HashMap<>(4);
        params.put("userName", userName);
        List<Map<String, Object>> articleList = articleDao.getAuthorIncomeDetail(params);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(articleList);
        return pageInfo;
    }

    @Override
    public String authorTotalIncome(String userName, String month) {
        Map<String, Object> params = new HashMap<>(4);
        Long startTime = 0L;
        Long endTime = 99999999999999L;
        month = month.replaceAll("\\D", "");
        if (StringUtils.isNotBlank(month)) {
            startTime = Long.valueOf(month + "00000000");
            endTime = Long.valueOf(month + "99999999");
        }
        params.put("userName", userName);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        Map<String, Object> result = authorDao.totalIncome(params);
        Double deductMoney = MapUtils.getDouble(result, "DEDUCT_MONEY", 0.00d);
        return new DecimalFormat("###.##").format(deductMoney);
    }

    @Override
    public int getAuthorNameCount(Author author) {
        return authorDao.getAuthorNameCount(author);
    }
}
