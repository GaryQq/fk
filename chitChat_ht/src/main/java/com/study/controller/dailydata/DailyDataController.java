package com.study.controller.dailydata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.service.dailydata.DailyDataService;
import com.study.util.bean.DataGridResultInfo;
import com.study.util.bean.PageBean;
import com.study.util.permissions.ResultUtil;
import com.study.util.tool.DateUtil;
import com.study.util.tool.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "DailyData", description = "每日数据一对多汇总API")
@RestController
@RequestMapping("/dailyData")
public class DailyDataController {
	private static Logger logger = LoggerFactory.getLogger(DailyDataController.class);

	@Autowired
	private DailyDataService dailyDataService;

	@ApiOperation(value = "一对一每日数据明细", notes = "每日数据API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "status", value = "1全部用户2去除白名单用户", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "start_time", value = "开始时间", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "end_time", value = "结束时间", required = false, dataType = "string", paramType = "query") })
	@RequestMapping(value = "/oneToOneDay", method = { RequestMethod.GET })
	public DataGridResultInfo oneToOneDay(HttpServletRequest request,
			@RequestParam(value = "start_time", required = false) String start_time,
			@RequestParam(value = "end_time", required = false) String end_time,
			@RequestParam(value = "status", required = false) String status, @ModelAttribute PageBean bean) {
		// 如果开始时间与结束时间为空查询推送时间在前后7天之内的
		if (StringUtil.eqBlank(start_time)) {
			start_time = DateUtil.getPastDate(7);
		}
		if (StringUtil.eqBlank(end_time)) {
			end_time = DateUtil.getFetureDate(1);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("status", status);
		param.put("start_time", start_time);
		param.put("end_time", end_time);
		// 查询结果集总数
		long total = dailyDataService.findOneToOneDayTotal(param);
		// 查询结果集
		List<Map<String, Object>> res_list = dailyDataService.findOneToOneDay(param, bean);
		// 合计结果集
		Map<String, Object> footerMap = dailyDataService.findOneToOneDayFooter(param);
		List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
		footerList.add(footerMap);
		return ResultUtil.createDataGridFooterResult(total, res_list, footerList);
	}

}
