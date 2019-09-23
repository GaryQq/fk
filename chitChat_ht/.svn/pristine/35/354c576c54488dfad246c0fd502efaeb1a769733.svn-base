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
import com.study.service.management.BannerService;
import com.study.util.bean.DataGridResultInfo;
import com.study.util.bean.PageBean;
import com.study.util.permissions.ResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "Banner", description = "banner管理API")
@RestController
@RequestMapping("/banner")
public class BannerController {
	private static Logger logger = LoggerFactory.getLogger(BannerController.class);

	@Autowired
	private BannerService bannerService;

	@ApiOperation(value = "banner管理", notes = "banner管理API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "bannerid", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "title", value = "title", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "operator", value = "操作人", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "status", value = "banner状态", required = false, dataType = "string", paramType = "query") })
	@RequestMapping(value = "/getData", method = { RequestMethod.GET })
	public DataGridResultInfo getData(HttpServletRequest request,
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "operator", required = false) String operator,
			@RequestParam(value = "status", required = false) String status, @ModelAttribute PageBean bean) {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("title", title);
		param.put("operator", operator);
		param.put("status", status);
		List<Map<String, Object>> bannerViewList = bannerService.findBannerView(param, bean);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(bannerViewList, 1);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

	@ApiOperation(value = "添加Banner", notes = "保存添加API")
	@RequestMapping(value = "/add", method = { RequestMethod.POST })
	public String add(HttpServletRequest request) {
		try {
			String status = request.getParameter("status");
			String type = request.getParameter("type");// 轮播图显示位置
			String title = request.getParameter("title");// banner标题
			String picUrl = request.getParameter("picUrl");// banner图片地址
			String typeId = request.getParameter("typeId");// 跳转类型
			String h5Url = request.getParameter("H5Url");// 跳转到h5的URL
			String homepage = request.getParameter("homepage");// 个人主页 userid
			String shareContent = request.getParameter("shareContent");// 分享内容
			String shareImg = request.getParameter("shareImg");// 分享图片
			String startTime = request.getParameter("startTime");// 开始时间
			String stopTime = request.getParameter("endTime");// 结束时间
			String eventDayTime = request.getParameter("eventDayTime");// 时间段
			String week = request.getParameter("week");// 时间段
			String open_sid = request.getParameter("open_sid");// 展示渠道
			String close_sid = request.getParameter("close_sid");// 屏蔽渠道
			String client_type = request.getParameter("myselect");// 客户端
			String weight = request.getParameter("weight");// 权重
			Map<String, Object> param = new HashMap<>();
			param.put("status", status);
			param.put("type", type);
			param.put("title", title);
			param.put("picUrl", picUrl);
			param.put("typeId", typeId);
			param.put("h5Url", h5Url);
			param.put("homepage", homepage);
			param.put("shareContent", shareContent);
			param.put("shareImg", shareImg);
			param.put("startTime", startTime);
			param.put("stopTime", stopTime);
			param.put("eventDayTime", eventDayTime);
			param.put("week", week);
			param.put("open_sid", open_sid);
			param.put("close_sid", close_sid);
			param.put("client_type", client_type);
			param.put("weight", weight);
			Session session = SecurityUtils.getSubject().getSession();
			User user = (User) session.getAttribute("userSession");
			param.put("operator", user.getName());
			bannerService.insertBannerEntity(param);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "删除Banner", notes = "根据id删除API")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ids", value = "bannerid", required = true, dataType = "string", paramType = "query") })
	@RequestMapping(value = "delete", method = { RequestMethod.DELETE })
	public String delete(HttpServletRequest request, @RequestParam(value = "ids") String ids) {
		try {
			if (ids != null) {
				String[] idsArry = ids.split(",");
				for (String id : idsArry) {
					bannerService.deleteBannerEntity(Integer.parseInt(id));
				}
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "banner修改", notes = "保存修改banner")
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(HttpServletRequest request) {
		try {
			String id = request.getParameter("id");
			String type = request.getParameter("type");// 轮播图显示位置
			String title = request.getParameter("title");// banner标题
			String picUrl = request.getParameter("picUrl");// banner图片地址
			String typeId = request.getParameter("typeId");// 跳转类型
			String h5Url = request.getParameter("H5Url");// 跳转到h5的URL
			String homepage = request.getParameter("homepage");// 个人主页 userid
			String shareContent = request.getParameter("shareContent");// 分享内容
			String shareImg = request.getParameter("shareImg");// 分享图片
			String startTime = request.getParameter("startTime");// 开始时间
			String stopTime = request.getParameter("endTime");// 结束时间
			String eventDayTime = request.getParameter("eventDayTime");// 时间段
			String week = request.getParameter("week");// 时间段
			String open_sid = request.getParameter("open_sid");// 展示渠道
			String close_sid = request.getParameter("close_sid");// 屏蔽渠道
			String client_type = request.getParameter("myselect");// 客户端
			String weight = request.getParameter("weight");// 权重
			Map<String, Object> param = new HashMap<>();
			param.put("id", id);
			param.put("type", type);
			param.put("title", title);
			param.put("picUrl", picUrl);
			param.put("typeId", typeId);
			param.put("h5Url", h5Url);
			param.put("homepage", homepage);
			param.put("shareContent", shareContent);
			param.put("shareImg", shareImg);
			param.put("startTime", startTime);
			param.put("stopTime", stopTime);
			param.put("eventDayTime", eventDayTime);
			param.put("week", week);
			param.put("open_sid", open_sid);
			param.put("close_sid", close_sid);
			param.put("client_type", client_type);
			param.put("weight", weight);
			Session session = SecurityUtils.getSubject().getSession();
			User user = (User) session.getAttribute("userSession");
			param.put("operator", user.getName());
			logger.info("updateBannerEntity=====>param:" + param);
			bannerService.updateBannerEntity(param);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "修改banner上线下线", notes = "修改banner上线下线API")
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
					logger.info("enableBannerEntity=====>param:" + param);
					bannerService.enableBannerEntity(param);
				}
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

}
