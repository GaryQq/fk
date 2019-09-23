package com.study.service.dailydata.impl;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.BooleanLiteral;
import org.springframework.stereotype.Service;

import com.study.config.ESTransportClient;
import com.study.mapper.dailydata.DailyDataMapper;
import com.study.service.dailydata.DailyDataService;
import com.study.util.bean.PageBean;
import com.study.util.tool.BigDecimalUtil;
import com.study.util.tool.StringUtil;

import io.swagger.models.auth.In;

@Service("dailyDataService")
public class DailyDataServiceImpl implements DailyDataService {
	@Autowired
	private DailyDataMapper dailyDataMapper;

	private static Logger logger = LoggerFactory.getLogger(DailyDataServiceImpl.class);

	private static TransportClient esClient = ESTransportClient.getInstance();

	@Override
	public long findOneToOneDayTotal(Map<String, Object> param) {
		String start_time = StringUtil.nullBlank(param.get("start_time"));
		String end_time = StringUtil.nullBlank(param.get("end_time"));
		String status = StringUtil.nullBlank(param.get("status"));
		try {
			// 声明where 条件
			BoolQueryBuilder qbs = QueryBuilders.boolQuery();

			// 根据时间条件查询
			QueryBuilder qb1 = QueryBuilders.rangeQuery("create_date").from(start_time).to(end_time).includeLower(true)
					.includeUpper(true);
			BoolQueryBuilder qbs1 = QueryBuilders.boolQuery().must(qb1);
			qbs.must(qbs1);

			// 是否过滤白名单
			if ("1".equals(status)) {
				QueryBuilder qb2 = QueryBuilders.termQuery("type", "yuban_1");
				BoolQueryBuilder qbs2 = QueryBuilders.boolQuery().must(qb2);
				qbs.must(qbs2);
			} else if ("2".equals(status)) {
				QueryBuilder qb2 = QueryBuilders.termQuery("type", "yuban_2");
				BoolQueryBuilder qbs2 = QueryBuilders.boolQuery().must(qb2);
				qbs.must(qbs2);
			}
			SearchRequestBuilder requestBuilder = esClient.prepareSearch("zb_ht_onetoone_date")
					.setTypes("ht_onetoone_date");
			requestBuilder.setQuery(qbs);
			SearchResponse response = requestBuilder.execute().actionGet();
			return response.getHits().getTotalHits();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<Map<String, Object>> findOneToOneDay(Map<String, Object> param, PageBean bean) {
		Integer page = bean.getPage();
		Integer rows = bean.getRows();
		String start_time = StringUtil.nullBlank(param.get("start_time"));
		String end_time = StringUtil.nullBlank(param.get("end_time"));
		String status = StringUtil.nullBlank(param.get("status"));
		List<Map<String, Object>> res_list = new ArrayList<Map<String, Object>>();

		try {

			// 声明where 条件
			BoolQueryBuilder qbs = QueryBuilders.boolQuery();

			// 根据时间条件查询
			QueryBuilder qb1 = QueryBuilders.rangeQuery("create_date").from(start_time).to(end_time).includeLower(true)
					.includeUpper(true);
			BoolQueryBuilder qbs1 = QueryBuilders.boolQuery().must(qb1);
			qbs.must(qbs1);

			// 是否过滤白名单
			if ("1".equals(status)) {
				QueryBuilder qb2 = QueryBuilders.termQuery("type", "yuban_1");
				BoolQueryBuilder qbs2 = QueryBuilders.boolQuery().must(qb2);
				qbs.must(qbs2);
			} else if ("2".equals(status)) {
				QueryBuilder qb2 = QueryBuilders.termQuery("type", "yuban_2");
				BoolQueryBuilder qbs2 = QueryBuilders.boolQuery().must(qb2);
				qbs.must(qbs2);
			}
			SearchRequestBuilder requestBuilder = esClient.prepareSearch("zb_ht_onetoone_date")
					.setTypes("ht_onetoone_date");
			requestBuilder.setQuery(qbs);

			SortBuilder date_sort = SortBuilders.fieldSort("create_date").order(SortOrder.DESC);
			requestBuilder.addSort(date_sort);

			SearchResponse response = requestBuilder.setFrom((page - 1) * rows).setSize(rows).execute().actionGet();
			SearchHits hits = response.getHits();
			if (hits.getHits().length > 0) {
				for (int i = 0; i < hits.getHits().length; i++) {
					SearchHit hit = hits.getHits()[i];
					Map<String, Object> val = hit.getSource();
					String create_date = StringUtil.nullBlank(val.get("create_date"));
					String long_time = StringUtil.nullBlank(val.get("long_time"));// 接通时长
					String fq_voice_num = StringUtil.nullBlank(val.get("fq_voice_num"));// 发起音频次数
					String fq_video_num = StringUtil.nullBlank(val.get("fq_video_num"));// 发起视频次数
					String jt_voice_num = StringUtil.nullBlank(val.get("jt_voice_num"));// 音频接通次数
					String jt_video_num = StringUtil.nullBlank(val.get("jt_video_num"));// 视频接通次数
					String jtl_video_percent = StringUtil.nullBlank(val.get("jtl_video_percent"));// 视频接通率
					String jtl_voice_percent = StringUtil.nullBlank(val.get("jtl_voice_percent"));// 音频接通率
					String jj_video_percent = StringUtil.nullBlank(val.get("jj_video_percent"));// 视频拒接率
					String jj_voice_percent = StringUtil.nullBlank(val.get("jj_voice_percent"));// 音频拒接率
					String fq_video_user = StringUtil.nullBlank(val.get("fq_video_user"));// 发起视频人数
					String fq_voice_user = StringUtil.nullBlank(val.get("fq_voice_user"));// 发起音频人数
					String jt_video_user = StringUtil.nullBlank(val.get("jt_video_user"));// 视频接通人数
					String jt_voice_user = StringUtil.nullBlank(val.get("jt_voice_user"));// 音频接通人数
					String onetoone_user = StringUtil.nullBlank(val.get("onetoone_user"));// 参与用户数
					String gift_amount = StringUtil.nullBlank(val.get("gift_amount"));// 礼物消耗金币
					String video_amount = StringUtil.nullBlank(val.get("video_amount"));// 视频消耗金币
					String voice_amount = StringUtil.nullBlank(val.get("voice_amount"));// 音频消耗金币
					String msg_amount = StringUtil.nullBlank(val.get("msg_amount"));// 私信消耗金币
					double amount = BigDecimalUtil.add(StringUtil.nullDouble(gift_amount),
							StringUtil.nullDouble(video_amount));// 消耗总金币数
					amount = BigDecimalUtil.add(StringUtil.nullDouble(amount), StringUtil.nullDouble(voice_amount));
					amount = BigDecimalUtil.add(StringUtil.nullDouble(amount), StringUtil.nullDouble(msg_amount));
					long video_valid = this.findOneToOneDayValid(create_date, 1);// 查询视频有效发起次数
					long voice_valid = this.findOneToOneDayValid(create_date, 2);// 查询语音有效发起次数
					Map<String, Object> res_map = new HashMap<String, Object>();
					res_map.put("create_date", create_date);
					res_map.put("long_time", long_time);
					res_map.put("fq_voice_num", fq_voice_num);
					res_map.put("fq_video_num", fq_video_num);
					res_map.put("jt_voice_num", jt_voice_num);
					res_map.put("jt_video_num", jt_video_num);
					res_map.put("jtl_video_percent", String.format("%.2f", StringUtil.nullDouble(jtl_video_percent)));
					res_map.put("jtl_voice_percent", String.format("%.2f", StringUtil.nullDouble(jtl_voice_percent)));
					res_map.put("jj_video_percent", String.format("%.2f", StringUtil.nullDouble(jj_video_percent)));
					res_map.put("jj_voice_percent", String.format("%.2f", StringUtil.nullDouble(jj_voice_percent)));
					res_map.put("fq_video_user", fq_video_user);
					res_map.put("fq_voice_user", fq_voice_user);
					res_map.put("jt_video_user", jt_video_user);
					res_map.put("jt_voice_user", jt_voice_user);
					res_map.put("onetoone_user", onetoone_user);
					res_map.put("gift_amount", gift_amount);
					res_map.put("video_amount", video_amount);
					res_map.put("voice_amount", voice_amount);
					res_map.put("msg_amount", msg_amount);
					res_map.put("amount", amount);
					res_map.put("video_valid", video_valid);
					res_map.put("voice_valid", voice_valid);
					res_list.add(res_map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res_list;
	}

	// 查询一对一有效发起次数
	private static long findOneToOneDayValid(String create_date, Integer type) {
		try {
			// 声明where 条件
			BoolQueryBuilder qbs = QueryBuilders.boolQuery();

			// 根据时间条件查询
			QueryBuilder qb1 = QueryBuilders.rangeQuery("create_date").from(create_date + " 00:00:00")
					.to(create_date + " 23:59:59").includeLower(true).includeUpper(true);
			BoolQueryBuilder qbs1 = QueryBuilders.boolQuery().must(qb1);
			qbs.must(qbs1);

			QueryBuilder qb2 = QueryBuilders.termQuery("app_type", "6");
			BoolQueryBuilder qbs2 = QueryBuilders.boolQuery().must(qb2);
			qbs.must(qbs2);

			QueryBuilder qb3 = QueryBuilders.existsQuery("start_date");
			BoolQueryBuilder qbs3 = QueryBuilders.boolQuery().mustNot(qb3);
			qbs.must(qbs3);

			QueryBuilder qb4 = QueryBuilders.termQuery("find_type", type);
			BoolQueryBuilder qbs4 = QueryBuilders.boolQuery().must(qb4);
			qbs.must(qbs4);

			QueryBuilder qb5 = QueryBuilders.rangeQuery("end_start").from(0).to(20).includeLower(true)
					.includeUpper(true);
			BoolQueryBuilder qbs5 = QueryBuilders.boolQuery().mustNot(qb5);
			qbs.must(qbs5);

			SearchRequestBuilder requestBuilder = esClient.prepareSearch("fk_onetoone_detail")
					.setTypes("onetoone_detail").setQuery(qbs);
			SearchResponse response = requestBuilder.execute().actionGet();
			SearchHits hits = response.getHits();
			return hits.getTotalHits();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public Map<String, Object> findOneToOneDayFooter(Map<String, Object> param) {
		String start_time = StringUtil.nullBlank(param.get("start_time"));
		String end_time = StringUtil.nullBlank(param.get("end_time"));
		String status = StringUtil.nullBlank(param.get("status"));
		Map<String, Object> res_map = new HashMap<String, Object>();
		try {
			// 声明where 条件
			BoolQueryBuilder qbs = QueryBuilders.boolQuery();

			// 根据时间条件查询
			QueryBuilder qb1 = QueryBuilders.rangeQuery("create_date").from(start_time).to(end_time).includeLower(true)
					.includeUpper(true);
			BoolQueryBuilder qbs1 = QueryBuilders.boolQuery().must(qb1);
			qbs.must(qbs1);

			// 是否过滤白名单
			if ("1".equals(status)) {
				QueryBuilder qb2 = QueryBuilders.termQuery("type", "yuban_1");
				BoolQueryBuilder qbs2 = QueryBuilders.boolQuery().must(qb2);
				qbs.must(qbs2);
			} else if ("2".equals(status)) {
				QueryBuilder qb2 = QueryBuilders.termQuery("type", "yuban_2");
				BoolQueryBuilder qbs2 = QueryBuilders.boolQuery().must(qb2);
				qbs.must(qbs2);
			}

			SearchRequestBuilder requestBuilder = esClient.prepareSearch("zb_ht_onetoone_date")
					.setTypes("ht_onetoone_date");
			requestBuilder.setQuery(qbs);

			SortBuilder date_sort = SortBuilders.fieldSort("create_date").order(SortOrder.DESC);
			requestBuilder.addSort(date_sort);

			SearchResponse response = requestBuilder.setFrom(0).setSize(300).execute().actionGet();
			SearchHits hits = response.getHits();
			if (hits.getTotalHits() > 0) {
				double long_time = 0d, fq_voice_num = 0d, fq_video_num = 0d, jt_voice_num = 0d, jt_video_num = 0d,
						jtl_video_percent = 0d, jtl_voice_percent = 0d, jj_video_percent = 0d, jj_voice_percent = 0d,
						fq_voice_user = 0d, fq_video_user = 0d, jt_video_user = 0d, jt_voice_user = 0d,
						onetoone_user = 0d, gift_amount = 0d, video_amount = 0d, voice_amount = 0d, msg_amount = 0d;
				for (SearchHit hit : hits) {
					Map<String, Object> val = hit.getSource();
					long_time = BigDecimalUtil.add(StringUtil.nullDouble(long_time),
							StringUtil.nullDouble(val.get("long_time")));
					fq_video_num = BigDecimalUtil.add(StringUtil.nullDouble(fq_video_num),
							StringUtil.nullDouble(val.get("fq_video_num")));
					fq_voice_num = BigDecimalUtil.add(StringUtil.nullDouble(fq_voice_num),
							StringUtil.nullDouble(val.get("fq_voice_num")));
					jt_voice_num = BigDecimalUtil.add(StringUtil.nullDouble(jt_voice_num),
							StringUtil.nullDouble(val.get("jt_voice_num")));
					jt_video_num = BigDecimalUtil.add(StringUtil.nullDouble(jt_video_num),
							StringUtil.nullDouble(val.get("jt_video_num")));
					jtl_video_percent = BigDecimalUtil.add(StringUtil.nullDouble(jtl_video_percent),
							StringUtil.nullDouble(val.get("jtl_video_percent")));
					jtl_voice_percent = BigDecimalUtil.add(StringUtil.nullDouble(jtl_voice_percent),
							StringUtil.nullDouble(val.get("jtl_voice_percent")));
					jj_video_percent = BigDecimalUtil.add(StringUtil.nullDouble(jj_video_percent),
							StringUtil.nullDouble(val.get("jj_video_percent")));
					jj_voice_percent = BigDecimalUtil.add(StringUtil.nullDouble(jj_voice_percent),
							StringUtil.nullDouble(val.get("jj_voice_percent")));
					fq_voice_user = BigDecimalUtil.add(StringUtil.nullDouble(fq_voice_user),
							StringUtil.nullDouble(val.get("fq_voice_user")));
					fq_video_user = BigDecimalUtil.add(StringUtil.nullDouble(fq_video_user),
							StringUtil.nullDouble(val.get("fq_video_user")));
					jt_video_user = BigDecimalUtil.add(StringUtil.nullDouble(jt_video_user),
							StringUtil.nullDouble(val.get("jt_video_user")));
					jt_voice_user = BigDecimalUtil.add(StringUtil.nullDouble(jt_voice_user),
							StringUtil.nullDouble(val.get("jt_voice_user")));
					onetoone_user = BigDecimalUtil.add(StringUtil.nullDouble(onetoone_user),
							StringUtil.nullDouble(val.get("onetoone_user")));
					gift_amount = BigDecimalUtil.add(StringUtil.nullDouble(gift_amount),
							StringUtil.nullDouble(val.get("gift_amount")));
					video_amount = BigDecimalUtil.add(StringUtil.nullDouble(video_amount),
							StringUtil.nullDouble(val.get("video_amount")));
					voice_amount = BigDecimalUtil.add(StringUtil.nullDouble(voice_amount),
							StringUtil.nullDouble(val.get("voice_amount")));
					msg_amount = BigDecimalUtil.add(StringUtil.nullDouble(msg_amount),
							StringUtil.nullDouble(val.get("msg_amount")));
				}

				res_map.put("create_date", "合计");
				res_map.put("long_time", long_time);
				res_map.put("fq_video_num", fq_video_num);
				res_map.put("fq_voice_num", fq_voice_num);
				res_map.put("jt_voice_num", jt_voice_num);
				res_map.put("jt_video_num", jt_video_num);
				res_map.put("jtl_video_percent", jtl_video_percent);
				res_map.put("jtl_voice_percent", jtl_voice_percent);
				res_map.put("jj_video_percent", jj_video_percent);
				res_map.put("jj_voice_percent", jj_voice_percent);
				res_map.put("fq_voice_user", fq_voice_user);
				res_map.put("fq_video_user", fq_video_user);
				res_map.put("jt_video_user", jt_video_user);
				res_map.put("jt_voice_user", jt_voice_user);
				res_map.put("onetoone_user", onetoone_user);
				res_map.put("gift_amount", gift_amount);
				res_map.put("video_amount", video_amount);
				res_map.put("voice_amount", voice_amount);
				res_map.put("msg_amount", msg_amount);

				double amount = BigDecimalUtil.add(StringUtil.nullDouble(gift_amount),
						StringUtil.nullDouble(video_amount));
				amount = BigDecimalUtil.add(StringUtil.nullDouble(amount), StringUtil.nullDouble(voice_amount));
				amount = BigDecimalUtil.add(StringUtil.nullDouble(amount), StringUtil.nullDouble(msg_amount));
				res_map.put("amount", amount);

				return res_map;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
