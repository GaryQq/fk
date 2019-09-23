package com.study.controller.management;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageInfo;
import com.study.config.ESTransportClient;
import com.study.util.bean.DataGridResultInfo;
import com.study.util.bean.PageBean;
import com.study.util.esUtil.GroupBy;
import com.study.util.permissions.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "UserManage", description = "userManage管理API")
@RestController
@RequestMapping("/idfa")
public class IdfaController {
    private static Logger logger = LoggerFactory.getLogger(IdfaController.class);

    private static TransportClient esClient = ESTransportClient.getInstance();

    @ApiOperation(value = "idfa管理", notes = "idfa管理API")
    @RequestMapping(value = "/idfaStastic", method = {RequestMethod.GET})
    public DataGridResultInfo getTeacherList(HttpServletRequest request, @ModelAttribute PageBean bean) {
        if (bean.getPage() == null) {
            bean.setPage(1);
        }
        if (bean.getRows() == null) {
            bean.setRows(15);
        }
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
            startTime = startTime + " 00:00:00";
            endTime = endTime + " 23:59:59";
        }
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            SearchRequestBuilder requestBuilder = esClient.prepareSearch("fk_idfa")
                    .setTypes("idfa");
            // 声明where 条件
            BoolQueryBuilder qbs = QueryBuilders.boolQuery();
            requestBuilder.setQuery(qbs);
            GroupBy groupBy = new GroupBy(requestBuilder, "source_group_1", "source", true);
            Map<String, Object> groupbyResponse = groupBy.getGroupbyResponse();
            for (Map.Entry<String, Object> entry : groupbyResponse.entrySet()) {
                String bucketKey = entry.getKey();
                logger.info("idfa统计,source:" + bucketKey);
                Map<String, Object> stasticMap = new HashMap<>();
                String sourceName = "";
                if ("zshd".equals(bucketKey)) {
                    sourceName = "掌上互动";
                } else if ("qlaso".equals(bucketKey)) {
                    sourceName = "青绿aso";
                } else if ("jzasm".equals(bucketKey)) {
                    sourceName = "巨掌asm";
                } else if ("qmjfq".equals(bucketKey)) {
                    sourceName = "钱脉积分墙";
                } else {
                    sourceName = bucketKey;
                }
                long totalCount = getIdfaTotalCount(bucketKey, startTime, endTime);
                long activateCount = getIdfaActivateCount(bucketKey, startTime, endTime);
                long unActivateCount = totalCount - activateCount;
                stasticMap.put("sourceName", sourceName);
                stasticMap.put("totalCount", totalCount);
                stasticMap.put("activateCount", activateCount);
                stasticMap.put("unActivateCount", unActivateCount);
                list.add(stasticMap);
            }
        } catch (Exception e) {
            logger.error("idfa统计异常", e);
        }
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
        return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
    }

    //查询总数
    private long getIdfaTotalCount(String source, String start_time, String end_time) {
        SearchRequestBuilder requestBuilder = esClient.prepareSearch("fk_idfa")
                .setTypes("idfa");
        // 声明where 条件
        BoolQueryBuilder qbs = QueryBuilders.boolQuery();
        QueryBuilder qb1 = QueryBuilders.termQuery("source", source);
        QueryBuilder qb2 = QueryBuilders.rangeQuery("create_date").from(start_time).to(end_time).includeLower(true)
                .includeUpper(true);
        BoolQueryBuilder qbs1 = null;
        if (!StringUtils.isEmpty(start_time) && !StringUtils.isEmpty(end_time)) {
            qbs1 = QueryBuilders.boolQuery().must(qb1).must(qb2);
        } else {
            qbs1 = QueryBuilders.boolQuery().must(qb1);
        }

        qbs.must(qbs1);
        requestBuilder.setQuery(qbs);
        SearchResponse response = requestBuilder.execute().actionGet();
        SearchHits hits = response.getHits();
        return hits.getTotalHits();
    }

    //查询激活数
    private long getIdfaActivateCount(String source, String start_time, String end_time) {
        SearchRequestBuilder requestBuilder = esClient.prepareSearch("fk_idfa")
                .setTypes("idfa");
        // 声明where 条件
        BoolQueryBuilder qbs = QueryBuilders.boolQuery();
        QueryBuilder qb1 = QueryBuilders.termQuery("source", source);
        QueryBuilder qb2 = QueryBuilders.termQuery("status", 1);
        QueryBuilder qb3 = QueryBuilders.rangeQuery("create_date").from(start_time).to(end_time).includeLower(true)
                .includeUpper(true);
        BoolQueryBuilder qbs1 = null;
        if (!StringUtils.isEmpty(start_time) && !StringUtils.isEmpty(end_time)) {
            qbs1 = QueryBuilders.boolQuery().must(qb1).must(qb2).must(qb3);
        } else {
            qbs1 = QueryBuilders.boolQuery().must(qb1).must(qb2);
        }
        qbs.must(qbs1);
        requestBuilder.setQuery(qbs);
        SearchResponse response = requestBuilder.execute().actionGet();
        SearchHits hits = response.getHits();
        return hits.getTotalHits();
    }


}
