package com.study.controller.detail;

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

import com.study.service.detail.OneToOneDetailService;
import com.study.util.bean.DataGridResultInfo;
import com.study.util.bean.PageBean;
import com.study.util.permissions.ResultUtil;
import com.study.util.tool.DateUtil;
import com.study.util.tool.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "OneToOneDetail", description = "一对一明细API")
@RestController
@RequestMapping("/oneToOneDetail")
public class OneToOneDetailController {

	private static Logger logger = LoggerFactory.getLogger(OneToOneDetailController.class);

	@Autowired
	private OneToOneDetailService oneToOneDetailService;

	@ApiOperation(value = "一对一音视频数据明细", notes = "明细数据API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "con_status", value = "是否接通1全部2接通3未接4拒接", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "user_nick_name", value = "发起者昵称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "anchor_nick_name", value = "接听者昵称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "anchor_status", value = "发起者类型1全部2去除白名单用户3白名单用户", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "user_status", value = "接听者类型1全部2去除白名单用户3白名单用户", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "start_time", value = "开始时间", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "end_time", value = "结束时间", required = false, dataType = "string", paramType = "query") })
	@RequestMapping(value = "/getOneToOneDetail", method = { RequestMethod.GET })
	public DataGridResultInfo getOneToOneDetail(HttpServletRequest request,
			@RequestParam(value = "start_time", required = false) String start_time,
			@RequestParam(value = "end_time", required = false) String end_time,
			@RequestParam(value = "con_status", required = false) String con_status,
			@RequestParam(value = "user_nick_name", required = false) String user_nick_name,
			@RequestParam(value = "anchor_nick_name", required = false) String anchor_nick_name,
			@RequestParam(value = "anchor_status", required = false) String anchor_status,
			@RequestParam(value = "user_status", required = false) String user_status, @ModelAttribute PageBean bean) {
		// 如果开始时间与结束时间为空查询推送时间在前后7天之内的
		if (StringUtil.eqBlank(start_time)) {
			start_time = DateUtil.getPastDate(7);
		}
		if (StringUtil.eqBlank(end_time)) {
			end_time = DateUtil.getFetureDate(1);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("start_time", start_time + " 00:00:00");
		param.put("end_time", end_time + " 23:59:59");
		param.put("con_status", con_status);
		param.put("user_nick_name", user_nick_name);
		param.put("anchor_nick_name", anchor_nick_name);
		param.put("anchor_status", anchor_status);
		param.put("user_status", user_status);

		// 查询结果集总数
		long total = oneToOneDetailService.findOneToOneDetailTotal(param);
		// 查询结果集
		List<Map<String, Object>> res_list = oneToOneDetailService.findOneToOneDetailList(param, bean);
		// 合计结果集
		Map<String, Object> footerMap = oneToOneDetailService.findOneToOneDetailFooter(param, bean);
		List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
		footerList.add(footerMap);
		return ResultUtil.createDataGridFooterResult(total, res_list, footerList);
	}

	@ApiOperation(value = "一对一私信数据明细", notes = "明细数据API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "user_nick_name", value = "发起者昵称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "anchor_nick_name", value = "接听者昵称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "anchor_status", value = "发起者类型1全部2去除白名单用户3白名单用户", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "user_status", value = "接听者类型1全部2去除白名单用户3白名单用户", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "start_time", value = "开始时间", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "end_time", value = "结束时间", required = false, dataType = "string", paramType = "query") })
	@RequestMapping(value = "/getOneToOneMsgDetail", method = { RequestMethod.GET })
	public DataGridResultInfo getOneToOneMsgDetail(HttpServletRequest request,
			@RequestParam(value = "start_time", required = false) String start_time,
			@RequestParam(value = "end_time", required = false) String end_time,
			@RequestParam(value = "con_status", required = false) String con_status,
			@RequestParam(value = "user_nick_name", required = false) String user_nick_name,
			@RequestParam(value = "anchor_nick_name", required = false) String anchor_nick_name,
			@RequestParam(value = "anchor_status", required = false) String anchor_status,
			@RequestParam(value = "user_status", required = false) String user_status, @ModelAttribute PageBean bean) {
		// 如果开始时间与结束时间为空查询推送时间在前后7天之内的
		if (StringUtil.eqBlank(start_time)) {
			start_time = DateUtil.getPastDate(7);
		}
		if (StringUtil.eqBlank(end_time)) {
			end_time = DateUtil.getFetureDate(1);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("start_time", start_time + " 00:00:00");
		param.put("end_time", end_time + " 23:59:59");
		param.put("con_status", con_status);
		param.put("user_nick_name", user_nick_name);
		param.put("anchor_nick_name", anchor_nick_name);
		param.put("anchor_status", anchor_status);
		param.put("user_status", user_status);

		// 查询结果集总数
		long total = oneToOneDetailService.findOneToOneMsgDetailTotal(param);
		// 查询结果集
		List<Map<String, Object>> res_list = oneToOneDetailService.findOneToOneMsgDetailList(param, bean);
		logger.info("findOneToOneMsgDetailList======>res_list:" + res_list);
		// 合计结果集
		// Map<String, Object> footerMap =
		// oneToOneDetailService.findOneToOneMsgDetailFooter(param, bean);
		// List<Map<String, Object>> footerList = new ArrayList<Map<String,
		// Object>>();
		// footerList.add(footerMap);
		return ResultUtil.createDataGridResult(total, res_list);
		// return ResultUtil.createDataGridFooterResult(total, res_list,
		// footerList);
	}

}
