package com.study.controller.permissions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.study.model.permissions.User;
import com.study.model.permissions.UserRole;
import com.study.service.lotterytie.LotterytieService;
import com.study.service.permissions.UserRoleService;
import com.study.service.permissions.UserService;
import com.study.util.bean.DataGridResultInfo;
import com.study.util.bean.PageBean;
import com.study.util.permissions.PasswordHelper;
import com.study.util.permissions.ResultUtil;
import com.study.util.tool.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "UserController", description = "用户操作api")
@RestController
@RequestMapping("/users")
public class UserController {

	@Resource
	private UserService userService;
	@Resource
	private UserRoleService userRoleService;
	@Autowired
	private LotterytieService lotterytieService;

	@ApiOperation(value = "查看部门下的人员", notes = "根据部门查看多有人员")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "deptId", value = "部门id", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "username", value = "登录名", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "name", value = "姓名", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "rows", value = "一页条数", required = true, dataType = "int", paramType = "query") })
	@RequestMapping(value = "/getData", method = { RequestMethod.GET })
	public DataGridResultInfo getData(HttpServletRequest request,
			@RequestParam(value = "deptId", required = true) Integer deptId,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "name", required = false) String name, @ModelAttribute PageBean bean) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deptId", deptId);
		map.put("name", name);
		map.put("username", username);
		List<User> userList = userService.selectUserByDeptId(map, bean);
		PageInfo<User> info = new PageInfo<User>(userList);
		return ResultUtil.createDataGridResult(info.getTotal(), info.getList());

	}

	/**
	 * 保存用户角色
	 * 
	 * @param userRole
	 *            用户角色 此处获取的参数的角色id是以 “,” 分隔的字符串
	 * @return
	 */
	@RequestMapping("/saveUserRoles")
	public String saveUserRoles(UserRole userRole) {
		if (StringUtils.isEmpty(userRole.getUserid())) {
			return "error";
		}
		try {
			userRoleService.addUserRole(userRole);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "添加保存", notes = "添加保存")
	@RequestMapping(value = "/add", method = { RequestMethod.POST })
	public String add(HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		String deptId = request.getParameter("deptId");
		String enable = request.getParameter("enable");
		String userCode = request.getParameter("userCode");
		String fkUserName = request.getParameter("fkUserName");
		String mark = request.getParameter("mark");

		User u = userService.selectByUsername(username);
		if (u != null) {
			return "error";
		}
		try {
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			user.setName(name);
			user.setPhone(phone);
			user.setDeptId(StringUtil.nullZero(deptId));
			user.setEnable(StringUtil.nullZero(enable));
			user.setUserCode(StringUtil.nullZero(userCode));
			user.setMark(mark);
			user.setAddress(address);
			if (!"".equals(fkUserName)) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("nick_name", fkUserName);
				Map<String, Object> user_map = lotterytieService.findUser(param);
				user.setFkUserName(StringUtil.nullBlank(user_map.get("user_name")));
			}
			/* user.setEnable(1); */
			PasswordHelper.encryptPassword(user);
			userService.insertUser(user);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "修改保存", notes = "修改保存")
	@RequestMapping(value = "/edit", method = { RequestMethod.POST })
	public String edit(HttpServletRequest request) {
		try {
			String id = request.getParameter("id");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String phone = request.getParameter("phone");
			String address = request.getParameter("address");
			String deptId = request.getParameter("deptId");
			String enable = request.getParameter("enable");
			String userCode = request.getParameter("userCode");
			String fkUserName = request.getParameter("fkUserName");
			String mark = request.getParameter("mark");
			String rolesId = request.getParameter("rolesId"); // 角色ID

			User user = new User();
			user.setId(StringUtil.nullZero(id));
			user.setUsername(username);
			user.setPassword(password);
			user.setName(name);
			user.setPhone(phone);
			user.setDeptId(StringUtil.nullZero(deptId));
			user.setEnable(StringUtil.nullZero(enable));
			user.setUserCode(StringUtil.nullZero(userCode));
			user.setMark(mark);
			user.setAddress(address);
			if (!"".equals(fkUserName)) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("nick_name", fkUserName);
				Map<String, Object> user_map = lotterytieService.findUser(param);
				user.setFkUserName(StringUtil.nullBlank(user_map.get("user_name")));
			}
			userService.updateNotNull(user);
			if (!"".equals(rolesId)) {
				UserRole userRole = new UserRole();
				userRole.setRoleid(rolesId);
				userRole.setUserid(user.getId());
				userRoleService.addUserRole(userRole);
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "删除用户", notes = "根据id删除用户,同时删除当前用户关联角色")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ids", value = "用户id", required = true, dataType = "string", paramType = "query") })
	@RequestMapping(value = "/delete", method = { RequestMethod.DELETE })
	public String delete(@RequestParam(value = "ids", required = true) String ids) {
		try {
			if (ids != null) {
				String[] id_s = ids.split(",");
				for (String id : id_s) {
					userService.delUser(Integer.parseInt(id));
				}
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "启用或停用用户", notes = "启用或停用用户")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "enable", value = "启用、停用", required = true, dataType = "int", paramType = "query"), })
	@RequestMapping(value = "enable", method = { RequestMethod.GET })
	public String enable(@RequestParam(value = "id", required = true) Integer id,
			@RequestParam(value = "enable", required = true) Integer enable) {
		try {
			User entity = new User();
			entity.setId(id);
			entity.setEnable(enable);
			userService.updateNotNull(entity);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}

	}
}
