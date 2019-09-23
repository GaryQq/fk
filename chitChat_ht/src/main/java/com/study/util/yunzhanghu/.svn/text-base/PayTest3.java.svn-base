package com.study.util.yunzhanghu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
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

import com.alibaba.fastjson.JSONObject;
import com.study.config.ESTransportClient;
import com.study.util.tool.DateUtil;
import com.study.util.tool.StringUtil;

public class PayTest3 {
	private static TransportClient esClient = ESTransportClient.getInstance();
	private static Logger logger = LoggerFactory.getLogger(PayYunZhangHuUtil.class);

	
	private static String dealer_id = "05789476";
	private static String broker_id = "yiyun73";
	private static String appKey = "9Yj2BGPb6U0sGw9qT8ttpvxKN2jtNw96";
	private static String key = "381D99sZ6hxl2303QFOtoEa5";

	public static void main(String[] args) {
		try {
			String start_time = DateUtil.getPastDate(3) + " 00:00:00";
			String end_time = DateUtil.getStringDate();
			// 声明where 条件
			BoolQueryBuilder qbs = QueryBuilders.boolQuery();

			QueryBuilder qb1 = QueryBuilders.existsQuery("order_status");
			BoolQueryBuilder qbs1 = QueryBuilders.boolQuery().mustNot(qb1);
			qbs.must(qbs1);

			QueryBuilder qb2 = QueryBuilders.rangeQuery("create_date").from(start_time).format("yyyy-MM-dd HH:mm:ss")
					.to(end_time).format("yyyy-MM-dd HH:mm:ss").includeLower(true).includeUpper(true);
			BoolQueryBuilder qbs2 = QueryBuilders.boolQuery().must(qb2);
			qbs.must(qbs2);
			
			QueryBuilder qb3 = QueryBuilders.termQuery("order_id", "1538133781040474");
			BoolQueryBuilder qbs3 = QueryBuilders.boolQuery().must(qb3);
			qbs.must(qbs3);

			SearchRequestBuilder requestBuilder = esClient.prepareSearch("fk_pay_yunzhanghu")
					.setTypes("pay_yunzhanghu");
			requestBuilder.setQuery(qbs);

			SortBuilder dateSort = SortBuilders.fieldSort("create_date").order(SortOrder.DESC);
			requestBuilder.addSort(dateSort);

			SearchResponse esresponse = requestBuilder.setFrom(0).setSize(10000).execute().actionGet();
			SearchHits hits = esresponse.getHits();
			if (hits.getHits().length > 0) {
				for (SearchHit hit : hits) {
					Map<String, Object> val = hit.getSource();
					logger.info(hit.getSourceAsString());
					String order_id = StringUtil.nullBlank(val.get("order_id"));
					if (!StringUtil.eqBlank(order_id)) {
						StringBuilder sb = new StringBuilder();
						sb.append("{");
						sb.append("\"order_id\":").append("\"" + order_id + "\"");// 商户订单号
						sb.append("}");
						byte[] content = sb.toString().getBytes("utf-8");
						byte[] desKey = key.getBytes("utf-8");
						// 云支付加密
						byte[] enc = PayYunZhangHuUtil.TripleDesEncrypt(content, desKey);
						byte[] enc64 = Base64.encodeBase64(enc);
						logger.info("findPayOrderDetail=========>encrypt:" + new String(enc64));
						String data = new String(enc64);
						String mess = (int) ((Math.random() * 9 + 1) * 100000) + "";
						String timestamp = System.currentTimeMillis() / 1000 + "";

						String httpsHmac = "data=" + data + "&mess=" + mess + "&timestamp=" + timestamp + "&key="
								+ appKey;
						logger.info("findPayOrderDetail=========>httpsHmac:" + httpsHmac);
						String str = HMACSHA256.getHmacSha256(httpsHmac, appKey);
						logger.info("findPayOrderDetail=========>str:" + str);

						// send get example
						String url = "https://api-jiesuan.yunzhanghu.com/api/payment/v1/query-realtime-order";
						String paramGet = "?data=" + URLEncoder.encode(data, "UTF-8") + "&mess="
								+ URLEncoder.encode(mess, "UTF-8") + "&sign=" + URLEncoder.encode(str, "UTF-8")
								+ "&sign_type=sha256" + "&timestamp=" + URLEncoder.encode(timestamp, "UTF-8");
						url += paramGet;

						HttpClient client = new DefaultHttpClient();
						HttpGet request = new HttpGet(url);

						String request_id = (int) ((Math.random() * 9 + 1) * 100000) + "";
						logger.info("findPayOrderDetail=========>request_id:" + request_id);

						// 添加请求头
						request.setHeader("dealer-id", dealer_id);
						request.setHeader("request-id", request_id);

						HttpResponse response = client.execute(request);

						BufferedReader rd = new BufferedReader(
								new InputStreamReader(response.getEntity().getContent()));

						rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
						StringBuffer result = new StringBuffer();
						String line = "";
						result = new StringBuffer();
						while ((line = rd.readLine()) != null) {
							result.append(line);
						}
						logger.info("findPayOrderDetail=========>result:" + result.toString());
						JSONObject json = new JSONObject().parseObject(result.toString());
						String code = json.getString("code");
						String res_status = "";
						if ("0000".equals(code)) {
							JSONObject res_data = json.getJSONObject("data");
							res_status = res_data.getString("status");
						}
//						XContentBuilder xcb = XContentFactory.jsonBuilder().startObject();
//						xcb.field("order_result", result.toString());
//						xcb.field("order_status", res_status);
//						xcb.endObject();
//						UpdateResponse update_response = esClient
//								.update(new UpdateRequest("fk_pay_yunzhanghu", "pay_yunzhanghu", hit.getId()).doc(xcb))
//								.get();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
