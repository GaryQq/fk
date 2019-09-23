package com.study.controller.fengliaoAgency;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.config.ESTransportClient;
import com.study.model.permissions.User;
import com.study.service.fengliaoAgency.FengLiaoFamilyService;
import com.study.service.lotterytie.LotterytieService;
import com.study.util.ListGroupBy;
import com.study.util.bean.DataGridResultInfo;
import com.study.util.bean.PageBean;
import com.study.util.esUtil.ESClientUtil;
import com.study.util.permissions.PageBeanUtil;
import com.study.util.permissions.ResultUtil;
import com.study.util.tool.DateUtil;
import com.study.util.tool.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumBuilder;
import org.elasticsearch.search.aggregations.metrics.valuecount.InternalValueCount;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author nieht
 * @Title: FengLiaoFamilyController
 * @ProjectName chitChat_ht
 * @Description: 疯聊家族相关功能
 * @date 2019/3/6 14:10
 */
@Api(value = "FengLiaoFamilyController", description = "疯聊家族相关功能")
@RestController
@RequestMapping("/flFamily")
public class FengLiaoFamilyController {

    private static Logger logger = LoggerFactory.getLogger(FengLiaoFamilyController.class);

    @Autowired
    private FengLiaoFamilyService fengLiaoFamilyService;

    @Autowired
    private LotterytieService lotterytieService;

    //ES连接声明
    private static TransportClient esClient = ESTransportClient.getInstance();

    private List<String> user_list = new ArrayList<>();

    @ApiOperation(value = "当前明细1对1", notes = "当前明细1对1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nick_name", value = "昵称", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "start_time", value = "开始时间", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "结束时间", required = false, dataType = "string", paramType = "query") })
    @RequestMapping(value = "/findCurrentDetailOneToOneList", method = { RequestMethod.GET })
    public DataGridResultInfo findCurrentDetailOneToOneList(HttpServletRequest request,
                                                            @RequestParam(value = "nick_name", required = false) String nick_name,
                                                            @RequestParam(value = "start_time", required = false) String start_time,
                                                            @RequestParam(value = "end_time", required = false) String end_time, @ModelAttribute PageBean bean) {
        // 如果开始时间与结束时间为空查询时间在前7天之内,到当前时间
        if (StringUtil.eqBlank(start_time)) {
            start_time = DateUtil.getPastDate(7);
        }
        if (StringUtil.eqBlank(end_time)) {
            end_time = DateUtil.getFetureDate(1);
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("nick_name", nick_name);
        param.put("start_time", start_time);
        param.put("end_time", end_time);
        // 查询当前登录用户关联的fk账号
        Session session = SecurityUtils.getSubject().getSession();
        User user = (User) session.getAttribute("userSession");
        String fkUserName = StringUtil.nullBlank(user.getFkUserName());
        param.put("fkUserName", fkUserName);
        List<Map<String, Object>> findViewList = fengLiaoFamilyService.findCurrentDetailOneToOneList(param, bean);
        List<String> anchor_list = new ArrayList<String>();
        for (Map<String, Object> map : findViewList) {
            Map<String, Object> res_map = new HashMap<String, Object>();
            String user_name = StringUtil.nullBlank(map.get("USER_NAME"));
            anchor_list.add(user_name);
        }
        param.put("anchor_list", anchor_list);
        user_list = lotterytieService.findWhiteUserList();
        Map<String, Object> objectMap = new HashMap<String, Object>();
        Map<String, Object> account_map = new HashMap<String, Object>();
        List<Map<String, Object>> res_list = new ArrayList<>();
        for (Map<String, Object> map : findViewList) {
            Map<String, Object> res_map = new HashMap<String, Object>();
            String user_name = StringUtil.nullBlank(map.get("USER_NAME"));
            param.put("user_name", user_name);
            param.put("type", "call_num");
            objectMap = findOneToOneDetailEntity(param);
            double call_num = StringUtil.nullDouble(objectMap.get("call_num"));//查询通话次数
            param.put("type", "time");
            objectMap = findOneToOneDetailEntity(param);
            double viode_time = StringUtil.nullDouble(objectMap.get("viode_time"));//视频时长
            double voice_time = StringUtil.nullDouble(objectMap.get("voice_time"));//语音时长
            double collect_time = viode_time + voice_time;//音视频时长
            param.put("type", "all_num");
            objectMap = findOneToOneDetailEntity(param);
            double all_num = StringUtil.nullDouble(objectMap.get("all_num"));//对当前用户发起一对一次数
            param.put("type", "connect_num");
            objectMap = findOneToOneDetailEntity(param);
            double connect_num = StringUtil.nullDouble(objectMap.get("connect_num"));//查询对当前用户发起一对一接通次数
            double rate = all_num == 0 ? 0d : (new BigDecimal(connect_num).multiply(new BigDecimal(100))).divide(new BigDecimal(all_num), 2, BigDecimal.ROUND_HALF_UP).doubleValue();//接通率

            account_map = findAyoiAccountDetailEntity(param);
            double voice_amount = StringUtil.nullDouble(account_map.get("voice_amount"));//一对一语音金币扣款
            double viode_amount = StringUtil.nullDouble(account_map.get("viode_amount"));//一对一视频金币扣款
            double collect_amount = voice_amount + viode_amount;//音视频金币
            double msg_amount = StringUtil.nullDouble(account_map.get("msg_amount"));//一对一私信金币扣款
            double gift_amount = StringUtil.nullDouble(account_map.get("gift_amount"));//礼物扣款
            double private_amount = StringUtil.nullDouble(account_map.get("private_amount"));//私照扣款
            double look_die_amount = StringUtil.nullDouble(account_map.get("look_die_amount"));//阅后即焚扣款
            double amount = voice_amount + viode_amount + msg_amount + gift_amount + private_amount + look_die_amount;//总金币数

            res_map.put("nick_name", ESClientUtil.findYTUserEntity(user_name).get("nick_name"));
            res_map.put("call_num", call_num);
            res_map.put("voice_time", new BigDecimal(voice_time).divide(new BigDecimal(3600), 2, BigDecimal.ROUND_HALF_UP) + "(h)");
            res_map.put("viode_time", new BigDecimal(viode_time).divide(new BigDecimal(3600), 2, BigDecimal.ROUND_HALF_UP) + "(h)");
            res_map.put("collect_time", new BigDecimal(collect_time).divide(new BigDecimal(3600), 2, BigDecimal.ROUND_HALF_UP) + "(h)");
            res_map.put("voice_amount", voice_amount);
            res_map.put("viode_amount", viode_amount);
            res_map.put("collect_amount", collect_amount);
            res_map.put("msg_amount", msg_amount);
            res_map.put("gift_amount", gift_amount);
            res_map.put("amount", amount);
            res_map.put("connect_num", connect_num);
            res_map.put("all_num", all_num);
            res_map.put("private_amount", private_amount);
            res_map.put("look_die_amount", look_die_amount);
            res_map.put("rate", rate + "(%)");
            res_list.add(res_map);
        }
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(res_list, 1);

        List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
        Map<String, Object> footerMap = new HashMap<String, Object>();
        footerMap.put("nick_name", "合计");
        footerMap.put("call_num", "0");
        footerList.add(footerMap);
        return ResultUtil.createDataGridFooterResult(pageInfo.getTotal(), pageInfo.getList(), footerList);
    }

    @ApiOperation(value = "音视频通话明细", notes = "音视频通话明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nick_name", value = "昵称", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "by_status", value = "接听", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "noby_status", value = "拒接", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "start_time", value = "开始时间", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "结束时间", required = false, dataType = "string", paramType = "query") })
    @RequestMapping(value = "/findFamilyOneToOneDetailList", method = { RequestMethod.GET })
    public DataGridResultInfo findFamilyOneToOneDetailList(HttpServletRequest request,
                                                           @RequestParam(value = "nick_name", required = false) String nick_name,
                                                           @RequestParam(value = "by_status", required = false) String by_status,
                                                           @RequestParam(value = "noby_status", required = false) String noby_status,
                                                           @RequestParam(value = "start_time", required = false) String start_time,
                                                           @RequestParam(value = "end_time", required = false) String end_time, @ModelAttribute PageBean bean) {
        // 如果开始时间与结束时间为空查询时间在前7天之内,到当前时间
        if (StringUtil.eqBlank(start_time)) {
            start_time = DateUtil.getPastDate(7);
        }
        if (StringUtil.eqBlank(end_time)) {
            end_time = DateUtil.getFetureDate(1);
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("nick_name", nick_name);
        param.put("start_time", start_time + " 00:00:00");
        param.put("end_time", end_time + " 23:59:59");
        // 查询当前登录用户关联的fk账号
        Session session = SecurityUtils.getSubject().getSession();
        User user = (User) session.getAttribute("userSession");
        String fkUserName = StringUtil.nullBlank(user.getFkUserName());
        param.put("fkUserName", fkUserName);
        List<Map<String, Object>> findViewList = fengLiaoFamilyService.findFamilyOneToOneDetailList(param, bean);

        user_list = lotterytieService.findWhiteUserList();

        if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
            PageHelper.startPage(bean.getPage(), bean.getRows());
        }

        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(findViewList, 1);

        List<String> anchor_list = new ArrayList<String>();
        for (Map<String, Object> map : findViewList) {
            Map<String, Object> res_map = new HashMap<String, Object>();
            String user_name = StringUtil.nullBlank(map.get("USER_NAME"));
            anchor_list.add(user_name);
        }
        param.put("anchor_list", anchor_list);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        QueryBuilder date_qb = QueryBuilders.rangeQuery("create_date").from(start_time).to(end_time)
                .includeLower(true).includeUpper(true);
        BoolQueryBuilder date_qbs = QueryBuilders.boolQuery().must(date_qb);
        boolQueryBuilder.must(date_qbs);

        QueryBuilder user_name_qb = QueryBuilders.termsQuery("user_name", user_list);
        BoolQueryBuilder user_name_bqb = QueryBuilders.boolQuery().mustNot(user_name_qb);
        boolQueryBuilder.must(user_name_bqb);

        QueryBuilder anchor_name_qb = QueryBuilders.termsQuery("anchor_name", anchor_list);
        BoolQueryBuilder anchor_name_bqb = QueryBuilders.boolQuery().must(anchor_name_qb);
        boolQueryBuilder.must(anchor_name_bqb);

        if ("1".equals(by_status)) {// 已接通
            QueryBuilder start_date_qb = QueryBuilders.existsQuery("start_date");
            BoolQueryBuilder start_date_bqb = QueryBuilders.boolQuery().must(start_date_qb);
            boolQueryBuilder.must(start_date_bqb);
        } else if ("2".equals(by_status)) {// 未接通
            QueryBuilder start_date_qb = QueryBuilders.existsQuery("start_date");
            BoolQueryBuilder start_date_bqb = QueryBuilders.boolQuery().mustNot(start_date_qb);
            boolQueryBuilder.must(start_date_bqb);
        }
        if ("1".equals(noby_status)) {// 是
            QueryBuilder status_qb = QueryBuilders.termQuery("status", "4");
            BoolQueryBuilder status_bqb = QueryBuilders.boolQuery().must(status_qb);
            boolQueryBuilder.must(status_bqb);
        } else if ("2".equals(noby_status)) {// 否
            QueryBuilder status_qb = QueryBuilders.termQuery("status", "4");
            BoolQueryBuilder status_bqb = QueryBuilders.boolQuery().mustNot(status_qb);
            boolQueryBuilder.must(status_bqb);
        }

        SearchRequestBuilder requestBuilder = esClient.prepareSearch("fk_onetoone_detail").setTypes("onetoone_detail");
        requestBuilder.setQuery(boolQueryBuilder);
        SortBuilder statusBuilder = SortBuilders.fieldSort("create_date").order(SortOrder.DESC);
        requestBuilder.addSort(statusBuilder);

        SearchResponse response = requestBuilder.setFrom((bean.getPage() - 1) * bean.getRows())
                .setSize(bean.getRows()).execute().actionGet();

        pageInfo.setTotal(response.getHits().getTotalHits());
        SearchHits hits = response.getHits();
        List<Map<String, Object>> res_list = new ArrayList<Map<String, Object>>();
        for (SearchHit hit : hits) {
            Map<String, Object> val = hit.getSource();
            String nick_name_view = StringUtil.nullBlank(val.get("user_nick_name"));//发起者昵称
            if ("".equals(nick_name_view)) {
                String user_name = StringUtil.nullBlank(val.get("user_name"));//发起者账户
                Map<String, Object> user_param = new HashMap<String, Object>();
                user_param.put("user_name", user_name);
                nick_name_view = StringUtil.nullBlank(lotterytieService.findUser(user_param).get("nick_name"));
            }
            String create_time = StringUtil.nullBlank(val.get("create_date"));//发起时间
            String type = StringUtil.nullBlank(val.get("find_type"));//类型1视频2语音
            String answer_create_time = StringUtil.nullBlank(val.get("start_date"));//接通时间
            String statusr = StringUtil.nullBlank(val.get("status"));//备注
            String status = "";//是否接通
            String statue2 = ""; //是否拒绝
            if (! answer_create_time.isEmpty()) {
                status = "1";
            } else if (statusr.equals("4")) {
                statue2 = "-1";
            }
            String anchor_nick_name = StringUtil.nullBlank(val.get("anchor_nick_name"));//接听者昵称
            if ("".equals(anchor_nick_name)) {
                String user_name = StringUtil.nullBlank(val.get("anchor_name"));//发起者昵称
                Map<String, Object> user_param = new HashMap<String, Object>();
                user_param.put("user_name", user_name);
                anchor_nick_name = StringUtil.nullBlank(lotterytieService.findUser(user_param).get("nick_name"));
            }
            String time = "0";//通话时长
            String end_date = StringUtil.nullBlank(val.get("end_date"));
            if (! end_date.isEmpty() && ! answer_create_time.isEmpty()) {
                try {
                    Date a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(answer_create_time);
                    Date b = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end_date);
                    long interval = (b.getTime() - a.getTime()) / 1000;
                    time = StringUtil.nullBlank(interval);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String fqtime = "0";//发起时长
            if (! end_date.isEmpty() && ! create_time.isEmpty()) {
                try {
                    Date a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(create_time);
                    Date b = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end_date);
                    long interval = (b.getTime() - a.getTime()) / 1000;
                    fqtime = StringUtil.nullBlank(interval);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            double price = StringUtil.nullDouble(val.get("price"));//接听者价格
            Integer coun = (StringUtil.nullZero(time) / 60) + 1;//通话扣款次数
            Integer goal_amount = (new Double(price)).intValue() * coun;//总扣款金额

            Map<String, Object> res_map = new HashMap<String, Object>();
            res_map.put("nick_name", nick_name_view);
            res_map.put("create_time", create_time);
            res_map.put("type", type);
            res_map.put("answer_create_time", answer_create_time);
            res_map.put("statusr", statusr);
            res_map.put("status", status);
            res_map.put("statue2", statue2);
            res_map.put("anchor_nick_name", anchor_nick_name);
            res_map.put("time", time);
            res_map.put("fqtime", fqtime);
            res_map.put("price", price);
            res_map.put("goal_amount", ! "1".equals(status) ? 0 : goal_amount);
            res_list.add(res_map);
        }
        pageInfo.setList(res_list);
        List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
        Map<String, Object> footerMap = new HashMap<String, Object>();
        footerMap.put("nick_name", "合计");
        footerList.add(footerMap);
        return ResultUtil.createDataGridFooterResult(pageInfo.getTotal(), pageInfo.getList(), footerList);
    }

    @ApiOperation(value = "订单明细", notes = "订单明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nick_name", value = "昵称", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "skill_id", value = "技能ID", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "order_status", value = "订单状态", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "start_time", value = "开始时间", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "结束时间", required = false, dataType = "string", paramType = "query") })
    @RequestMapping(value = "/findFamilyOrderDetailList", method = { RequestMethod.GET })
    public DataGridResultInfo findFamilyOrderDetailList(HttpServletRequest request,
                                                        @RequestParam(value = "nick_name", required = false) String nick_name,
                                                        @RequestParam(value = "skill_id", required = false) String skill_id,
                                                        @RequestParam(value = "order_status", required = false) String order_status,
                                                        @RequestParam(value = "start_time", required = false) String start_time,
                                                        @RequestParam(value = "end_time", required = false) String end_time, @ModelAttribute PageBean bean) {
        // 如果开始时间与结束时间为空查询时间在前7天之内,到当前时间
        if (StringUtil.eqBlank(start_time)) {
            start_time = DateUtil.getPastDate(7);
        }
        if (StringUtil.eqBlank(end_time)) {
            end_time = DateUtil.getFetureDate(1);
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("nick_name", nick_name);
        param.put("skill_id", skill_id);
        param.put("order_status", order_status);
        param.put("start_time", start_time + " 00:00:00");
        param.put("end_time", end_time + " 23:59:59");
        // 查询当前登录用户关联的fk账号
        Session session = SecurityUtils.getSubject().getSession();
        User user = (User) session.getAttribute("userSession");
        String fkUserName = StringUtil.nullBlank(user.getFkUserName());
        param.put("fkUserName", fkUserName);
        List<Map<String, Object>> findViewList = fengLiaoFamilyService.findFamilyOrderDetailList(param, bean);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(findViewList, 1);
        List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
        Map<String, Object> footerMap = new HashMap<String, Object>();
        footerMap.put("nick_name", "合计");
        footerMap.put("call_num", "0");
        footerList.add(footerMap);
        return ResultUtil.createDataGridFooterResult(pageInfo.getTotal(), pageInfo.getList(), footerList);
    }


    @ApiOperation(value = "每日数据", notes = "每日数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start_time", value = "开始时间", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "结束时间", required = false, dataType = "string", paramType = "query") })
    @RequestMapping(value = "/findFamilyEveryDayList", method = { RequestMethod.GET })
    public DataGridResultInfo findFamilyEveryDayList(HttpServletRequest request,
                                                     @RequestParam(value = "start_time", required = false) String start_time,
                                                     @RequestParam(value = "end_time", required = false) String end_time, @ModelAttribute PageBean bean) {

        // 如果开始时间与结束时间为空查询时间在前7天之内,到当前时间
        if (StringUtil.eqBlank(start_time)) {
            start_time = DateUtil.getPastDate(7);
        }
        if (StringUtil.eqBlank(end_time)) {
            end_time = DateUtil.getFetureDate(0);
        }
        //当前日期小于3月1号则开始时间为3月1号
        if (DateUtil.compareDateShort(start_time, "2019-03-01") < 0) {
            start_time = "2019-03-01";
        }

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("start_time", start_time);
        param.put("end_time", end_time);
        // 查询当前登录用户关联的fk账号
        Session session = SecurityUtils.getSubject().getSession();
        User user = (User) session.getAttribute("userSession");
        String fkUserName = StringUtil.nullBlank(user.getFkUserName());
        param.put("fkUserName", fkUserName);
        List<String> anchor_list = new ArrayList<>();
        List<Map<String, Object>> findViewList = fengLiaoFamilyService.findFamilyOneToOneDetailList(param, bean);
        user_list = lotterytieService.findWhiteUserList();
        for (Map<String, Object> map : findViewList) {
            String user_name = StringUtil.nullBlank(map.get("USER_NAME"));
            anchor_list.add(user_name);
        }
        param.put("anchor_list", anchor_list);
        String every_day = end_time;
        String start_date = end_time + " 00:00:00";
        String end_date = end_time + " 23:59:59";
        Map<String, Object> onetoone_map = new HashMap<String, Object>();
        Map<String, Object> account_map = new HashMap<String, Object>();
        List<Map<String, Object>> res_list = new ArrayList<Map<String, Object>>();
        if (anchor_list.size() > 0) {
            for (int i = 1; i < 900; i++) {
                param.put("start_time", start_date);
                param.put("end_time", end_date);

                param.put("type", "time");
                onetoone_map = findOneToOneDetailEntity(param);
                double viode_time = StringUtil.nullDouble(onetoone_map.get("viode_time"));//视频时长
                double voice_time = StringUtil.nullDouble(onetoone_map.get("voice_time"));//语音时长
                double collect_time = viode_time + voice_time;//音视频时长

                param.put("type", "group_connect_num");
                onetoone_map = findOneToOneDetailEntity(param);
                double viode_num = StringUtil.nullDouble(onetoone_map.get("viode_num"));//视频接通次数
                double voice_num = StringUtil.nullDouble(onetoone_map.get("voice_num"));//语音接通次数
                param.put("type", "group_all_num");
                onetoone_map = findOneToOneDetailEntity(param);
                double viode_all_num = StringUtil.nullDouble(onetoone_map.get("viode_all_num"));// 查询视频发起次数
                double voice_all_num = StringUtil.nullDouble(onetoone_map.get("voice_all_num"));// 查询语音发起次数

                double viode_rate = viode_all_num == 0 ? 0d : (new BigDecimal(viode_num).multiply(new BigDecimal(100)))
                        .divide(new BigDecimal(viode_all_num), 2, BigDecimal.ROUND_HALF_UP).doubleValue();//视频接通率

                double voice_rate = voice_all_num == 0 ? 0d : (new BigDecimal(voice_num).multiply(new BigDecimal(100)))
                        .divide(new BigDecimal(voice_all_num), 2, BigDecimal.ROUND_HALF_UP).doubleValue();//语音接通率

                param.put("type", "group_repulse_num");
                onetoone_map = findOneToOneDetailEntity(param);

                double viode_repulse_num = StringUtil.nullDouble(onetoone_map.get("viode_repulse_num"));//视频拒绝次数
                double voice_repulse_num = StringUtil.nullDouble(onetoone_map.get("voice_repulse_num"));//语音拒绝次数

                double viode_repulse_rate = viode_all_num == 0 ? 0d : (new BigDecimal(viode_repulse_num).multiply(new BigDecimal(100)))
                        .divide(new BigDecimal(viode_all_num), 2, BigDecimal.ROUND_HALF_UP).doubleValue();//视频拒接率
                double voice_repulse_rate = voice_all_num == 0 ? 0d : (new BigDecimal(voice_repulse_num).multiply(new BigDecimal(100)))
                        .divide(new BigDecimal(voice_all_num), 2, BigDecimal.ROUND_HALF_UP).doubleValue();//语音拒接率
                account_map = findAyoiAccountDetailEntity(param);
                double viode_amount = StringUtil.nullDouble(account_map.get("viode_amount"));//一对一视频金币扣款
                double voice_amount = StringUtil.nullDouble(account_map.get("voice_amount"));//一对一语音金币扣款
                double msg_amount = StringUtil.nullDouble(account_map.get("msg_amount"));//一对一私信金币扣款
                double gift_amount = StringUtil.nullDouble(account_map.get("gift_amount"));//礼物扣款
                double private_amount = StringUtil.nullDouble(account_map.get("private_amount"));//私照扣款
                double look_die_amount = StringUtil.nullDouble(account_map.get("look_die_amount"));//阅后即焚扣款
                double amount = voice_amount + viode_amount + msg_amount + gift_amount + private_amount + look_die_amount;//总金币数

                Map<String, Object> res_map = new HashMap<String, Object>();
                res_map.put("every_day", every_day);//日期
                res_map.put("collect_time", new BigDecimal(collect_time).divide(new BigDecimal(3600), 2, BigDecimal.ROUND_HALF_UP) + "(h)");//直播时长
                res_map.put("viode_num", viode_num);//视频接通次数
                res_map.put("voice_num", voice_num);//语音接通次数
                res_map.put("viode_all_num", viode_all_num);//视频发起次数
                res_map.put("voice_all_num", voice_all_num);//语音发起次数
                res_map.put("viode_rate", viode_rate + "(%)");//视频接通率
                res_map.put("voice_rate", voice_rate + "(%)");//语音接通率
                res_map.put("viode_repulse_rate", viode_repulse_rate + "(%)");//视频拒接率
                res_map.put("voice_repulse_rate", voice_repulse_rate + "(%)");//音频拒接率
                res_map.put("viode_amount", viode_amount);//一对一视频金币扣款
                res_map.put("voice_amount", voice_amount);//一对一语音金币扣款
                res_map.put("msg_amount", msg_amount);//一对一私信金币扣款
                res_map.put("gift_amount", gift_amount);//礼物扣款
                res_map.put("private_amount", private_amount);//私照扣款
                res_map.put("look_die_amount", look_die_amount);//阅后即焚扣款
                res_map.put("amount", amount);//累计消耗金币数
                res_list.add(res_map);
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(end_time));
                    calendar.add(Calendar.DAY_OF_MONTH, - i);
                    Date today = calendar.getTime();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String result = format.format(today);
                    if (DateUtil.compareDateShort(result, start_time) < 0) {
                        break;
                    }
                    start_date = result + " 00:00:00";
                    end_date = result + " 23:59:59";
                    every_day = result;
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
            PageHelper.startPage(bean.getPage(), bean.getRows());
        }

        ListGroupBy<Map<String, Object>> listPageUtil = new ListGroupBy<Map<String, Object>>(res_list, bean.getPage(),
                bean.getRows());
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(findViewList, 1);
        pageInfo.setTotal(res_list.size());
        pageInfo.setList(listPageUtil.getPagedList());
        List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
        Map<String, Object> footerMap = new HashMap<String, Object>();
        footerMap.put("nick_name", "合计");
        footerMap.put("call_num", "0");
        footerList.add(footerMap);
        return ResultUtil.createDataGridFooterResult(pageInfo.getTotal(), pageInfo.getList(), footerList);

    }

    @ApiOperation(value = "技能列表", notes = "技能列表API")
    @RequestMapping(value = "/findSkillList", method = { RequestMethod.POST })
    public String findSkillList(HttpServletRequest request) {
        Map<String, Object> param = new HashMap<String, Object>();
        List<Map<String, Object>> skill_list = fengLiaoFamilyService.findSkillList(param);
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(skill_list));
        return jsonArray.toString();
    }

    /**
     * @Description: 查询金币明细数据
     * @Author: nieht
     * @Date: 2019/3/7 9:54
     */
    private Map<String, Object> findAyoiAccountDetailEntity(Map<String, Object> param) {
        String start_time = StringUtil.nullBlank(param.get("start_time"));
        String end_time = StringUtil.nullBlank(param.get("end_time"));
        String user_name = StringUtil.nullBlank(param.get("user_name"));
        List<String> anchor_list = (List<String>) param.get("anchor_list");
        Map<String, Object> res_map = new HashMap<String, Object>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        QueryBuilder date_qb = QueryBuilders.rangeQuery("create_date").from(start_time).to(end_time)
                .includeLower(true).includeUpper(true);
        BoolQueryBuilder date_qbs = QueryBuilders.boolQuery().must(date_qb);
        boolQueryBuilder.must(date_qbs);

        QueryBuilder user_name_qb = QueryBuilders.termsQuery("user_name", user_list);
        BoolQueryBuilder user_name_bqb = QueryBuilders.boolQuery().mustNot(user_name_qb);
        boolQueryBuilder.must(user_name_bqb);

        if (! user_name.isEmpty()) {
            QueryBuilder anchor_name_qb = QueryBuilders.termQuery("anchor_name", user_name);
            BoolQueryBuilder anchor_name_bqb = QueryBuilders.boolQuery().must(anchor_name_qb);
            boolQueryBuilder.must(anchor_name_bqb);
        }

        QueryBuilder anchor_name_qb = QueryBuilders.termsQuery("anchor_name", anchor_list);
        BoolQueryBuilder anchor_name_bqb = QueryBuilders.boolQuery().must(anchor_name_qb);
        boolQueryBuilder.must(anchor_name_bqb);

        TermsBuilder group_opt_type = AggregationBuilders.terms("group_opt_type").field("opt_type");
        SumBuilder sum_amount = AggregationBuilders.sum("sum_amount").field("amount");

        SearchRequestBuilder aggRequestBuilder = esClient.prepareSearch("fk_ayoi_account_detail")
                .setTypes("ayoi_account_detail");
        aggRequestBuilder.setQuery(boolQueryBuilder);
        aggRequestBuilder.addAggregation(group_opt_type.subAggregation(sum_amount));

        SearchResponse aggResponse = aggRequestBuilder.setSize(0).execute().actionGet();
        Terms terms = aggResponse.getAggregations().get("group_opt_type");
        Iterator<Terms.Bucket> gradeBucketIt = terms.getBuckets().iterator();
        double user_amount = 0d;//当前用户消费金额
        while (gradeBucketIt.hasNext()) {
            Terms.Bucket gradeBucket = gradeBucketIt.next();
            InternalSum internal_sumAmount = gradeBucket.getAggregations().get("sum_amount");
            String opt_type = StringUtil.nullBlank(gradeBucket.getKey());//消费类型
            user_amount = internal_sumAmount.getValue();
            if ("66".equals(opt_type)) {//一对一语音金币扣款
                res_map.put("voice_amount", user_amount);
            } else if ("67".equals(opt_type)) {//一对一视频金币扣款
                res_map.put("viode_amount", user_amount);
            } else if ("68".equals(opt_type)) {//一对一私信金币扣款
                res_map.put("msg_amount", user_amount);
            } else if ("72".equals(opt_type)) {//礼物扣款
                res_map.put("gift_amount", user_amount);
            } else if ("159".equals(opt_type)) {//私照扣款
                res_map.put("private_amount", user_amount);
            } else if ("160".equals(opt_type)) {//阅后即焚
                res_map.put("look_die_amount", user_amount);
            }
        }
        return res_map;
    }

    /**
     * @Description: 查询一对一相关数据
     * @Author: nieht
     * @Date: 2019/3/7 9:54
     */
    private Map<String, Object> findOneToOneDetailEntity(Map<String, Object> param) {
        Map<String, Object> res_map = new HashMap<String, Object>();
        String start_time = StringUtil.nullBlank(param.get("start_time"));
        String end_time = StringUtil.nullBlank(param.get("end_time"));
        String user_name = StringUtil.nullBlank(param.get("user_name"));
        String type = StringUtil.nullBlank(param.get("type"));
        List<String> anchor_list = (List<String>) param.get("anchor_list");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        QueryBuilder date_qb = QueryBuilders.rangeQuery("create_date").from(start_time).to(end_time)
                .includeLower(true).includeUpper(true);
        BoolQueryBuilder date_qbs = QueryBuilders.boolQuery().must(date_qb);
        boolQueryBuilder.must(date_qbs);

        QueryBuilder app_type_qb = QueryBuilders.termsQuery("app_type", Arrays.asList("2", "6"));
        BoolQueryBuilder app_type_bqb = QueryBuilders.boolQuery().must(app_type_qb);
        boolQueryBuilder.must(app_type_bqb);

        QueryBuilder user_name_qb = QueryBuilders.termsQuery("user_name", user_list);
        BoolQueryBuilder user_name_bqb = QueryBuilders.boolQuery().mustNot(user_name_qb);
        boolQueryBuilder.must(user_name_bqb);

        if (! user_name.isEmpty()) {
            QueryBuilder anchor_name_qb = QueryBuilders.termQuery("anchor_name", user_name);
            BoolQueryBuilder anchor_name_bqb = QueryBuilders.boolQuery().must(anchor_name_qb);
            boolQueryBuilder.must(anchor_name_bqb);
        }

        QueryBuilder anchor_name_qb = QueryBuilders.termsQuery("anchor_name", anchor_list);
        BoolQueryBuilder anchor_name_bqb = QueryBuilders.boolQuery().must(anchor_name_qb);
        boolQueryBuilder.must(anchor_name_bqb);

        SearchRequestBuilder requestBuilder = null;
        switch (type) {
            case "call_num":
                /**
                 * @Description: 查询通话次数，开始时间不为空
                 * @Author: nieht
                 * @Date: 2019/3/6 16:12
                 */
                QueryBuilder start_date_qb = QueryBuilders.existsQuery("start_date");
                BoolQueryBuilder start_date_bqb = QueryBuilders.boolQuery().must(start_date_qb);
                boolQueryBuilder.must(start_date_bqb);
                break;
            case "all_num":
                /**
                 * @Description: 对当前用户发起一对一次数
                 * @Author: nieht
                 * @Date: 2019/3/6 17:38
                 */
                break;
            case "connect_num":
                /**
                 * @Description: 查询对当前用户发起一对一接通次数
                 * @Author: nieht
                 * @Date: 2019/3/6 17:43
                 */
                QueryBuilder start_date_qb2 = QueryBuilders.existsQuery("start_date");
                BoolQueryBuilder start_date_bqb2 = QueryBuilders.boolQuery().must(start_date_qb2);
                boolQueryBuilder.must(start_date_bqb2);
                break;

            case "time":
                /**
                 * @Description: 查询音频, 视频时长
                 * @Author: nieht
                 * @Date: 2019/3/6 16:31
                 */
                TermsBuilder group_find_type = AggregationBuilders.terms("group_find_type").field("find_type");
                SumBuilder sum_end_start = AggregationBuilders.sum("sum_end_start").field("end_start");

                SearchRequestBuilder aggRequestBuilder = esClient.prepareSearch("fk_onetoone_detail")
                        .setTypes("onetoone_detail");

                aggRequestBuilder.setQuery(boolQueryBuilder);
                aggRequestBuilder.addAggregation(group_find_type.subAggregation(sum_end_start));
                SearchResponse aggResponse = aggRequestBuilder.setSize(0).execute().actionGet();
                System.out.println(aggRequestBuilder);
                Terms terms = aggResponse.getAggregations().get("group_find_type");
                Iterator<Terms.Bucket> gradeBucketIt = terms.getBuckets().iterator();
                while (gradeBucketIt.hasNext()) {
                    Terms.Bucket gradeBucket = gradeBucketIt.next();
                    InternalSum internal_sumAmount = gradeBucket.getAggregations().get("sum_end_start");
                    String find_type = StringUtil.nullBlank(gradeBucket.getKey());
                    Integer user_num = (int) internal_sumAmount.getValue();
                    //视频时间，2语音时间
                    if ("1".equals(find_type)) {
                        res_map.put("viode_time", user_num);
                    } else if ("2".equals(find_type)) {
                        res_map.put("voice_time", user_num);
                    }
                }
                return res_map;
            case "group_connect_num":
                /**
                 * @Description: 一对一接通次数
                 * @Author: nieht
                 * @Date: 2019/3/8 15:09
                 */
                QueryBuilder start_date_qb3 = QueryBuilders.existsQuery("start_date");
                BoolQueryBuilder start_date_bqb3 = QueryBuilders.boolQuery().must(start_date_qb3);
                boolQueryBuilder.must(start_date_bqb3);

                TermsBuilder group_find_type2 = AggregationBuilders.terms("group_find_type").field("find_type");
                ValueCountBuilder count_find_type2 = AggregationBuilders.count("count_find_type").field("find_type");
                SearchRequestBuilder aggRequestBuilder2 = esClient.prepareSearch("fk_onetoone_detail")
                        .setTypes("onetoone_detail");

                aggRequestBuilder2.setQuery(boolQueryBuilder);
                aggRequestBuilder2.addAggregation(group_find_type2.subAggregation(count_find_type2));
                SearchResponse aggResponse2 = aggRequestBuilder2.setSize(0).execute().actionGet();
                Terms terms2 = aggResponse2.getAggregations().get("group_find_type");
                Iterator<Terms.Bucket> gradeBucketIt2 = terms2.getBuckets().iterator();
                while (gradeBucketIt2.hasNext()) {
                    Terms.Bucket gradeBucket = gradeBucketIt2.next();
                    InternalValueCount count_num_count = gradeBucket.getAggregations().get("count_find_type");
                    String find_type = StringUtil.nullBlank(gradeBucket.getKey());
                    Integer user_num = (int) count_num_count.getValue();
                    //视频时间，2语音时间
                    if ("1".equals(find_type)) {
                        res_map.put("viode_num", user_num);
                    } else if ("2".equals(find_type)) {
                        res_map.put("voice_num", user_num);
                    }
                }
                return res_map;
            case "group_repulse_num":
                /**
                 * @Description: 一对一拒接次数
                 * @Author: nieht
                 * @Date: 2019/3/8 15:09
                 */
                QueryBuilder status_qb = QueryBuilders.termQuery("status", "4");
                BoolQueryBuilder status_bqb = QueryBuilders.boolQuery().must(status_qb);
                boolQueryBuilder.must(status_bqb);

                TermsBuilder group_find_type3 = AggregationBuilders.terms("group_find_type").field("find_type");
                ValueCountBuilder count_find_type3 = AggregationBuilders.count("count_find_type").field("find_type");
                SearchRequestBuilder aggRequestBuilder3 = esClient.prepareSearch("fk_onetoone_detail")
                        .setTypes("onetoone_detail");

                aggRequestBuilder3.setQuery(boolQueryBuilder);
                aggRequestBuilder3.addAggregation(group_find_type3.subAggregation(count_find_type3));
                SearchResponse aggResponse3 = aggRequestBuilder3.setSize(0).execute().actionGet();
                Terms terms3 = aggResponse3.getAggregations().get("group_find_type");
                Iterator<Terms.Bucket> gradeBucketIt3 = terms3.getBuckets().iterator();
                while (gradeBucketIt3.hasNext()) {
                    Terms.Bucket gradeBucket = gradeBucketIt3.next();
                    InternalValueCount count_num_count = gradeBucket.getAggregations().get("count_find_type");
                    String find_type = StringUtil.nullBlank(gradeBucket.getKey());
                    Integer user_num = (int) count_num_count.getValue();
                    //视频时间，2语音时间
                    if ("1".equals(find_type)) {
                        res_map.put("viode_repulse_num", user_num);
                    } else if ("2".equals(find_type)) {
                        res_map.put("voice_repulse_num", user_num);
                    }
                }
                return res_map;
            case "group_all_num":
                /**
                 * @Description: 一对一发起次数
                 * @Author: nieht
                 * @Date: 2019/3/8 15:09
                 */
                TermsBuilder group_find_type4 = AggregationBuilders.terms("group_find_type").field("find_type");
                ValueCountBuilder count_find_type4 = AggregationBuilders.count("count_find_type").field("find_type");
                SearchRequestBuilder aggRequestBuilder4 = esClient.prepareSearch("fk_onetoone_detail")
                        .setTypes("onetoone_detail");

                aggRequestBuilder4.setQuery(boolQueryBuilder);
                aggRequestBuilder4.addAggregation(group_find_type4.subAggregation(count_find_type4));
                SearchResponse aggResponse4 = aggRequestBuilder4.setSize(0).execute().actionGet();
                Terms terms4 = aggResponse4.getAggregations().get("group_find_type");
                Iterator<Terms.Bucket> gradeBucketIt4 = terms4.getBuckets().iterator();
                while (gradeBucketIt4.hasNext()) {
                    Terms.Bucket gradeBucket = gradeBucketIt4.next();
                    InternalValueCount count_num_count = gradeBucket.getAggregations().get("count_find_type");
                    String find_type = StringUtil.nullBlank(gradeBucket.getKey());
                    Integer user_num = (int) count_num_count.getValue();
                    //视频时间，2语音时间
                    if ("1".equals(find_type)) {
                        res_map.put("viode_all_num", user_num);
                    } else if ("2".equals(find_type)) {
                        res_map.put("voice_all_num", user_num);
                    }
                }
                return res_map;
            default:
                break;
        }

        requestBuilder = esClient.prepareSearch("fk_onetoone_detail").setTypes("onetoone_detail");
        requestBuilder.setQuery(boolQueryBuilder);
        SearchResponse response = requestBuilder.execute().actionGet();

        res_map.put(type, response.getHits().getTotalHits());
        return res_map;
    }

}
