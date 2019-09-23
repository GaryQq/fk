package com.study.controller.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.study.model.permissions.User;
import com.study.service.lotterytie.LotterytieService;
import com.study.service.management.AuditManagementService;
import com.study.util.bean.DataGridResultInfo;
import com.study.util.bean.PageBean;
import com.study.util.permissions.ResultUtil;
import com.study.util.tool.DateUtil;
import com.study.util.tool.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "AuditManagement", description = "审核管理API")
@RestController
@RequestMapping("/auditManagement")
public class AuditManagementController {
	private static Logger logger = LoggerFactory.getLogger(AuditManagementController.class);

	@Autowired
	private AuditManagementService auditManagementService;

	@Autowired
	private LotterytieService lotterytieService;

	/** 声明全局变量返回容器 */
	private Map<String, Object> res_map = new HashMap<String, Object>();

	@ApiOperation(value = "审核管理", notes = "审核管理API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "nick_name", value = "昵称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "status", value = "审核状态", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "start_time", value = "申请时间", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "end_time", value = "申请时间", required = false, dataType = "string", paramType = "query") })
	@RequestMapping(value = "/getData", method = { RequestMethod.GET })
	public DataGridResultInfo getData(HttpServletRequest request,
			@RequestParam(value = "nick_name", required = false) String nick_name,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "start_time", required = false) String start_time,
			@RequestParam(value = "end_time", required = false) String end_time, @ModelAttribute PageBean bean) {

		// 如果开始时间与结束时间为空查询推送时间在前后7天之内的
		if (StringUtil.eqBlank(start_time)) {
			start_time = DateUtil.getPastDate(7);
		}
		if (StringUtil.eqBlank(end_time)) {
			end_time = DateUtil.getFetureDate(7);
		}
		/**
		 * 这里只查询审核未通过的代理,因为代理被拒绝之后可以重复添加,避免出现一个人多条数据,这里只显示未通过记录
		 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("nick_name", nick_name);
		param.put("start_time", start_time);
		param.put("end_time", end_time);
		param.put("status", status);
		List<Map<String, Object>> auditManagementViewList = auditManagementService.findAuditManagementView(param, bean);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(auditManagementViewList, 1);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

	@ApiOperation(value = "代理管理", notes = "代理管理API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "nick_name", value = "昵称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "del_status", value = "代理状态", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "language", value = "母语", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "level", value = "级别", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "sort_status", value = "排序规则", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "user_status", value = "用户类型", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "start_time", value = "申请时间", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "end_time", value = "申请时间", required = false, dataType = "string", paramType = "query") })
	@RequestMapping(value = "/getAuditData", method = { RequestMethod.GET })
	public DataGridResultInfo getAuditData(HttpServletRequest request,
			@RequestParam(value = "nick_name", required = false) String nick_name,
			@RequestParam(value = "del_status", required = false) String del_status,
			@RequestParam(value = "language", required = false) String language,
			@RequestParam(value = "level", required = false) String level,
			@RequestParam(value = "sort_status", required = false) String sort_status,
			@RequestParam(value = "user_status", required = false) String user_status,
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
		param.put("nick_name", nick_name);
		param.put("start_time", start_time);
		param.put("end_time", end_time);
		param.put("del_status", del_status);
		param.put("language", language);
		param.put("level", level);
		param.put("sort_status", sort_status);
		param.put("user_status", user_status);
		logger.info("findAuditView=========>param:" + param);
		List<Map<String, Object>> findAuditViewList = auditManagementService.findAuditView(param, bean);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(findAuditViewList, 1);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

	@ApiOperation(value = "代理是否过审", notes = "代理是否过审API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ids", value = "审核用户ID", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "content", value = "拒绝原因", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "del_status", value = "显示或隐藏", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "status", value = "代理审核状态", required = true, dataType = "string", paramType = "query") })
	@RequestMapping(value = "enable", method = { RequestMethod.GET })
	public String enable(HttpServletRequest request, @RequestParam(value = "ids", required = true) String ids,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "del_status", required = false) Integer del_status,
			@RequestParam(value = "content", required = false) String content) {
		try {
			if (ids != null) {
				String[] idsArry = ids.split(",");
				Map<String, Object> param = new HashMap<>();
				param.put("status", status);
				param.put("content", content);
				param.put("del_status", del_status);
				Session session = SecurityUtils.getSubject().getSession();
				User user = (User) session.getAttribute("userSession");
				param.put("operator", user.getName());
				for (String id : idsArry) {
					param.put("id", id);
					logger.info("enableUserAgentEntity=====>param:" + param);
					auditManagementService.enableUserAgentEntity(param);
				}
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "查询代理信息", notes = "查询代理信息API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "user_name", value = "代理账号", required = true, dataType = "string", paramType = "query") })
	@RequestMapping(value = "searchEntity", method = { RequestMethod.POST })
	public String searchEntity(HttpServletRequest request,
			@RequestParam(value = "user_name", required = true) String user_name) {
		try {
			Map<String, Object> audit_map = auditManagementService.findAuditManagementEntity(user_name);
			if (!audit_map.isEmpty()) {
				res_map.put("code", "success");
				res_map.put("data", audit_map);
			}
			JSONObject json = new JSONObject(res_map);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "添加代理信息", notes = "添加代理信息API")
	@RequestMapping(value = "/addAduit", method = { RequestMethod.POST })
	public String addAduit(HttpServletRequest request) {
		try {
			String nick_name = request.getParameter("nick_name");
			String region = request.getParameter("region");
			String master = request.getParameter("master");
			String user_nick_name = request.getParameter("user_nick_name");
			String area_code = request.getParameter("area_code");
			String mobile = request.getParameter("mobile");
			String eMail = request.getParameter("eMail");
			String info = request.getParameter("info");

			Map<String, Object> param = new HashMap<>();
			param.put("nick_name", nick_name);
			param.put("region", region);
			param.put("master", master);
			param.put("user_nick_name", user_nick_name);
			param.put("area_code", area_code);
			param.put("mobile", mobile);
			param.put("eMail", eMail);
			param.put("info", info);
			Map<String, Object> user_map = lotterytieService.findUser(param);
			if (!user_map.isEmpty()) {
				param.put("user_name", user_map.get("user_name"));
			}
			Session session = SecurityUtils.getSubject().getSession();
			User user = (User) session.getAttribute("userSession");
			param.put("operator", user.getName());
			logger.info("insertAuditEntity==========>param:" + param);
			auditManagementService.insertAuditEntity(param);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "修改代理信息", notes = "修改代理信息API")
	@RequestMapping(value = "/editAudit", method = { RequestMethod.POST })
	public String editAudit(HttpServletRequest request) {
		try {
			String audit_id = request.getParameter("audit_id");
			String region = request.getParameter("region");
			String master = request.getParameter("master");
			String agent_name = request.getParameter("agent_name");
			String area_code = request.getParameter("area_code");
			String mobile = request.getParameter("mobile");
			String eMail = request.getParameter("eMail");
			String info = request.getParameter("info");
			String user_name = request.getParameter("user_name");
			String user_level = request.getParameter("user_level");

			Map<String, Object> param = new HashMap<>();
			param.put("audit_id", audit_id);
			param.put("region", region);
			param.put("master", master);
			param.put("agent_name", agent_name);
			param.put("area_code", area_code);
			param.put("mobile", mobile);
			param.put("eMail", eMail);
			param.put("info", info);
			param.put("user_level", user_level);
			Session session = SecurityUtils.getSubject().getSession();
			User user = (User) session.getAttribute("userSession");
			param.put("operator", user.getName());
			logger.info("updateAuditEntity==========>param:" + param);
			auditManagementService.updateAuditEntity(param);

			/**
			 * 添加代理修改操作记录
			 */
			Map<String, Object> records_param = new HashMap<String, Object>();
			records_param.put("operator", user.getName());
			records_param.put("user_name", user_name);
			if (StringUtil.eqBlank(user_level)) {
				records_param.put("type", "/auditManagement/editAudit");
				records_param.put("context", "修改代理信息");
			} else {
				records_param.put("type", "/auditManagement/editAuditLevel");
				records_param.put("context", "修改代理评级信息");
			}
			lotterytieService.insertChangeRecordsEntity(records_param);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "代理查询邀请过来的旗下老师审核状态", notes = "审核查询")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "nick_name", value = "昵称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "status", value = "审核状态", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "start_time", value = "申请时间", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "end_time", value = "申请时间", required = false, dataType = "string", paramType = "query") })
	@RequestMapping(value = "/getTeacherData", method = { RequestMethod.GET })
	public DataGridResultInfo getTeacherData(HttpServletRequest request,
			@RequestParam(value = "nick_name", required = false) String nick_name,
			@RequestParam(value = "status", required = false) String status,
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
		param.put("nick_name", nick_name);
		param.put("start_time", start_time);
		param.put("end_time", end_time);
		param.put("status", status);
		Session session = SecurityUtils.getSubject().getSession();
		User user = (User) session.getAttribute("userSession");
		List<Map<String, Object>> auditTeacherViewList = new ArrayList<Map<String, Object>>();
		String fkUserName = StringUtil.nullBlank(user.getFkUserName());
		if (!"".equals(fkUserName)) {
			param.put("fkUserName", fkUserName);
			auditTeacherViewList = auditManagementService.findAuditTeacherView(param, bean);
		}
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(auditTeacherViewList, 1);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

}
