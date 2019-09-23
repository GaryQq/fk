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
import com.study.service.management.OpinionFeedbackService;
import com.study.util.bean.DataGridResultInfo;
import com.study.util.bean.PageBean;
import com.study.util.permissions.ResultUtil;
import com.study.util.tool.DateUtil;
import com.study.util.tool.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@Api(value = "OpinionFeedbackController", description = "反馈模块")
@RestController
@RequestMapping("/opinionFeedback")
public class OpinionFeedbackController {

	private static Logger logger = LoggerFactory.getLogger(OpinionFeedbackController.class);
	
	@Autowired
	private OpinionFeedbackService opinionFeedbackService;
	
	@ApiImplicitParams({
			@ApiImplicitParam(name = "nick_name", value = "反馈用户昵称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "start_time", value = "反馈时间", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "end_time", value = "反馈时间", required = false, dataType = "string", paramType = "query") })
	@RequestMapping(value = "/findFeedbackData", method = { RequestMethod.GET })
	public DataGridResultInfo findFeedbackData(HttpServletRequest request,
			@RequestParam(value = "nick_name", required = false) String nick_name,
			@RequestParam(value = "start_time", required = false) String start_time,
			@RequestParam(value = "end_time", required = false) String end_time, @ModelAttribute PageBean bean) {
		// 如果开始时间与结束时间为空查询推送时间在前后7天之内的
		if (StringUtil.eqBlank(start_time)) {
			start_time = DateUtil.getPastDate(7);
		}
		if (StringUtil.eqBlank(end_time)) {
			end_time = DateUtil.getFetureDate(7);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("start_time", start_time);
		param.put("end_time", end_time);
		param.put("nick_name", nick_name);
		List<Map<String, Object>> bannerViewList = opinionFeedbackService.findFeedbackData(param, bean);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(bannerViewList, 1);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}
}
