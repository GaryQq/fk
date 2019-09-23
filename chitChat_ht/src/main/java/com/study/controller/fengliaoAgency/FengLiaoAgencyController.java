package com.study.controller.fengliaoAgency;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.study.model.permissions.User;
import com.study.service.fengliaoAgency.FengLiaoAgencyService;
import com.study.util.bean.DataGridResultInfo;
import com.study.util.bean.PageBean;
import com.study.util.permissions.ResultUtil;
import com.study.util.tool.DateUtil;
import com.study.util.tool.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "FengLiaoAgencyController", description = "疯聊代理API")
@RestController
@RequestMapping("/flAgency")
public class FengLiaoAgencyController {
    private static Logger logger = LoggerFactory.getLogger(FengLiaoAgencyController.class);

    @Autowired
    private FengLiaoAgencyService fengLiaoAgencyService;

    @ApiOperation(value = "用户注册管理", notes = "用户注册管理API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nick_name", value = "昵称", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "start_time", value = "注册时间", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "注册时间", required = false, dataType = "string", paramType = "query") })
    @RequestMapping(value = "/findUserRegisterList", method = { RequestMethod.GET })
    public DataGridResultInfo findUserRegisterList(HttpServletRequest request,
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
        List<Map<String, Object>> findViewList = fengLiaoAgencyService.findUserRegisterList(param, bean);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(findViewList, 1);
        return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @ApiOperation(value = "查询用户充值金额", notes = "用户充值管理API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nick_name", value = "昵称", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "start_time", value = "充值时间", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "充值时间", required = false, dataType = "string", paramType = "query") })
    @RequestMapping(value = "/findUserPayDetailList", method = { RequestMethod.GET })
    public DataGridResultInfo findUserPayDetailList(HttpServletRequest request,
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
        List<Map<String, Object>> findViewList = fengLiaoAgencyService.findUserPayDetailList(param, bean);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(findViewList, 1);

        // 查询合计列
        int goal_amount = fengLiaoAgencyService.findUserPaySumInfo(param);
        List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
        Map<String, Object> footerMap = new HashMap<>();
        footerMap.put("NICK_NAME", "合计");
        footerMap.put("GOAL_AMOUNT", goal_amount);
        footerList.add(footerMap);
        return ResultUtil.createDataGridFooterResult(pageInfo.getTotal(), pageInfo.getList(), footerList);
    }

    @ApiOperation(value = "查询代理提现记录", notes = "查询代理提现记录管理API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nick_name", value = "昵称", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "start_time", value = "提现时间", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "提现时间", required = false, dataType = "string", paramType = "query") })
    @RequestMapping(value = "/findAgencyPayDetailList", method = { RequestMethod.GET })
    public DataGridResultInfo findAgencyPayDetailList(HttpServletRequest request,
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
        param.put("start_time", start_time);
        param.put("end_time", end_time);
        // 查询当前登录用户关联的fk账号
        Session session = SecurityUtils.getSubject().getSession();
        User user = (User) session.getAttribute("userSession");
        String fkUserName = StringUtil.nullBlank(user.getFkUserName());
        param.put("fkUserName", fkUserName);
        List<Map<String, Object>> findViewList = fengLiaoAgencyService.findAgencyPayDetailList(param, bean);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(findViewList, 1);

        // 查询代理累计收入
        request.setAttribute("agency_amount", "11111");

        // 查询合计列
        int goal_amount = fengLiaoAgencyService.findAgencyPaySumInfo(param);
        List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
        Map<String, Object> footerMap = new HashMap<>();
        footerMap.put("CREATE_DATE", "合计");
        footerMap.put("AMOUNT", goal_amount);
        footerList.add(footerMap);
        return ResultUtil.createDataGridFooterResult(pageInfo.getTotal(), pageInfo.getList(), footerList);
    }

    @ApiOperation(value = "查询代理账户信息", notes = "代理账户信息管理API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start_time", value = "提现时间", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "提现时间", required = false, dataType = "string", paramType = "query") })
    @RequestMapping(value = "/findAgencyPayAccount", method = { RequestMethod.POST })
    public String findAgencyPayAccount(HttpServletRequest request,
                                       @RequestParam(value = "start_time", required = false) String start_time,
                                       @RequestParam(value = "end_time", required = false) String end_time) {

        // 如果开始时间与结束时间为空查询时间在前7天之内,到当前时间
        if (StringUtil.eqBlank(start_time)) {
            start_time = DateUtil.getPastDate(7);
        }
        if (StringUtil.eqBlank(end_time)) {
            end_time = DateUtil.getFetureDate(1);
        }

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("start_time", start_time);
        param.put("end_time", end_time);
        // 查询当前登录用户关联的fk账号
        Session session = SecurityUtils.getSubject().getSession();
        User user = (User) session.getAttribute("userSession");
        String fkUserName = StringUtil.nullBlank(user.getFkUserName());
        param.put("fkUserName", fkUserName);
        Double income = fengLiaoAgencyService.findAgencyPayIncome(param);// 累计收入
        Double balance = fengLiaoAgencyService.findAgencyPayBalance(param);// 代理余额
        String ratio = fengLiaoAgencyService.findAgencyPayRatio(param);// 代理提成比例
        JSONObject json = new JSONObject();
        json.put("income", income);
        json.put("balance", balance);
        json.put("ratio", ratio);
        return json.toString();
    }

    @ApiOperation(value = "代理提现信息", notes = "代理账户信息管理API")
    @RequestMapping(value = "/saveAgencyCashPay", method = { RequestMethod.POST })
    public String saveAgencyCashPay(HttpServletRequest request) {
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            // 查询当前登录用户关联的fk账号
            Session session = SecurityUtils.getSubject().getSession();
            User user = (User) session.getAttribute("userSession");
            String fkUserName = StringUtil.nullBlank(user.getFkUserName());
            param.put("fkUserName", fkUserName);
            Double balance = fengLiaoAgencyService.findAgencyPayBalance(param);// 代理余额
            param.put("balance", balance);
            fengLiaoAgencyService.updateAgencyAccount(param);// 修改代理提现账户
            fengLiaoAgencyService.saveAgencyPayDetail(param);// 添加代理提现明细
            fengLiaoAgencyService.saveAgencyAccountDetail(param);// 添加金币提现明细
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

}
