package com.study.service.detail.impl;

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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mysql.fabric.xmlrpc.base.Array;
import com.study.config.ESTransportClient;
import com.study.config.MgTemplate;
import com.study.mapper.detail.OneToOneDetailMapper;
import com.study.service.detail.OneToOneDetailService;
import com.study.service.lotterytie.LotterytieService;
import com.study.util.bean.PageBean;
import com.study.util.esUtil.ESClientUtil;
import com.study.util.tool.BigDecimalUtil;
import com.study.util.tool.DateUtil;
import com.study.util.tool.StringUtil;

import io.swagger.annotations.ApiImplicitParam;

@Service("oneToOneDetailService")
public class OneToOneDetailServiceImpl implements OneToOneDetailService {
	private static Logger logger = LoggerFactory.getLogger(OneToOneDetailServiceImpl.class);

	@Autowired
	private OneToOneDetailMapper oneToOneDetailMapper;

	@Autowired
	private LotterytieService lotterytieService;

	@Autowired
	private MongoTemplate mongoTemplate;

	private static TransportClient esClient = ESTransportClient.getInstance();

	@Override
	public long findOneToOneDetailTotal(Map<String, Object> param) {
		String start_time = StringUtil.nullBlank(param.get("start_time"));
		String end_time = StringUtil.nullBlank(param.get("end_time"));
		String con_status = StringUtil.nullBlank(param.get("con_status"));// 是否接通1全部2接通3未接4拒接
		String user_nick_name = StringUtil.nullBlank(param.get("user_nick_name"));// 发起者昵称
		String anchor_nick_name = StringUtil.nullBlank(param.get("anchor_nick_name"));// 接听者昵称
		String anchor_status = StringUtil.nullBlank(param.get("anchor_status"));// 发起者类型1全部2去除白名单用户3白名单用户
		String user_status = StringUtil.nullBlank(param.get("user_status"));// 接听者类型1全部2去除白名单用户3白名单用户

		try {

			// 声明where 条件
			BoolQueryBuilder qbs = QueryBuilders.boolQuery();

			// 根据时间条件查询
			QueryBuilder qb1 = QueryBuilders.rangeQuery("create_date").from(start_time).to(end_time).includeLower(true)
					.includeUpper(true);
			BoolQueryBuilder qbs1 = QueryBuilders.boolQuery().must(qb1);
			qbs.must(qbs1);

			// 查询视频跟语音
			QueryBuilder qb2 = QueryBuilders.termsQuery("find_type", new ArrayList<String>() {
				{
					add("1");
					add("2");
				}
			});
			BoolQueryBuilder qbs2 = QueryBuilders.boolQuery().must(qb2);
			qbs.must(qbs2);

			if ("2".equals(con_status)) {// 查询接通
				QueryBuilder qb3 = QueryBuilders.existsQuery("start_date");
				BoolQueryBuilder qbs3 = QueryBuilders.boolQuery().must(qb3);
				qbs.must(qbs3);
			} else if ("3".equals(con_status)) {// 查询未接通
				QueryBuilder qb4 = QueryBuilders.existsQuery("start_date");
				BoolQueryBuilder qbs4 = QueryBuilders.boolQuery().mustNot(qb4);
				qbs.must(qbs4);
			} else if ("4".equals(con_status)) {// 查询拒接
				QueryBuilder qb5 = QueryBuilders.termQuery("status", "4");
				BoolQueryBuilder qbs5 = QueryBuilders.boolQuery().must(qb5);
				qbs.must(qbs5);
			}

			if (!"".equals(anchor_nick_name)) {// 查询接听者
				QueryBuilder qb6 = QueryBuilders.matchPhraseQuery("anchor_nick_name", anchor_nick_name);
				BoolQueryBuilder qbs6 = QueryBuilders.boolQuery().must(qb6);
				qbs.must(qbs6);
			}

			if (!"".equals(user_nick_name)) {// 查询发起者
				QueryBuilder qb6 = QueryBuilders.matchPhraseQuery("user_nick_name", user_nick_name);
				BoolQueryBuilder qbs6 = QueryBuilders.boolQuery().must(qb6);
				qbs.must(qbs6);
			}

			List<String> user_list = lotterytieService.findWhiteUserList();

			if ("2".equals(anchor_status)) {// 接收者去除白名单用户
				QueryBuilder qb7 = QueryBuilders.termsQuery("anchor_name", user_list);
				BoolQueryBuilder qbs7 = QueryBuilders.boolQuery().mustNot(qb7);
				qbs.must(qbs7);
			} else if ("3".equals(anchor_status)) {// 接收者是白名单用户
				QueryBuilder qb8 = QueryBuilders.termsQuery("anchor_name", user_list);
				BoolQueryBuilder qbs8 = QueryBuilders.boolQuery().must(qb8);
				qbs.must(qbs8);
			}

			if ("2".equals(user_status)) {// 发起者去除白名单用户
				QueryBuilder qb9 = QueryBuilders.termsQuery("user_name", user_list);
				BoolQueryBuilder qbs9 = QueryBuilders.boolQuery().mustNot(qb9);
				qbs.must(qbs9);
			} else if ("3".equals(user_status)) {// 发起者是白名单用户
				QueryBuilder qb10 = QueryBuilders.termsQuery("user_name", user_list);
				BoolQueryBuilder qbs10 = QueryBuilders.boolQuery().must(qb10);
				qbs.must(qbs10);
			}

			// 只查询语伴一对一记录
			QueryBuilder qb11 = QueryBuilders.termQuery("app_type", "6");
			BoolQueryBuilder qbs11 = QueryBuilders.boolQuery().must(qb11);
			qbs.must(qbs11);

			SearchRequestBuilder requestBuilder = esClient.prepareSearch("fk_onetoone_detail")
					.setTypes("onetoone_detail");
			requestBuilder.setQuery(qbs);
			SortBuilder date_sort = SortBuilders.fieldSort("create_date").order(SortOrder.DESC);
			requestBuilder.addSort(date_sort);

			SearchResponse response = requestBuilder.execute().actionGet();
			return response.getHits().getTotalHits();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<Map<String, Object>> findOneToOneDetailList(Map<String, Object> param, PageBean bean) {
		Integer page = bean.getPage();
		Integer rows = bean.getRows();
		String start_time = StringUtil.nullBlank(param.get("start_time"));
		String end_time = StringUtil.nullBlank(param.get("end_time"));
		String con_status = StringUtil.nullBlank(param.get("con_status"));// 是否接通1全部2接通3未接4拒接
		String user_nick_name = StringUtil.nullBlank(param.get("user_nick_name"));// 发起者昵称
		String anchor_nick_name = StringUtil.nullBlank(param.get("anchor_nick_name"));// 接听者昵称
		String anchor_status = StringUtil.nullBlank(param.get("anchor_status"));// 发起者类型1全部2去除白名单用户3白名单用户
		String user_status = StringUtil.nullBlank(param.get("user_status"));// 接听者类型1全部2去除白名单用户3白名单用户

		List<Map<String, Object>> res_list = new ArrayList<Map<String, Object>>();
		try {
			// 声明where 条件
			BoolQueryBuilder qbs = QueryBuilders.boolQuery();

			// 根据时间条件查询
			QueryBuilder qb1 = QueryBuilders.rangeQuery("create_date").from(start_time).to(end_time).includeLower(true)
					.includeUpper(true);
			BoolQueryBuilder qbs1 = QueryBuilders.boolQuery().must(qb1);
			qbs.must(qbs1);

			// 查询视频跟语音
			QueryBuilder qb2 = QueryBuilders.termsQuery("find_type", new ArrayList<String>() {
				{
					add("1");
					add("2");
				}
			});
			BoolQueryBuilder qbs2 = QueryBuilders.boolQuery().must(qb2);
			qbs.must(qbs2);

			if ("2".equals(con_status)) {// 查询接通
				QueryBuilder qb3 = QueryBuilders.existsQuery("start_date");
				BoolQueryBuilder qbs3 = QueryBuilders.boolQuery().must(qb3);
				qbs.must(qbs3);
			} else if ("3".equals(con_status)) {// 查询未接通
				QueryBuilder qb4 = QueryBuilders.existsQuery("start_date");
				BoolQueryBuilder qbs4 = QueryBuilders.boolQuery().mustNot(qb4);
				qbs.must(qbs4);
			} else if ("4".equals(con_status)) {// 查询拒接
				QueryBuilder qb5 = QueryBuilders.termQuery("status", "4");
				BoolQueryBuilder qbs5 = QueryBuilders.boolQuery().must(qb5);
				qbs.must(qbs5);
			}

			if (!"".equals(anchor_nick_name)) {// 查询接听者
				QueryBuilder qb6 = QueryBuilders.matchPhraseQuery("anchor_nick_name", anchor_nick_name);
				BoolQueryBuilder qbs6 = QueryBuilders.boolQuery().must(qb6);
				qbs.must(qbs6);
			}

			if (!"".equals(user_nick_name)) {// 查询发起者
				QueryBuilder qb6 = QueryBuilders.matchPhraseQuery("user_nick_name", user_nick_name);
				BoolQueryBuilder qbs6 = QueryBuilders.boolQuery().must(qb6);
				qbs.must(qbs6);
			}

			List<String> user_list = lotterytieService.findWhiteUserList();

			if ("2".equals(anchor_status)) {// 接收者去除白名单用户
				QueryBuilder qb7 = QueryBuilders.termsQuery("anchor_name", user_list);
				BoolQueryBuilder qbs7 = QueryBuilders.boolQuery().mustNot(qb7);
				qbs.must(qbs7);
			} else if ("3".equals(anchor_status)) {// 接收者是白名单用户
				QueryBuilder qb8 = QueryBuilders.termsQuery("anchor_name", user_list);
				BoolQueryBuilder qbs8 = QueryBuilders.boolQuery().must(qb8);
				qbs.must(qbs8);
			}

			if ("2".equals(user_status)) {// 发起者去除白名单用户
				QueryBuilder qb9 = QueryBuilders.termsQuery("user_name", user_list);
				BoolQueryBuilder qbs9 = QueryBuilders.boolQuery().mustNot(qb9);
				qbs.must(qbs9);
			} else if ("3".equals(user_status)) {// 发起者是白名单用户
				QueryBuilder qb10 = QueryBuilders.termsQuery("user_name", user_list);
				BoolQueryBuilder qbs10 = QueryBuilders.boolQuery().must(qb10);
				qbs.must(qbs10);
			}

			// 只查询语伴一对一记录
			QueryBuilder qb11 = QueryBuilders.termQuery("app_type", "6");
			BoolQueryBuilder qbs11 = QueryBuilders.boolQuery().must(qb11);
			qbs.must(qbs11);

			SearchRequestBuilder requestBuilder = esClient.prepareSearch("fk_onetoone_detail")
					.setTypes("onetoone_detail");
			requestBuilder.setQuery(qbs);
			SortBuilder date_sort = SortBuilders.fieldSort("create_date").order(SortOrder.DESC);
			requestBuilder.addSort(date_sort);

			SearchResponse response = requestBuilder.setFrom((page - 1) * rows).setSize(rows).execute().actionGet();
			SearchHits hits = response.getHits();
			if (hits.getHits().length > 0) {
				for (SearchHit hit : hits) {
					Map<String, Object> val = hit.getSource();
					String user_name = StringUtil.nullBlank(val.get("user_name"));// 发起者账号
					user_nick_name = StringUtil.nullBlank(val.get("user_nick_name"));// 发起者昵称

					if ("".equals(user_nick_name)) {
						logger.info("findOneToOneDetailList=====>user_name:" + user_name);
						Map<String, Object> user_map = ESClientUtil.findYTUserEntity(user_name);
						user_nick_name = StringUtil.nullBlank(user_map.get("nick_name"));
					}

					String create_date = StringUtil.nullBlank(val.get("create_date"));// 发起时间
					String find_type = StringUtil.nullBlank(val.get("find_type"));// 类型
					String start_date = StringUtil.nullBlank(val.get("start_date"));// 接通时间
					String end_date = StringUtil.nullBlank(val.get("end_date"));// 结束时间
					String status = StringUtil.nullBlank(val.get("status"));
					if (!"".equals(start_date)) {// 接通
						con_status = "2";
					} else if ("4".equals(status)) {// 拒接
						con_status = "4";
					} else if ("".equals(start_date)) {// 未接
						con_status = "3";
					}
					String anchor_name = StringUtil.nullBlank(val.get("anchor_name"));// 接听者账号
					anchor_nick_name = StringUtil.nullBlank(val.get("anchor_nick_name"));// 接听者昵称

					if ("".equals(anchor_nick_name)) {
						logger.info("findOneToOneDetailList=====>anchor_name:" + anchor_name);
						Map<String, Object> user_map = ESClientUtil.findYTUserEntity(anchor_name);
						anchor_nick_name = StringUtil.nullBlank(user_map.get("nick_name"));
					}

					String hang_up = "";// 挂断者
					String content = StringUtil.nullBlank(val.get("error_content"));// 备注
					if ("2".equals(status)) {// 用户发起中挂断
						hang_up = user_nick_name;
						content = "用户发起中挂断";
					} else if ("3".equals(status)) {// 主播接听中挂断
						hang_up = anchor_nick_name;
						content = "主播接听中挂断";
					} else if ("4".equals(status)) {// 主播发起中拒接
						hang_up = anchor_nick_name;
						content = "主播发起中拒接";
					} else if ("5".equals(status)) {// 用户心跳断开
						hang_up = user_nick_name;
						content = "用户心跳断开";
					} else if ("6".equals(status)) {// 主播心跳断开
						hang_up = anchor_nick_name;
						content = "主播心跳断开";
					} else if ("7".equals(status)) {// 用户扣款失败
						hang_up = user_nick_name;
						content = "用户扣款失败";
					} else if ("8".equals(status)) {// 用户调用接口异常
						hang_up = user_nick_name;
						content = "用户调用接口异常";
					} else if ("9".equals(status)) {// 主播调用接口异常
						hang_up = anchor_nick_name;
						content = "主播调用接口异常";
					} else if ("10".equals(status)) {// 通话中用户挂断
						hang_up = user_nick_name;
						content = "通话中用户挂断";
					} else if ("11".equals(status)) {// 主播发其中未接听
						hang_up = user_nick_name;
						content = "主播发其中未接听";
					} else if ("12".equals(status)) {// 用户余额不足
						hang_up = user_nick_name;
						content = "用户余额不足";
					}

					String price = StringUtil.nullBlank(val.get("price"));// 接听价格
					String free = "1";// 免费一分钟1不含2包含
					String time = "";// 通话时长
					if (!"".equals(start_date) && !"".equals(end_date)) {
						long start = DateUtil.getDateTime(start_date);
						long end = DateUtil.getDateTime(end_date);
						time = DateUtil.secondToTime(StringUtil.nullZeroLong((end - start) / 1000));
					}

					String place_id = StringUtil.nullBlank(val.get("place_id"));// 一对一场次ID
					// 查询当前场次消耗金豆数,收入金砖数
					Map<String, Object> map = oneToOneDetailMapper.findOneToOneDetail(place_id);
					Integer brick_amount = 0;// 收入金砖数
					Integer goal_amount = 0;// 消耗金币数
					if (map != null) {
						brick_amount = StringUtil.nullZero(map.get("BRICE_AMOUNT"));
						goal_amount = StringUtil.nullZero(map.get("GOAL_AMOUNT"));
					}
					logger.info("findOneToOneDetail========>place_id:" + place_id + "--map:" + map);
					Map<String, Object> res_map = new HashMap<String, Object>();
					res_map.put("user_name", user_name);// 发起者账号
					res_map.put("user_nick_name", user_nick_name);// 发起者昵称
					res_map.put("create_date", create_date);// 发起时间
					res_map.put("find_type", find_type);// 类型1视频2语音
					res_map.put("start_date", start_date);// 接通时间
					res_map.put("end_date", end_date);// 结束时间
					res_map.put("end_start", DateUtil.calLastedTime(create_date, end_date));// 结束时间
					res_map.put("con_status", con_status);// 是否接通1全部2接通3未接4拒接
					res_map.put("anchor_nick_name", anchor_nick_name);// 接听者昵称
					res_map.put("anchor_name", anchor_name);// 接听者账号
					res_map.put("hang_up", hang_up);// 挂断者
					res_map.put("price", price);// 接听价格
					res_map.put("free", free);// 免费一分钟1不含2包含
					res_map.put("time", time);// 通话时长
					res_map.put("goal_amount", goal_amount);// 消耗金币数
					res_map.put("brick_amount", brick_amount);// 收入金砖数
					res_map.put("content", content);// 备注
					res_list.add(res_map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res_list;
	}

	@Override
	public Map<String, Object> findOneToOneDetailFooter(Map<String, Object> param, PageBean bean) {
		Integer page = bean.getPage();
		Integer rows = bean.getRows();
		String start_time = StringUtil.nullBlank(param.get("start_time"));
		String end_time = StringUtil.nullBlank(param.get("end_time"));
		String con_status = StringUtil.nullBlank(param.get("con_status"));// 是否接通1全部2接通3未接4拒接
		String user_nick_name = StringUtil.nullBlank(param.get("user_nick_name"));// 发起者昵称
		String anchor_nick_name = StringUtil.nullBlank(param.get("anchor_nick_name"));// 接听者昵称
		String anchor_status = StringUtil.nullBlank(param.get("anchor_status"));// 发起者类型1全部2去除白名单用户3白名单用户
		String user_status = StringUtil.nullBlank(param.get("user_status"));// 接听者类型1全部2去除白名单用户3白名单用户
		Map<String, Object> res_map = new HashMap<String, Object>();
		try {
			// 声明where 条件
			BoolQueryBuilder qbs = QueryBuilders.boolQuery();

			// 根据时间条件查询
			QueryBuilder qb1 = QueryBuilders.rangeQuery("create_date").from(start_time).to(end_time).includeLower(true)
					.includeUpper(true);
			BoolQueryBuilder qbs1 = QueryBuilders.boolQuery().must(qb1);
			qbs.must(qbs1);

			// 查询视频跟语音
			QueryBuilder qb2 = QueryBuilders.termsQuery("find_type", new ArrayList<String>() {
				{
					add("1");
					add("2");
				}
			});
			BoolQueryBuilder qbs2 = QueryBuilders.boolQuery().must(qb2);
			qbs.must(qbs2);

			if ("2".equals(con_status)) {// 查询接通
				QueryBuilder qb3 = QueryBuilders.existsQuery("start_date");
				BoolQueryBuilder qbs3 = QueryBuilders.boolQuery().must(qb3);
				qbs.must(qbs3);
			} else if ("3".equals(con_status)) {// 查询未接通
				QueryBuilder qb4 = QueryBuilders.existsQuery("start_date");
				BoolQueryBuilder qbs4 = QueryBuilders.boolQuery().mustNot(qb4);
				qbs.must(qbs4);
			} else if ("4".equals(con_status)) {// 查询拒接
				QueryBuilder qb5 = QueryBuilders.termQuery("status", "4");
				BoolQueryBuilder qbs5 = QueryBuilders.boolQuery().must(qb5);
				qbs.must(qbs5);
			}

			if (!"".equals(anchor_nick_name)) {// 查询接听者
				QueryBuilder qb6 = QueryBuilders.matchPhraseQuery("anchor_nick_name", anchor_nick_name);
				BoolQueryBuilder qbs6 = QueryBuilders.boolQuery().must(qb6);
				qbs.must(qbs6);
			}

			if (!"".equals(user_nick_name)) {// 查询发起者
				QueryBuilder qb6 = QueryBuilders.matchPhraseQuery("user_nick_name", user_nick_name);
				BoolQueryBuilder qbs6 = QueryBuilders.boolQuery().must(qb6);
				qbs.must(qbs6);
			}

			List<String> user_list = lotterytieService.findWhiteUserList();

			if ("2".equals(anchor_status)) {// 接收者去除白名单用户
				QueryBuilder qb7 = QueryBuilders.termsQuery("anchor_name", user_list);
				BoolQueryBuilder qbs7 = QueryBuilders.boolQuery().mustNot(qb7);
				qbs.must(qbs7);
			} else if ("3".equals(anchor_status)) {// 接收者是白名单用户
				QueryBuilder qb8 = QueryBuilders.termsQuery("anchor_name", user_list);
				BoolQueryBuilder qbs8 = QueryBuilders.boolQuery().must(qb8);
				qbs.must(qbs8);
			}

			if ("2".equals(user_status)) {// 发起者去除白名单用户
				QueryBuilder qb9 = QueryBuilders.termsQuery("user_name", user_list);
				BoolQueryBuilder qbs9 = QueryBuilders.boolQuery().mustNot(qb9);
				qbs.must(qbs9);
			} else if ("3".equals(user_status)) {// 发起者是白名单用户
				QueryBuilder qb10 = QueryBuilders.termsQuery("user_name", user_list);
				BoolQueryBuilder qbs10 = QueryBuilders.boolQuery().must(qb10);
				qbs.must(qbs10);
			}

			// 只查询语伴一对一记录
			QueryBuilder qb11 = QueryBuilders.termQuery("app_type", "6");
			BoolQueryBuilder qbs11 = QueryBuilders.boolQuery().must(qb11);
			qbs.must(qbs11);

			SearchRequestBuilder requestBuilder = esClient.prepareSearch("fk_onetoone_detail")
					.setTypes("onetoone_detail");
			requestBuilder.setQuery(qbs);
			SortBuilder date_sort = SortBuilders.fieldSort("create_date").order(SortOrder.DESC);
			requestBuilder.addSort(date_sort);

			SearchResponse response = requestBuilder.setFrom((page - 1) * rows).setSize(rows).execute().actionGet();
			SearchHits hits = response.getHits();
			if (hits.getHits().length > 0) {
				Integer time_d = 0, brick_amount_d = 0, goal_amount_d = 0;
				for (SearchHit hit : hits) {
					Map<String, Object> val = hit.getSource();
					String start_date = StringUtil.nullBlank(val.get("start_date"));// 接通时间
					String end_date = StringUtil.nullBlank(val.get("end_date"));// 结束时间
					String time = "";// 通话时长
					if (!"".equals(start_date) && !"".equals(end_date)) {
						long start = DateUtil.getDateTime(start_date);
						long end = DateUtil.getDateTime(end_date);
						time_d = time_d + StringUtil.nullZero((end - start) / 1000);
					}

					String place_id = StringUtil.nullBlank(val.get("place_id"));// 一对一场次ID
					// 查询当前场次消耗金豆数,收入金砖数
					Map<String, Object> map = oneToOneDetailMapper.findOneToOneDetail(place_id);
					Integer brick_amount = 0;// 收入金砖数
					Integer goal_amount = 0;// 消耗金币数
					if (map != null) {
						brick_amount = StringUtil.nullZero(map.get("BRICE_AMOUNT"));
						goal_amount = StringUtil.nullZero(map.get("GOAL_AMOUNT"));
					}
					brick_amount_d = brick_amount_d + brick_amount;
					goal_amount_d = goal_amount_d + goal_amount;
				}
				res_map.put("user_nick_name", "合计");
				res_map.put("time", DateUtil.secondToTime(time_d));
				res_map.put("goal_amount", goal_amount_d);
				res_map.put("brick_amount", brick_amount_d);
				return res_map;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public long findOneToOneMsgDetailTotal(Map<String, Object> param) {
		String start_time = StringUtil.nullBlank(param.get("start_time"));
		String end_time = StringUtil.nullBlank(param.get("end_time"));
		String user_nick_name = StringUtil.nullBlank(param.get("user_nick_name"));// 发起者昵称
		String anchor_nick_name = StringUtil.nullBlank(param.get("anchor_nick_name"));// 接听者昵称
		String anchor_status = StringUtil.nullBlank(param.get("anchor_status"));// 发起者类型1全部2去除白名单用户3白名单用户
		String user_status = StringUtil.nullBlank(param.get("user_status"));// 接听者类型1全部2去除白名单用户3白名单用户
		try {

			DBCollection collection = mongoTemplate.getCollection("yt_live_private_letter");

			DBObject obj = new BasicDBObject();
			BasicDBList condList = new BasicDBList();
			condList.add(new BasicDBObject("appType", "6"));
			if ("2".equals(user_status)) {
				condList.add(new BasicDBObject("user_type", new BasicDBObject("$ne", 1)));
			} else if ("3".equals(user_status)) {
				condList.add(new BasicDBObject("user_type", "1"));
			}

			if ("2".equals(anchor_status)) {
				condList.add(new BasicDBObject("anchor_type", new BasicDBObject("$ne", 1)));
			} else if ("3".equals(user_status)) {
				condList.add(new BasicDBObject("anchor_type", "1"));
			}

			if (!"".equals(user_nick_name)) {
				condList.add(new BasicDBObject("user_nick_name", user_nick_name));
			}
			if (!"".equals(anchor_nick_name)) {
				condList.add(new BasicDBObject("anchor_nick_name", anchor_nick_name));
			}

			if (!"".equals(start_time) && !"".equals(end_time)) {
				condList.add(new BasicDBObject("create_time", new BasicDBObject("$gte", start_time)));
				condList.add(new BasicDBObject("create_time", new BasicDBObject("$lt", end_time)));
			}
			obj.put("$and", condList);
			return collection.find(obj).count();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<Map<String, Object>> findOneToOneMsgDetailList(Map<String, Object> param, PageBean bean) {
		Integer page = bean.getPage();
		Integer rows = bean.getRows();
		String start_time = StringUtil.nullBlank(param.get("start_time"));
		String end_time = StringUtil.nullBlank(param.get("end_time"));
		String user_nick_name = StringUtil.nullBlank(param.get("user_nick_name"));// 发起者昵称
		String anchor_nick_name = StringUtil.nullBlank(param.get("anchor_nick_name"));// 接听者昵称
		String anchor_status = StringUtil.nullBlank(param.get("anchor_status"));// 发起者类型1全部2去除白名单用户3白名单用户
		String user_status = StringUtil.nullBlank(param.get("user_status"));// 接听者类型1全部2去除白名单用户3白名单用户
		List<Map<String, Object>> res_list = new ArrayList<Map<String, Object>>();
		try {
			DBCollection collection = mongoTemplate.getCollection("yt_live_private_letter");

			DBObject obj = new BasicDBObject();
			BasicDBList condList = new BasicDBList();
			condList.add(new BasicDBObject("appType", "6"));
			if ("2".equals(user_status)) {
				condList.add(new BasicDBObject("user_type", new BasicDBObject("$ne", 1)));
			} else if ("3".equals(user_status)) {
				condList.add(new BasicDBObject("user_type", "1"));
			}

			if ("2".equals(anchor_status)) {
				condList.add(new BasicDBObject("anchor_type", new BasicDBObject("$ne", 1)));
			} else if ("3".equals(user_status)) {
				condList.add(new BasicDBObject("anchor_type", "1"));
			}

			if (!"".equals(user_nick_name)) {
				condList.add(new BasicDBObject("user_nick_name", user_nick_name));
			}
			if (!"".equals(anchor_nick_name)) {
				condList.add(new BasicDBObject("anchor_nick_name", anchor_nick_name));
			}

			if (!"".equals(start_time) && !"".equals(end_time)) {
				condList.add(new BasicDBObject("create_time", new BasicDBObject("$gte", start_time)));
				condList.add(new BasicDBObject("create_time", new BasicDBObject("$lt", end_time)));
			}
			obj.put("$and", condList);
			DBCursor list = collection.find(obj).skip((page - 1) * rows).limit(rows)
					.sort(new BasicDBObject("create_time", -1));
			for (DBObject dbObj : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("create_time", dbObj.get("create_time"));
				map.put("user_nick_name", dbObj.get("user_nick_name"));
				map.put("anchor_nick_name", dbObj.get("anchor_nick_name"));
				map.put("msgPrice", dbObj.get("msgPrice"));
				map.put("msg_amount", dbObj.get("msg_amount"));
				map.put("brick_amount", dbObj.get("brick_amount"));
				map.put("content", dbObj.get("content"));
				res_list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res_list;
	}

	@Override
	public Map<String, Object> findOneToOneMsgDetailFooter(Map<String, Object> param, PageBean bean) {
		Integer page = bean.getPage();
		Integer rows = bean.getRows();
		String start_time = StringUtil.nullBlank(param.get("start_time"));
		String end_time = StringUtil.nullBlank(param.get("end_time"));
		String user_nick_name = StringUtil.nullBlank(param.get("user_nick_name"));// 发起者昵称
		String anchor_nick_name = StringUtil.nullBlank(param.get("anchor_nick_name"));// 接听者昵称
		String anchor_status = StringUtil.nullBlank(param.get("anchor_status"));// 发起者类型1全部2去除白名单用户3白名单用户
		String user_status = StringUtil.nullBlank(param.get("user_status"));// 接听者类型1全部2去除白名单用户3白名单用户
		List<Map<String, Object>> res_list = new ArrayList<Map<String, Object>>();
		try {
			DBCollection collection = mongoTemplate.getCollection("yt_live_private_letter");

			DBObject obj = new BasicDBObject();
			BasicDBList condList = new BasicDBList();
			condList.add(new BasicDBObject("appType", "6"));
			if ("2".equals(user_status)) {
				condList.add(new BasicDBObject("user_type", new BasicDBObject("$ne", 1)));
			} else if ("3".equals(user_status)) {
				condList.add(new BasicDBObject("user_type", "1"));
			}

			if ("2".equals(anchor_status)) {
				condList.add(new BasicDBObject("anchor_type", new BasicDBObject("$ne", 1)));
			} else if ("3".equals(user_status)) {
				condList.add(new BasicDBObject("anchor_type", "1"));
			}

			if (!"".equals(user_nick_name)) {
				condList.add(new BasicDBObject("user_nick_name", user_nick_name));
			}
			if (!"".equals(anchor_nick_name)) {
				condList.add(new BasicDBObject("anchor_nick_name", anchor_nick_name));
			}

			if (!"".equals(start_time) && !"".equals(end_time)) {
				condList.add(new BasicDBObject("create_time", new BasicDBObject("$gte", start_time)));
				condList.add(new BasicDBObject("create_time", new BasicDBObject("$lt", end_time)));
			}
			obj.put("$and", condList);
			DBCursor list = collection.find(obj).skip((page - 1) * rows).limit(rows)
					.sort(new BasicDBObject("create_time", -1));
			for (DBObject dbObj : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("create_time", dbObj.get("create_time"));
				map.put("user_nick_name", dbObj.get("user_nick_name"));
				map.put("anchor_nick_name", dbObj.get("anchor_nick_name"));
				map.put("msgPrice", dbObj.get("msgPrice"));
				map.put("msg_amount", dbObj.get("msg_amount"));
				map.put("brick_amount", dbObj.get("brick_amount"));
				map.put("content", dbObj.get("content"));
				res_list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
