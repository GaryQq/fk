package com.study.controller.permissions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.study.model.permissions.Resources;
import com.study.service.permissions.ResourcesService;
import com.study.shiro.ShiroService;
import com.study.util.bean.DataGridResultInfo;
import com.study.util.bean.PageBean;
import com.study.util.permissions.ResultUtil;
import com.study.util.tool.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "ResourcesController", description = "菜单操作API")
@RestController
@RequestMapping("/resources")
public class ResourcesController {
	private static Logger logger = LoggerFactory.getLogger(ResourcesController.class);

	@Resource
	private ResourcesService resourcesService;
	@Resource
	private ShiroService shiroService;

	@ApiOperation(value = "获取权限", notes = "根据角色id,用户id获所拥有的权限")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "rid", value = "角色id", required = true, dataType = "string", paramType = "query") })
	@RequestMapping(value = "/resourcesWithSelected", method = { RequestMethod.GET })
	public List<Resources> resourcesWithSelected(Integer rid) {
		return resourcesService.queryResourcesListWithSelected(rid);
	}

	@ApiOperation(value = "菜单权限", notes = "根据当前登录人的id来获取当前人所拥有的菜单接口")
	@RequestMapping(value = "/loadMenu", method = { RequestMethod.GET })
	public List<Resources> loadMenu() {
		Map<String, Object> map = new HashMap<>();
		Integer userid = (Integer) SecurityUtils.getSubject().getSession().getAttribute("userSessionId");
		map.put("type", 1);
		map.put("userid", userid);
		List<Resources> resourcesList = resourcesService.loadUserResources(map);
		return resourcesList;
	}

	@ApiOperation(value = "菜单添加", notes = "保存菜单")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(HttpServletRequest request) {
		try {
			String name = request.getParameter("name");
			String resurl = request.getParameter("resUrl");
			String type = request.getParameter("type");
			String parentid = request.getParameter("parentid");
			String sort = request.getParameter("sort");
			String iconCls = request.getParameter("iconCls");
			String butFun = request.getParameter("butFun");

			Resources resources = new Resources();
			resources.setName(name);
			resources.setResurl(resurl);
			resources.setType(StringUtil.nullZero(type));
			resources.setParentid(StringUtil.nullZero(parentid));
			resources.setSort(StringUtil.nullZero(sort));
			resources.setIconCls(iconCls);
			resources.setButFun(butFun);

			logger.info("insertResources=======>resources:" + resources.toString());
			resourcesService.insertResources(resources);
			// 更新权限
			shiroService.updatePermission();
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "菜单修改", notes = "保存修改菜单")
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(HttpServletRequest request) {
		try {
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			String resurl = request.getParameter("resUrl");
			String type = request.getParameter("type");
			String parentid = request.getParameter("parentid");
			String sort = request.getParameter("sort");
			String iconCls = request.getParameter("iconCls");
			String butFun = request.getParameter("butFun");

			Resources resources = new Resources();
			resources.setId(StringUtil.nullZero(id));
			resources.setName(name);
			resources.setResurl(resurl);
			resources.setType(StringUtil.nullZero(type));
			resources.setParentid(StringUtil.nullZero(parentid));
			resources.setSort(StringUtil.nullZero(sort));
			resources.setIconCls(iconCls);
			resources.setButFun(butFun);
			
			resourcesService.updateResources(resources);
			// 更新权限
			shiroService.updatePermission();
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "菜单删除", notes = "删除菜单API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ids", value = "菜单id", required = true, dataType = "string", paramType = "query") })
	@RequestMapping(value = "/delete", method = { RequestMethod.DELETE })
	public String delete(@RequestParam(value = "ids") String ids) {
		try {
			if (ids != null) {
				String[] idsArry = ids.split(",");
				for (String id : idsArry) {
					resourcesService.delete(Integer.parseInt(id));
				}
			}
			// 更新权限
			shiroService.updatePermission();
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "菜单展示", notes = "获取所有的菜单API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "name", value = "菜单名称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "菜单类别，1菜单，2按钮", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "page", value = "当前页码", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "rows", value = "一页显示数", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "parentid", value = "父节点id", required = false, dataType = "int", paramType = "query") })
	@RequestMapping(value = "/getMenu", method = { RequestMethod.GET })
	public DataGridResultInfo getMenu(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute PageBean page, @RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "type", required = true) Integer type,
			@RequestParam(value = "parentid", required = false) Integer parentid) {
		Resources example = new Resources();
		example.setType(type);
		example.setName(name);
		example.setParentid(parentid);
		List<Resources> queryByType = resourcesService.queryByType(example, page);
		PageInfo<Resources> pageInfo = new PageInfo<Resources>(queryByType);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

	@ApiOperation(value = "获取多有菜单", notes = "获取所有菜单，返回list")
	@RequestMapping(value = "/getDataTree", method = { RequestMethod.GET })
	public List<Resources> getData(HttpServletRequest request, HttpServletResponse response) {
		Resources example = new Resources();
		example.setType(1);
		List<Resources> queryByType = resourcesService.queryByType(example, null);
		return queryByType;
	}
}
