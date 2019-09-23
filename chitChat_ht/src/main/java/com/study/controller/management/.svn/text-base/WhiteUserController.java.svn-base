package com.study.controller.management;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.study.model.permissions.User;
import com.study.service.management.WhiteUserService;
import com.study.util.bean.DataGridResultInfo;
import com.study.util.bean.PageBean;
import com.study.util.permissions.ResultUtil;
import com.study.util.tool.DateUtil;
import com.study.util.tool.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "WhiteUser", description = "白名单用户管理API")
@RestController
@RequestMapping("/whiteUser")
public class WhiteUserController {
	private static Logger logger = LoggerFactory.getLogger(WhiteUserController.class);

	@Autowired
	private WhiteUserService whiteUserService;

	@ApiOperation(value = "白名单用户管理", notes = "白名单用户管理API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "nick_name", value = "昵称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "start_time", value = "白名单添加时间", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "end_time", value = "白名单添加时间", required = false, dataType = "string", paramType = "query") })
	@RequestMapping(value = "/getData", method = { RequestMethod.GET })
	public DataGridResultInfo getData(HttpServletRequest request,
			@RequestParam(value = "nick_name", required = false) String nick_name,
			@RequestParam(value = "start_time", required = false) String start_time,
			@RequestParam(value = "end_time", required = false) String end_time, @ModelAttribute PageBean bean) {

		// 如果开始时间与结束时间为空查询推送时间在前后7天之内的
		if (StringUtil.eqBlank(start_time)) {
			start_time = DateUtil.getPastDate(7);
		}
		if (StringUtil.eqBlank(end_time)) {
			end_time = DateUtil.getFetureDate(7);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("nick_name", nick_name);
		param.put("start_time", start_time);
		param.put("end_time", end_time);
		List<Map<String, Object>> whiteUserViewList = whiteUserService.findWhiteUserView(param, bean);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(whiteUserViewList, 1);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

	@ApiOperation(value = "保存添加白名单", notes = "保存添加白名单API")
	@RequestMapping(value = "/add", method = { RequestMethod.POST })
	public String add(HttpServletRequest request) {
		try {
			String user_name = request.getParameter("user_name");
			String nick_name = request.getParameter("nick_name");
			Map<String, Object> param = new HashMap<>();
			param.put("user_name", user_name);
			param.put("nick_name", nick_name);
			Session session = SecurityUtils.getSubject().getSession();
			User user = (User) session.getAttribute("userSession");
			param.put("operator", user.getName());
			List<Map<String, Object>> whiteUserViewList = whiteUserService.findWhiteUserView(param, null);
			if (whiteUserViewList.size() > 0) {
				return "error";
			} else {
				whiteUserService.insertWhiteUserEntity(param);
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

}
