package com.study.controller.management;

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

import com.github.pagehelper.PageInfo;
import com.study.service.management.AgencyService;
import com.study.util.bean.DataGridResultInfo;
import com.study.util.bean.PageBean;
import com.study.util.permissions.ResultUtil;
import com.study.util.tool.DateUtil;
import com.study.util.tool.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "AgencyController", description = "代理管理API")
@RestController
@RequestMapping("/agency")
public class AgencyController {
	private static Logger logger = LoggerFactory.getLogger(AgencyController.class);

	@Autowired
	private AgencyService agencyService;

	@ApiOperation(value = "代理收入", notes = "代理收入API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "nick_name", value = "昵称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "start_time", value = "开始时间", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "end_time", value = "结束时间", required = false, dataType = "string", paramType = "query") })
	@RequestMapping(value = "/getData", method = { RequestMethod.GET })
	public DataGridResultInfo getData(HttpServletRequest request,
			@RequestParam(value = "start_time", required = false) String start_time,
			@RequestParam(value = "end_time", required = false) String end_time,
			@RequestParam(value = "nick_name", required = false) String nick_name, @ModelAttribute PageBean bean) {
		// 如果开始时间与结束时间为空查询推送时间在前后7天之内的
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
		List<Map<String, Object>> findViewList = agencyService.findAgencyIncomeList(param, bean);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(findViewList, 1);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

}
