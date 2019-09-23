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

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageInfo;
import com.study.model.permissions.User;
import com.study.service.management.GiftService;
import com.study.util.bean.DataGridResultInfo;
import com.study.util.bean.PageBean;
import com.study.util.permissions.ResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Gift", description = "gift管理API")
@RestController
@RequestMapping("/gift")
public class GiftController {
	private static Logger logger = LoggerFactory.getLogger(GiftController.class);

	@Autowired
	private GiftService giftService;

	@ApiOperation(value = "gift管理", notes = "gift管理API")
	// @ApiImplicitParams({
	// @ApiImplicitParam(name = "name", value = "name", required = false,
	// dataType = "string", paramType = "query"),
	// @ApiImplicitParam(name = "source", value = "source", required = false,
	// dataType = "string", paramType = "query"),
	// @ApiImplicitParam(name = "type", value = "type", required = false,
	// dataType = "string", paramType = "query"),
	// @ApiImplicitParam(name = "operator", value = "operator", required =
	// false, dataType = "string", paramType = "query"),
	// @ApiImplicitParam(name = "gifLocation", value = "gifLocation", required =
	// false, dataType = "string", paramType = "query"),
	// @ApiImplicitParam(name = "status", value = "status", required = false,
	// dataType = "string", paramType = "query")
	// })
	@RequestMapping(value = "/getGiftList", method = { RequestMethod.GET })
	public DataGridResultInfo getData(HttpServletRequest request,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "source", required = false) String source,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "operator", required = false) String operator,
			@RequestParam(value = "gifLocation", required = false) String gifLocation,
			@RequestParam(value = "status", required = false) String status, @ModelAttribute PageBean bean) {
		if (bean.getPage() == null) {
			bean.setPage(1);
		}
		if (bean.getRows() == null) {
			bean.setRows(15);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("page_num", bean.getPage());
		param.put("page_size", bean.getRows());
		param.put("name", name);
		param.put("source", source);
		param.put("type", type);
		param.put("operator", operator);
		param.put("gifLocation", gifLocation);
		param.put("status", status);
		List<Map<String, Object>> dailyDataViewList = giftService.findGiftList1(param, bean);
		// PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String,
		// Object>>(dailyDataViewList, 1);
		// return ResultUtil.createDataGridResult(100, pageInfo.getList());
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(dailyDataViewList);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

	@ApiOperation(value = "添加Gift", notes = "保存添加API")
	@RequestMapping(value = "/add", method = { RequestMethod.POST })
	public String add(HttpServletRequest request) {
		try {
			Session session = SecurityUtils.getSubject().getSession();
			User user = (User) session.getAttribute("userSession");
			String operator = user.getName();

			String type = request.getParameter("type");
			String source = request.getParameter("source");
			String giftName = request.getParameter("giftName");
			String giftPrice = request.getParameter("giftPrice");
			String giftCrystal = request.getParameter("giftCrystal");
			String gifLocation = request.getParameter("giftGifLocation");
			String logoLocation = request.getParameter("giftLogoLocation");
			String live = request.getParameter("live");
			String levels = request.getParameter("levels");
			String levela = request.getParameter("levela");
			String levelb = request.getParameter("levelb");
			String levelc = request.getParameter("levelc");
			String common = request.getParameter("common");

			Map<String, Object> param = new HashMap<>();
			param.put("type", type);
			param.put("source", source);
			param.put("giftName", giftName);
			param.put("giftPrice", giftPrice);
			param.put("giftCrystal", giftCrystal);
			param.put("gifLocation", gifLocation);
			param.put("logoLocation", logoLocation);
			param.put("live", live);
			param.put("levels", levels);
			param.put("levela", levela);
			param.put("levelb", levelb);
			param.put("levelc", levelc);
			param.put("common", common);
			param.put("operator", operator);
			param.put("typeOne", 4);
			giftService.insertGift(param);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "修改Gift", notes = "修改API")
	@RequestMapping(value = "/edit")
	public String edit(HttpServletRequest request) {
		try {
			Session session = SecurityUtils.getSubject().getSession();
			User user = (User) session.getAttribute("userSession");
			String operator = user.getName();

			String type = request.getParameter("type");
			String source = request.getParameter("source");
			String gift_id = request.getParameter("gift_id");
			String giftName = request.getParameter("giftName");
			String giftPrice = request.getParameter("giftPrice");
			String giftCrystal = request.getParameter("giftCrystal");
			String giftLogoLocation = request.getParameter("giftLogoLocation");
			String giftGifLocation = request.getParameter("giftGifLocation");
			String sort = request.getParameter("sort");
			String status = request.getParameter("status");
			if (StringUtils.isEmpty(gift_id)) {
				logger.info("修改失败，礼物id为空");
				return "fail";
			}

			Map<String, Object> param = new HashMap<>();
			param.put("type", type);
			param.put("source", source);
			param.put("giftName", giftName);
			param.put("gift_id", gift_id);
			param.put("giftPrice", giftPrice);
			param.put("giftCrystal", giftCrystal);
			param.put("gifLocation", giftGifLocation);
			param.put("logoLocation", giftLogoLocation);
			param.put("operator", operator);
			param.put("sort", sort);
			param.put("status", status);
			giftService.updateGift(param);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}
}
