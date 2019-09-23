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

import com.study.service.management.PushJMSService;
import com.study.util.bean.MenuBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "PushJMSViewController", description = "语伴推送管理API")
@Controller
public class PushJMSViewController {
	private static Logger logger = LoggerFactory.getLogger(PushJMSViewController.class);

	@Autowired
	private PushJMSService pushJMSService;

	/** 声明全局变量返回容器 */
	private Map<String, Object> res_map;

	@ApiOperation(value = "跳转添加、修改", notes = "跳转添加、修改")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "bannerid", required = false, dataType = "int", paramType = "query") })
	@RequestMapping(value = "/pushJMSView/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request, @RequestParam(value = "id", required = false) Integer id,
			@ModelAttribute MenuBean bean) {
		request.setAttribute("menu", bean);
		if (id != null) {
			res_map = pushJMSService.findPushJMSEntity(id);
			request.setAttribute("entity", res_map);
			return "push/pushJMS/pushJMS_edit";
		} else {
			return "push/pushJMS/pushJMS_add";
		}
	}

	@ApiOperation(value = "跳转查看推送信息", notes = "跳转查看推送信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "jms_id", required = false, dataType = "int", paramType = "query") })
	@RequestMapping(value = "/pushJMSView/search", method = RequestMethod.GET)
	public String search(HttpServletRequest request, @RequestParam(value = "id", required = false) Integer id,
			@ModelAttribute MenuBean bean) {
		request.setAttribute("menu", bean);
		res_map = pushJMSService.findPushJMSEntity(id);
		request.setAttribute("entity", res_map);
		return "push/pushJMS/pushJMS_search";

	}
}
