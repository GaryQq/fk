package com.study.util.yunzhanghu;

import com.alibaba.fastjson.JSONObject;
import com.study.config.ESTransportClient;
import com.study.util.esUtil.ESClientUtil;
import com.study.util.tool.DateUtil;
import com.study.util.tool.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayYunZhangHuUtil {
    private static Logger logger = LoggerFactory.getLogger(PayYunZhangHuUtil.class);

    private static String dealer_id; // 商户平台ID(dealer_id)
    private static String broker_id; // 代征主体ID(broker_id)
    private static String appKey;
    private static String key;

    private static TransportClient esClient = ESTransportClient.getInstance();

    public static byte[] TripleDesEncrypt(byte[] content, byte[] key) throws Exception {
        byte[] icv = new byte[8];
        System.arraycopy(key, 0, icv, 0, 8);
        return TripleDesEncrypt(content, key, icv);
    }

    private static byte[] TripleDesEncrypt(byte[] content, byte[] key, byte[] icv) throws Exception {
        final SecretKey secretKey = new SecretKeySpec(key, "DESede");
        final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        final IvParameterSpec iv = new IvParameterSpec(icv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        return cipher.doFinal(content);
    }

    public static byte[] TripleDesDecrypt(byte[] content, byte[] key) throws Exception {
        byte[] icv = new byte[8];
        System.arraycopy(key, 0, icv, 0, 8);
        return TripleDesDecrypt(content, key, icv);
    }

    private static byte[] TripleDesDecrypt(byte[] content, byte[] key, byte[] icv) throws Exception {
        final SecretKey secretKey = new SecretKeySpec(key, "DESede");
        final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        final IvParameterSpec iv = new IvParameterSpec(icv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        return cipher.doFinal(content);
    }

    public static String payAccountTiXian(Map<String, Object> param) {
        String order_id = StringUtil.nullBlank(param.get("order_id"));// 订单ID
        String user_id = StringUtil.nullBlank(param.get("user_id"));// 用户唯一标识
        String real_name = StringUtil.nullBlank(param.get("real_name"));// 银行开户人
        String card_no = StringUtil.nullBlank(param.get("card_no"));// 银行卡号
        String pay = StringUtil.nullBlank(param.get("pay"));// 提现金额(元),最小0.1元
        String id_card = StringUtil.nullBlank(param.get("id_card"));// 开户人身份证
        String app_type = StringUtil.nullBlank(param.get("app_type"));
        StringBuffer result = new StringBuffer();

        // 提现账户区分根据app_type:空疯聊1红单2中超棋牌
        if ("".equals(app_type)) {
            dealer_id = "05789476";
            broker_id = "yiyun73";
            appKey = "9Yj2BGPb6U0sGw9qT8ttpvxKN2jtNw96";
            key = "381D99sZ6hxl2303QFOtoEa5";
        } else if ("1".equals(app_type)) {
            dealer_id = "01635946";
            broker_id = "yiyun73";
            appKey = "Hqn6R5lrxCfN05Ia9dI3Ls0G9eJ9aSi0";
            key = "v37pu94u5UT80THiNK9HdO2K";
        } else if ("2".equals(app_type)) {
            dealer_id = "07649374";
            broker_id = "yiyun73";
            appKey = "oGSXMphI2p93nQ61FWM250dfUT7W5MJx";
            key = "R45aY469E18aca31BHBeZT5k";
        }

        try {
            // 声明where 条件
            BoolQueryBuilder qbs = QueryBuilders.boolQuery();

            QueryBuilder qb1 = QueryBuilders.termQuery("order_id", order_id);
            BoolQueryBuilder qbs1 = QueryBuilders.boolQuery().must(qb1);
            qbs.must(qbs1);

            SearchRequestBuilder requestBuilder = esClient.prepareSearch("fk_pay_yunzhanghu")
                    .setTypes("pay_yunzhanghu");
            requestBuilder.setQuery(qbs);

            SortBuilder dateSort = SortBuilders.fieldSort("create_date").order(SortOrder.DESC);
            requestBuilder.addSort(dateSort);

            SearchResponse esresponse = requestBuilder.execute().actionGet();
            SearchHits hits = esresponse.getHits();
            if (hits.getHits().length > 0 && "".equals(app_type)) {// 有提现记录了,直接返回失败,避免重复提现
                StringBuilder res_str = new StringBuilder();
                res_str.append("{\"code:\":\"0099\",\"message:\":\"订单编号重复提现\"}");
                return res_str.toString();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"order_id\":").append("\"" + order_id + "\"").append(",");// 商户订单号
            sb.append("\"dealer_id\":").append("\"" + dealer_id + "\"").append(",");// 商户代码
            sb.append("\"broker_id\":").append("\"" + broker_id + "\"").append(",");// 代征主题
            sb.append("\"anchor_id\":").append("\"" + user_id + "\"").append(",");// 提现用户唯一标识
            sb.append("\"real_name\":").append("\"" + real_name + "\"").append(",");// 银行开户人
            sb.append("\"card_no\":").append("\"" + card_no + "\"").append(",");// 银行卡号
            sb.append("\"id_card\":").append("\"" + id_card + "\"").append(",");// 开户身份证号
            sb.append("\"pay\":").append("\"" + pay + "\"");// 提现金额(元),最小0.1元
            sb.append("}");
            byte[] content = sb.toString().getBytes("utf-8");
            byte[] desKey = key.getBytes("utf-8");
            // 云支付加密
            byte[] enc = PayYunZhangHuUtil.TripleDesEncrypt(content, desKey);
            byte[] enc64 = Base64.encodeBase64(enc);
            logger.info("payAccountTiXian=======>encrypt:" + new String(enc64));
            String data = new String(enc64);
            String mess = (int) ((Math.random() * 9 + 1) * 100000) + "";
            String timestamp = System.currentTimeMillis() / 1000 + "";

            String httpsHmac = "data=" + data + "&mess=" + mess + "&timestamp=" + timestamp + "&key=" + appKey;
            logger.info("payAccountTiXian=======>httpsHmac:" + httpsHmac);
            String str = HMACSHA256.getHmacSha256(httpsHmac, appKey);
            logger.info("payAccountTiXian=======>str:" + str);

            // send post example
            String url = "https://api-jiesuan.yunzhanghu.com/api/payment/v1/order-realtime";

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);

            String request_id = (int) ((Math.random() * 9 + 1) * 100000) + "";
            logger.info("payAccountTiXian=======>request_id:" + request_id);
            // 添加请求头
            post.setHeader("dealer-id", dealer_id);
            post.setHeader("request-id", request_id);

            List<NameValuePair> urlParameters;
            urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("data", data));
            urlParameters.add(new BasicNameValuePair("mess", mess));
            urlParameters.add(new BasicNameValuePair("timestamp", timestamp));
            urlParameters.add(new BasicNameValuePair("sign", str));
            urlParameters.add(new BasicNameValuePair("sign_type", "sha256"));

            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            logger.info(result.toString());

            try {
                // 处理记录保存到ES中,方便进行问题查询
                Map<String, Object> paramES = new HashMap<String, Object>();
                paramES.put("create_date", LocalDateTime.now());
                paramES.put("create_time", System.currentTimeMillis());
                paramES.put("param", sb);
                paramES.put("appKey", appKey);
                paramES.put("3deskey", desKey);
                paramES.put("httpsHmac", httpsHmac);
                paramES.put("sign", str);
                paramES.put("result", result);
                paramES.put("pay_amount", pay);
                paramES.put("user_id", user_id);
                paramES.put("order_id", order_id);
                ESClientUtil es = new ESClientUtil();
                es.createSource(paramES, "fk_pay_yunzhanghu", "pay_yunzhanghu");
            } catch (Exception e) {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static String findPayOrderDetail(Map<String, Object> param) {
        String order_id = StringUtil.nullBlank(param.get("order_id"));
        String app_type = StringUtil.nullBlank(param.get("app_type"));

        // 提现账户区分根据app_type:空疯聊1红单2中超棋牌
        if ("".equals(app_type)) {
            dealer_id = "05789476";
            broker_id = "yiyun73";
            appKey = "9Yj2BGPb6U0sGw9qT8ttpvxKN2jtNw96";
            key = "381D99sZ6hxl2303QFOtoEa5";
        } else if ("1".equals(app_type)) {
            dealer_id = "01635946";
            broker_id = "yiyun73";
            appKey = "Hqn6R5lrxCfN05Ia9dI3Ls0G9eJ9aSi0";
            key = "v37pu94u5UT80THiNK9HdO2K";
        } else if ("2".equals(app_type)) {
            dealer_id = "07649374";
            broker_id = "yiyun73";
            appKey = "oGSXMphI2p93nQ61FWM250dfUT7W5MJx";
            key = "R45aY469E18aca31BHBeZT5k";
        }

        try {
            if (! "".equals(app_type)) {
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

                String httpsHmac = "data=" + data + "&mess=" + mess + "&timestamp=" + timestamp + "&key=" + appKey;
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

                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

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
                return result.toString();
            } else {
                // 声明where 条件
                BoolQueryBuilder qbs = QueryBuilders.boolQuery();

                QueryBuilder qb1 = QueryBuilders.termQuery("order_id", order_id);
                BoolQueryBuilder qbs1 = QueryBuilders.boolQuery().must(qb1);
                qbs.must(qbs1);

                SearchRequestBuilder requestBuilder = esClient.prepareSearch("fk_pay_yunzhanghu")
                        .setTypes("pay_yunzhanghu");
                requestBuilder.setQuery(qbs);

                SortBuilder dateSort = SortBuilders.fieldSort("create_date").order(SortOrder.DESC);
                requestBuilder.addSort(dateSort);

                SearchResponse esresponse = requestBuilder.execute().actionGet();
                SearchHits hits = esresponse.getHits();
                if (hits.getHits().length > 0) {
                    SearchHit hit = hits.getHits()[0];
                    Map<String, Object> val = hit.getSource();
                    if ("1".equals(val.get("order_status"))) {
                        return StringUtil.nullBlank(val.get("order_result"));
                    } else {
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
                        XContentBuilder xcb = XContentFactory.jsonBuilder().startObject();
                        xcb.field("order_result", result.toString());
                        xcb.field("order_status", res_status);
                        xcb.endObject();
                        UpdateResponse update_response = esClient
                                .update(new UpdateRequest("fk_pay_yunzhanghu", "pay_yunzhanghu", hit.getId()).doc(xcb))
                                .get();
                        return result.toString();
                    }
                } else {
                    StringBuilder res_str = new StringBuilder();
                    res_str.append("{\"code:\":\"0099\",\"message:\":\"订单编号不存在\"}");
                    return res_str.toString();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String findPayTiXianDetail(Map<String, Object> param) {
        String app_type = StringUtil.nullBlank(param.get("app_type"));
        // 提现账户区分根据app_type:空疯聊1红单2中超棋牌
        if ("".equals(app_type)) {
            dealer_id = "05789476";
            broker_id = "yiyun73";
            appKey = "9Yj2BGPb6U0sGw9qT8ttpvxKN2jtNw96";
            key = "381D99sZ6hxl2303QFOtoEa5";
        } else if ("1".equals(app_type)) {
            dealer_id = "01635946";
            broker_id = "yiyun73";
            appKey = "Hqn6R5lrxCfN05Ia9dI3Ls0G9eJ9aSi0";
            key = "v37pu94u5UT80THiNK9HdO2K";
        } else if ("2".equals(app_type)) {
            dealer_id = "07649374";
            broker_id = "yiyun73";
            appKey = "oGSXMphI2p93nQ61FWM250dfUT7W5MJx";
            key = "R45aY469E18aca31BHBeZT5k";
        }

        try {

            String start_time = DateUtil.getPastDate(3) + " 00:00:00";
            String end_time = DateUtil.getStringDate();
            // 声明where 条件
            BoolQueryBuilder qbs = QueryBuilders.boolQuery();

            QueryBuilder qb1 = QueryBuilders.termQuery("order_status", "1");
            BoolQueryBuilder qbs1 = QueryBuilders.boolQuery().mustNot(qb1);
            qbs.must(qbs1);

            QueryBuilder qb2 = QueryBuilders.rangeQuery("create_date").from(start_time).format("yyyy-MM-dd HH:mm:ss")
                    .to(end_time).format("yyyy-MM-dd HH:mm:ss").includeLower(true).includeUpper(true);
            BoolQueryBuilder qbs2 = QueryBuilders.boolQuery().must(qb2);
            qbs.must(qbs2);

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
                    String order_id = StringUtil.nullBlank(val.get("order_id"));
                    if (! StringUtil.eqBlank(order_id)) {
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
                        XContentBuilder xcb = XContentFactory.jsonBuilder().startObject();
                        xcb.field("order_result", result.toString());
                        xcb.field("order_status", res_status);
                        xcb.endObject();
                        UpdateResponse update_response = esClient
                                .update(new UpdateRequest("fk_pay_yunzhanghu", "pay_yunzhanghu", hit.getId()).doc(xcb))
                                .get();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
