package com.study.util.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import com.study.config.ESTransportClient;

/**
 * es查询lottery数据信息
 * 
 * @author Administrator
 *
 */
public class ESLotteryUtil {

	/** 声明es连接 */
	private static TransportClient esClient = ESTransportClient.getInstance();

	/** 声明全局变量返回容器 */
	public static List<Map<String, Object>> res_list = new ArrayList<Map<String, Object>>();

	/** 声明全局变量返回容器 */
	public static Map<String, Object> res_map = new HashMap<String, Object>();

	/** 查询彩博库用户同步的信息 */
	@SuppressWarnings("unused")
	public static Map<String, Object> findLotteryUserInfo(String user_name) {

		SearchRequestBuilder requestBuilder = esClient.prepareSearch("fk_caipiao").setTypes("lottery_user");
		// 声明where 条件
		BoolQueryBuilder qbs = QueryBuilders.boolQuery();

		// 声明where 条件
		QueryBuilder qb1 = QueryBuilders.termQuery("user_name", user_name);
		BoolQueryBuilder qbs1 = QueryBuilders.boolQuery().must(qb1);
		qbs.must(qbs1);

		requestBuilder.setQuery(qbs);

		SearchResponse response = requestBuilder.execute().actionGet();
		SearchHits hits = response.getHits();
		if (hits.getHits().length > 0) {
			SearchHit hit = hits.getHits()[0];
			Map<String, Object> user_map = hit.getSource();
			String id = StringUtil.nullBlank(user_map.get("ID"));
			String nick_name = StringUtil.nullBlank(user_map.get("NICK_NAME"));
			String user_real_name = StringUtil.nullBlank(user_map.get("USER_REAL_NAME"));
			String user_mobile = StringUtil.nullBlank(user_map.get("USER_MOBILE"));
			String user_idcard = StringUtil.nullBlank(user_map.get("USER_IDCARD"));
			res_map.put("user_id", id);
			res_map.put("user_name", user_name);
			res_map.put("nick_name", nick_name);
			res_map.put("user_real_name", user_real_name);
			res_map.put("user_mobile", user_mobile);
			res_map.put("user_idcard", user_idcard);
		}
		return res_map;
	}

}
