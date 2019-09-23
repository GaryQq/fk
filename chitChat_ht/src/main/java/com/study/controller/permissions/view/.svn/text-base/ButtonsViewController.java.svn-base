package com.study.controller.permissions.view;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.model.permissions.Resources;
import com.study.service.permissions.ResourcesService;
import com.study.util.bean.MenuBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "ButtonsViewController", description = "按钮页面")
@Controller
public class ButtonsViewController {
	@Resource
	private ResourcesService resourcesService;

	@ApiOperation(value = "跳转添加、修改", notes = "跳转添加、修改")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "按钮id", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "menuName", value = "当前tab名称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "parentMenuName", value = "父页面tab名称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "parentid", value = "菜单id", required = true, dataType = "int", paramType = "query"), })
	@RequestMapping(value = "/buttons/add", method = { RequestMethod.GET })
	public String add(HttpServletRequest request, @RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "parentid", required = true) Integer parentid, @ModelAttribute MenuBean bean) {
		Resources example = new Resources();
		example.setType(1);
		List<Resources> queryByType = resourcesService.queryByType(example, null);
		request.setAttribute("resList", queryByType);
		request.setAttribute("menu", bean);
		if (id != null) {
			Resources res = resourcesService.selectByKey(id);
			request.setAttribute("res", res);
			return "permissions/buttons/buttons_edit";
		} else {
			request.setAttribute("parentid", parentid);
			return "permissions/buttons/buttons_add";
		}
	}

}
