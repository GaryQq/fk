package com.study.controller.management.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.study.util.bean.MenuBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "WhiteUserViewController", description = "白名单管理API")
@Controller
public class WhiteUserViewController {
	private static Logger logger = LoggerFactory.getLogger(WhiteUserViewController.class);

	/** 声明全局变量返回容器 */
	private Map<String, Object> res_map;

	@ApiOperation(value = "跳转添加、修改", notes = "跳转添加、修改")
	@RequestMapping(value = "/whiteUserView/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request, @ModelAttribute MenuBean bean) {
		request.setAttribute("menu", bean);
		return "operation/whiteUser/whiteUser_add";
	}

}
