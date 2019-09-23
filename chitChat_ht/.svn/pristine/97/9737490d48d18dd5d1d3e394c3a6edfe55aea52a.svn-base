package com.study.controller.management.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.service.management.GiftService;
import com.study.util.bean.MenuBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "GiftViewController", description = "gift管理API")
@Controller
public class GiftViewController {
	private static Logger logger = LoggerFactory.getLogger(GiftViewController.class);

	@Autowired
	private GiftService giftService;

	private Map<String, Object> res_map;

	@ApiOperation(value = "跳转添加、修改", notes = "跳转添加、修改")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "giftId", required = false, dataType = "int", paramType = "query") })
	@RequestMapping(value = "/giftView/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request, @RequestParam(value = "id", required = false) Integer id,
			@ModelAttribute MenuBean bean) {
		request.setAttribute("menu", bean);
		if (id != null) {
			res_map = giftService.findGiftEntity(id);
			logger.info("GiftViewController============>id:" + id + "--map:" + res_map);
			request.setAttribute("entity", res_map);
			return "operation/gift/gift_edit";
		} else {
			return "operation/gift/gift_add";
		}
	}
}
