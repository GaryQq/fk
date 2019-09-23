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
import com.study.service.management.PushJMSService;
import com.study.util.bean.DataGridResultInfo;
import com.study.util.bean.PageBean;
import com.study.util.permissions.ResultUtil;
import com.study.util.tool.DateUtil;
import com.study.util.tool.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "PushJMS", description = "语伴推送管理API")
@RestController
@RequestMapping("/pushJMS")
public class PushJMSController {
	private static Logger logger = LoggerFactory.getLogger(PushJMSController.class);

	@Autowired
	private PushJMSService pushJMSService;

	@ApiOperation(value = "语伴推送管理", notes = "语伴推送管理API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "推送ID", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "title", value = "title", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "start_time", value = "推送开始时间", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "end_time", value = "推送结束时间", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "jms_type", value = "推送类型", required = false, dataType = "string", paramType = "query") })
	@RequestMapping(value = "/getData", method = { RequestMethod.GET })
	public DataGridResultInfo getData(HttpServletRequest request,
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "start_time", required = false) String start_time,
			@RequestParam(value = "end_time", required = false) String end_time,
			@RequestParam(value = "jms_type", required = false) String jms_type, @ModelAttribute PageBean bean) {

		// 如果开始时间与结束时间为空查询推送时间在前后7天之内的
		if (StringUtil.eqBlank(start_time)) {
			start_time = DateUtil.getPastDate(7);
		}
		if (StringUtil.eqBlank(end_time)) {
			end_time = DateUtil.getFetureDate(7);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("title", title);
		param.put("jms_type", jms_type);
		param.put("start_time", start_time);
		param.put("end_time", end_time);
		param.put("jms_type", jms_type);
		List<Map<String, Object>> pushJMSViewList = pushJMSService.findPushJmsView(param, bean);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(pushJMSViewList, 1);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

	@ApiOperation(value = "根据昵称查询用户", notes = "根据昵称查询用户API")
	@RequestMapping(value = "/searchNickName", method = { RequestMethod.POST })
	public String searchNickName(HttpServletRequest request) {
		try {
			String nick_name = request.getParameter("nick_name");
			int i = pushJMSService.findNickNameUserEntity(nick_name);
			if (i > 0) {
				return "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "fail";
	}

	@ApiOperation(value = "保存添加语伴推送", notes = "保存添加语伴推送API")
	@RequestMapping(value = "/add", method = { RequestMethod.POST })
	public String add(HttpServletRequest request) {
		try {
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			String jms_type = request.getParameter("jms_type");
			String jms_type_url = request.getParameter("jms_type_url");
			String app_type = request.getParameter("app_type");
			String user_type = request.getParameter("user_type");
			String sex_type = request.getParameter("sex_type");
			String type = request.getParameter("type");
			String jms_date = request.getParameter("jms_date");
			String user_type_name = request.getParameter("user_type_name");

			Map<String, Object> param = new HashMap<>();
			param.put("title", title);
			param.put("content", content);
			param.put("jms_type", jms_type);
			param.put("jms_type_url", jms_type_url);
			param.put("app_type", app_type);
			param.put("user_type", user_type);
			param.put("sex_type", sex_type);
			param.put("type", type);
			param.put("jms_date", jms_date);
			param.put("user_type_name", user_type_name);
			Session session = SecurityUtils.getSubject().getSession();
			User user = (User) session.getAttribute("userSession");
			param.put("operator", user.getName());
			pushJMSService.insertPushJMSEntity(param);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "修改推送上线下线", notes = "修改推送上线下线API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ids", value = "bannerid", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "enable", value = "banner状态", required = true, dataType = "string", paramType = "query") })
	@RequestMapping(value = "enable", method = { RequestMethod.GET })
	public String enable(HttpServletRequest request, @RequestParam(value = "ids") String ids,
			@RequestParam(value = "enable") Integer enable) {
		try {
			if (ids != null) {
				String[] idsArry = ids.split(",");
				Map<String, Object> param = new HashMap<>();
				param.put("enable", enable);
				Session session = SecurityUtils.getSubject().getSession();
				User user = (User) session.getAttribute("userSession");
				param.put("operator", user.getName());
				for (String id : idsArry) {
					param.put("id", id);
					pushJMSService.enablePushJMSEntity(param);
				}
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}
}
