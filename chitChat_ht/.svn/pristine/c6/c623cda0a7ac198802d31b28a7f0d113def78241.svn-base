package com.study.controller.tool.view;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.study.service.tool.ToolService;
import com.study.util.bean.MenuBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "SourceToolViewController", description = "公共功能管理API")
@Controller
public class SourceToolViewController {

	private static Logger logger = LoggerFactory.getLogger(SourceToolViewController.class);

	@Autowired
	private ToolService toolService;

	@ApiOperation(value = "跳转渠道添加页面", notes = "跳转渠道添加页面")
	@RequestMapping(value = "/sourceToolView/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request, @ModelAttribute MenuBean bean) {
		request.setAttribute("menu", bean);
		return "tool/source/source_add";
	}
}
