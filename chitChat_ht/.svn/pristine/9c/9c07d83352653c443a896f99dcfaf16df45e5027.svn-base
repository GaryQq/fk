package com.study.controller.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.study.model.permissions.Resources;
import com.study.service.permissions.ResourcesService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Api(value = "BaseController", description = "所有菜单首页页面")
@Controller
public class BaseController {
	private static Logger logger = LoggerFactory.getLogger(BaseController.class);

	@Resource
	private ResourcesService resourcesService;

	@ApiOperation(value = "page_index页面", notes = "所有菜单的首页")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "path", value = "url地址", required = false, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "menuName", value = "菜单名称", required = false, dataType = "string", paramType = "path") })
	@RequestMapping(value = "/page/{fileName}/{path}/{menuName}/{id}", method = { RequestMethod.GET })
	public String page(@PathVariable String fileName, @PathVariable String path, @PathVariable String menuName,
			@PathVariable Integer id, HttpServletRequest request) {
		Example example = new Example(Resources.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("type", 2);
		createCriteria.andEqualTo("parentid", id);
		Integer userid = (Integer) SecurityUtils.getSubject().getSession().getAttribute("userSessionId");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userid", userid);
		param.put("type", 2);
		param.put("parentid", id);
		List<Resources> buttons = resourcesService.findByUserButtonList(param);
		request.setAttribute("buttons", buttons);
		request.setAttribute("menuName", menuName);
		logger.info("BaseController=======>fileName:" + fileName);
		return fileName + "/" + path + "/" + path;
	}
}
