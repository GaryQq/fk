package com.study.controller.management.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.service.management.AuditManagementService;
import com.study.util.bean.MenuBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "AuditManagementViewController", description = "代理管理API")
@Controller
public class AuditManagementViewController {
	
	private static Logger logger = LoggerFactory.getLogger(AuditManagementViewController.class);

	@Autowired
	private AuditManagementService auditManagementService;

	/** 声明全局变量返回容器 */
	private Map<String, Object> res_map;

	@ApiOperation(value = "申请代理信息查询", notes = "申请代理信息查询")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "user_name", value = "代理user_name", required = false, dataType = "String", paramType = "query") })
	@RequestMapping(value = "/auditManagementView/search", method = RequestMethod.GET)
	public String search(HttpServletRequest request,
			@RequestParam(value = "user_name", required = false) String user_name, @ModelAttribute MenuBean bean) {
		request.setAttribute("menu", bean);
		res_map = auditManagementService.findAuditManagementEntity(user_name);
		request.setAttribute("entity", res_map);
		return "audit/auditManagement/auditManagement_search";
	}

	@ApiOperation(value = "代理信息查询", notes = "代理信息查询")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "user_name", value = "代理user_name", required = false, dataType = "String", paramType = "query") })
	@RequestMapping(value = "/auditView/searchAudit", method = RequestMethod.GET)
	public String searchAudit(HttpServletRequest request,
			@RequestParam(value = "user_name", required = false) String user_name, @ModelAttribute MenuBean bean) {
		request.setAttribute("menu", bean);
		res_map = auditManagementService.findAuditEntity(user_name);
		request.setAttribute("entity", res_map);
		return "audit/audit/audit_search";
	}

	@ApiOperation(value = "跳转添加代理", notes = "跳转添加代理")
	@RequestMapping(value = "/auditView/addAudit", method = RequestMethod.GET)
	public String addAudit(HttpServletRequest request, @ModelAttribute MenuBean bean) {
		request.setAttribute("menu", bean);
		return "audit/audit/audit_add";
	}

	@ApiOperation(value = "跳转修改代理信息", notes = "跳转修改代理信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "audit_id", value = "代理ID", required = false, dataType = "int", paramType = "query") })
	@RequestMapping(value = "/auditView/editAudit", method = RequestMethod.GET)
	public String editAudit(HttpServletRequest request,
			@RequestParam(value = "audit_id", required = false) Integer audit_id, @ModelAttribute MenuBean bean) {
		request.setAttribute("menu", bean);
		res_map = auditManagementService.findAuditByIdEntity(audit_id);
		request.setAttribute("entity", res_map);
		return "audit/audit/audit_edit";
	}
}
