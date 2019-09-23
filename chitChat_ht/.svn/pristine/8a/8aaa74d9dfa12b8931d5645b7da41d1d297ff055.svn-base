package com.study.controller.permissions.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.study.model.permissions.Resources;
import com.study.model.permissions.User;
import com.study.service.permissions.ResourcesService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "HomeController", description = "登录、首页")
@Controller
public class HomeController {
	@Resource
	private ResourcesService resourcesService;

	@ApiOperation(value = "跳转登录页面", notes = "跳转登录页面")
	@RequestMapping(value = "/login", method = { RequestMethod.GET })
	public String login_view(HttpServletRequest request) {
		request.setAttribute("aa", "111");
		return "login";
	}

	@ApiOperation(value = "登录验证", notes = "登录验证")
	@RequestMapping(value = "/login", method = { RequestMethod.POST })
	public String login(HttpServletRequest request, User user) {
		if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
			request.setAttribute("msg", "用户名或密码不能为空！");
			return "login";
		}
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
		try {
			subject.login(token);
			return "redirect:index";
		} catch (LockedAccountException lae) {
			token.clear();
			request.setAttribute("msg", "用户已经被锁定不能登录，请与管理员联系！");
			return "login";
		} catch (AuthenticationException e) {
			token.clear();
			request.setAttribute("msg", "用户或密码不正确！");
			return "login";
		}
	}

	/**
	 * Description: 注销
	 *
	 * @param
	 * @author Easong
	 * @version 2018/1/26
	 * @since JDK1.7
	 */
	@ApiOperation(value = "注销", notes = "注销登录")
	@RequestMapping(value = "/loginOut", method = { RequestMethod.GET })
	public String logOut() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "login";
	}

	/**
	 * 首页
	 * 
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "首页", notes = "首页页面")
	@RequestMapping(value = "/index", method = { RequestMethod.GET })
	public String index(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		Integer userid = (Integer) SecurityUtils.getSubject().getSession().getAttribute("userSessionId");
		map.put("type", 1);
		map.put("userid", userid);
		List<Resources> resourcesList = resourcesService.loadUserResources(map);
		request.setAttribute("resources", resourcesList);
		Session session = SecurityUtils.getSubject().getSession();
		User user = (User) session.getAttribute("userSession");
		request.setAttribute("userName", user.getUsername());
		return "index";
	}
}
