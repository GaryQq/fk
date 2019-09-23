package com.study.controller.permissions.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.model.permissions.Dept;
import com.study.model.permissions.Role;
import com.study.model.permissions.User;
import com.study.service.lotterytie.LotterytieService;
import com.study.service.permissions.DeptService;
import com.study.service.permissions.RoleService;
import com.study.service.permissions.UserService;
import com.study.util.bean.MenuBean;
import com.study.util.tool.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "UserViewController", description = "用户展示页面")
@Controller
public class UserViewController {
	private static Logger logger = LoggerFactory.getLogger(UserViewController.class);
	@Autowired
	private DeptService deptService;
	@Autowired
	private UserService userService;
	@Resource
	private RoleService roleService;
	@Autowired
	private LotterytieService lotterytieService;

	@ApiOperation(value = "用户修改、添加页面", notes = "用户修改、添加页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "用户id", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "deptId", value = "部门id", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "menuName", value = "当前tab名称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "parentMenuName", value = "父页面tab名称", required = false, dataType = "string", paramType = "query") })
	@RequestMapping(value = "users/add", method = { RequestMethod.GET })
	public String add(HttpServletRequest request, @RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "deptId", required = true) Integer deptId, @ModelAttribute MenuBean bean) {
		List<Dept> selectAllDept = deptService.selectAllDept(null, null);
		request.setAttribute("depts", selectAllDept);
		request.setAttribute("deptId", deptId);
		request.setAttribute("menu", bean);
		if (id != null) {
			User user = userService.selectByKey(id);
			String fkUserName = user.getFkUserName();
			if (!"".equals(StringUtil.nullBlank(fkUserName))) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("user_name", fkUserName);
				Map<String, Object> user_map = lotterytieService.findUser(param);
				user.setFkUserName(StringUtil.nullBlank(user_map.get("nick_name")));
			}
			Role role = roleService.queryRoleListWithSelected(id);
			request.setAttribute("user", user);
			request.setAttribute("roles", role);
			return "permissions/users/users_edit";
		} else {
			return "permissions/users/users_add";
		}

	}

}
