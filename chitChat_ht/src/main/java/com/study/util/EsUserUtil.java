package com.study.util;

import com.study.config.ESTransportClient;
import com.study.util.tool.PortraitImgUtil;
import com.study.util.tool.StringUtil;

import org.elasticsearch.action.delete.DeleteResponse;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: Dell Date: 18-7-18 Time: 下午4:11 To change
 * this template use File | Settings | File Templates.
 */
public class EsUserUtil {
	private static TransportClient esClient = ESTransportClient.getInstance();

	public static Map<String, Object> getUserInfo(String userId) {
		TransportClient client = ESTransportClient.getInstance();
		SearchRequestBuilder requestBuilder = client.prepareSearch("fk_user_login_detail")
				.setTypes("user_login_detail");

		// 声明where 条件
		BoolQueryBuilder qbs = QueryBuilders.boolQuery();

		// 声明where 条件
		QueryBuilder qb1 = QueryBuilders.termsQuery("user_id", userId);
		BoolQueryBuilder qbs1 = QueryBuilders.boolQuery().must(qb1);
		qbs.must(qbs1);

		requestBuilder.setQuery(qbs);

		SortBuilder sortBuilder = SortBuilders.fieldSort("create_date").order(SortOrder.DESC);
		requestBuilder.addSort(sortBuilder);

		SearchResponse response = requestBuilder.execute().actionGet();
		SearchHits hits = response.getHits();
		if (hits.getHits().length > 0) {
			SearchHit hit = hits.getHits()[0];
			return hit.getSource();
		} else {
		}
		return null;
	}

	public static void main(String[] args) {
		EsUserUtil es = new EsUserUtil();
		Map<String, Object> es_map = es.getUserInfo("14795859");
		System.out.println(StringUtil.nullBlank(es_map).equals(""));
	}

	/**
	 * 根据唯一标识删除
	 *
	 * @param index
	 *            索引
	 * @param type
	 *            类型
	 * @param id
	 *            唯一标识
	 * @return index 索引 type 类型 source_id 修改数据唯一标识 version 当前数据版本号 success
	 *         成功失败标识
	 */
	public static Map<String, Object> deleteDocumentEntity(String index, String type, String id) {
		DeleteResponse response = esClient.prepareDelete(index, type, id).get();
		Map<String, Object> res_map = new HashMap<String, Object>();
		res_map.put("index", response.getIndex());
		res_map.put("type", response.getType());
		res_map.put("source_id", response.getId());
		res_map.put("version", response.getVersion());
		// res_map.put("success", response.isFound());
		return res_map;
	}

	/**
	 * 查询用户信息
	 * 
	 * @param param
	 *            nick_name:用户昵称 user_name:用户账号
	 * @return
	 */
	public static Map<String, Object> findUserEntity(Map<String, Object> param) {
		String nick_name = StringUtil.nullBlank(param.get("nick_name"));
		String user_name = StringUtil.nullBlank(param.get("user_name"));
		// 声明where 条件
		BoolQueryBuilder qbs = QueryBuilders.boolQuery();
		if (!StringUtil.eqBlank(nick_name)) {
			QueryBuilder qb1 = QueryBuilders.matchPhraseQuery("NICK_NAME", nick_name);
			BoolQueryBuilder qbs1 = QueryBuilders.boolQuery().must(qb1);
			qbs.must(qbs1);
		}

		if (!StringUtil.eqBlank(user_name)) {
			QueryBuilder qb2 = QueryBuilders.matchPhraseQuery("USER_NAME", user_name);
			BoolQueryBuilder qbs2 = QueryBuilders.boolQuery().must(qb2);
			qbs.must(qbs2);
		}

		SearchRequestBuilder requestBuilder = esClient.prepareSearch("fk_caibo").setTypes("yt_user");
		requestBuilder.setQuery(qbs);
		SearchResponse response = requestBuilder.execute().actionGet();
		SearchHits hits = response.getHits();
		if (hits.getHits().length > 0) {
			SearchHit hit = hits.getHits()[0];
			Map<String, Object> map = hit.getSource();
			String sex = StringUtil.nullBlank(map.get("SEX"));
			String user_id = StringUtil.nullBlank(map.get("ID"));
			String userName = StringUtil.nullBlank(map.get("USER_NAME"));
			String nickName = StringUtil.nullBlank(map.get("NICK_NAME"));
			String image = StringUtil.nullBlank(map.get("MID_IMAGE"));
			String type = StringUtil.nullBlank(map.get("TYPE"));
			String user_img = PortraitImgUtil.getPortraitImgUtil(image, type);
			Map<String, Object> res_map = new HashMap<String, Object>();
			res_map.put("user_id", user_id);
			res_map.put("sex", sex);
			res_map.put("user_name", userName);
			res_map.put("nick_name", nickName);
			res_map.put("user_img", user_img);
			return res_map;
		}
		return null;
	}

}
