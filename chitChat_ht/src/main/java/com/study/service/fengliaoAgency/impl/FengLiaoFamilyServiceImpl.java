package com.study.service.fengliaoAgency.impl;

import com.github.pagehelper.PageHelper;
import com.study.config.ESTransportClient;
import com.study.mapper.fengliaoAgency.FengLiaoFamilyMapper;
import com.study.mapper.lotterytie.LotterytieMapper;
import com.study.service.fengliaoAgency.FengLiaoFamilyService;
import com.study.util.bean.PageBean;
import com.study.util.esUtil.ESClientUtil;
import com.study.util.permissions.PageBeanUtil;
import com.study.util.tool.StringUtil;
import com.sun.javafx.binding.StringFormatter;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("fengLiaoFamilyService")
public class FengLiaoFamilyServiceImpl implements FengLiaoFamilyService {

    private static Logger logger = LoggerFactory.getLogger(FengLiaoFamilyServiceImpl.class);

    //ES连接声明
    private static TransportClient esClient = ESTransportClient.getInstance();

    //白名单用户集合
    private List<String> user_list = new ArrayList<String>();
    private List<Map<String, Object>> res_list;
    @Autowired
    private FengLiaoFamilyMapper fengLiaoFamilyMapper;

    @Autowired
    private LotterytieMapper lotterytieMapper;


    @Override
    public List<Map<String, Object>> findCurrentDetailOneToOneList(Map<String, Object> param, PageBean bean) {
        if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
            PageHelper.startPage(bean.getPage(), bean.getRows());
        }
        res_list = fengLiaoFamilyMapper.findCurrentDetailOneToOneList(param);
        return res_list;
    }

    @Override
    public List<Map<String, Object>> findFamilyOneToOneDetailList(Map<String, Object> param, PageBean bean) {
        res_list = fengLiaoFamilyMapper.findCurrentDetailOneToOneList(param);
        return res_list;
    }

    @Override
    public List<Map<String, Object>> findFamilyOrderDetailList(Map<String, Object> param, PageBean bean) {
        if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
            PageHelper.startPage(bean.getPage(), bean.getRows());
        }
        res_list = fengLiaoFamilyMapper.findFamilyOrderDetailList(param);
        return res_list;
    }

    @Override
    public List<Map<String, Object>> findSkillList(Map<String, Object> param) {
        res_list = fengLiaoFamilyMapper.findSkillList(param);
        return res_list;
    }

}
