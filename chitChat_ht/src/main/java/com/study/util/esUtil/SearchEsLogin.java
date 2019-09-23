package com.study.util.esUtil;

import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
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

public class SearchEsLogin {
	private static TransportClient client = ESTransportClient.getInstance();

	/**
	 * 根据登陆app类型查询用户token
	 * 
	 * @param user_name
	 * @return
	 */
	public Map<String, Object> findUserToken(String user_id, String app_type) {
		SearchRequestBuilder requestBuilder = client.prepareSearch("fk_user_login_detail")
				.setTypes("user_login_detail");

		// 声明where 条件
		BoolQueryBuilder qbs = QueryBuilders.boolQuery();

		// 声明where 条件
		QueryBuilder qb1 = QueryBuilders.termsQuery("user_id", user_id);
		BoolQueryBuilder qbs1 = QueryBuilders.boolQuery().must(qb1);
		qbs.must(qbs1);

		QueryBuilder qb2 = QueryBuilders.existsQuery("device_token");
		BoolQueryBuilder qbs2 = QueryBuilders.boolQuery().must(qb2);
		qbs.must(qbs2);

		QueryBuilder qb3 = QueryBuilders.termQuery("app_type", app_type);
		BoolQueryBuilder qbs3 = QueryBuilders.boolQuery().must(qb3);
		qbs.must(qbs3);

		requestBuilder.setQuery(qbs);

		SortBuilder sortBuilder = SortBuilders.fieldSort("create_date").order(SortOrder.DESC);
		requestBuilder.addSort(sortBuilder);

		SearchResponse response = requestBuilder.execute().actionGet();
		SearchHits hits = response.getHits();
		if (hits.getHits().length > 0) {
			SearchHit hit = hits.getHits()[0];
			GetResponse res_token = client.prepareGet(hit.getIndex(), hit.getType(), hit.getId())
					.setOperationThreaded(true) // 线程安全
					.get();
			System.out.println(res_token.getSourceAsString());
			return res_token.getSourceAsMap();
		} else {
		}
		return null;
	}

}
