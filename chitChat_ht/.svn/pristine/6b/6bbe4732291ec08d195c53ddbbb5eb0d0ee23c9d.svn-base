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

import com.study.service.management.GainRatioService;
import com.study.util.bean.MenuBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "GainRatioViewController", description = "gainRatio管理API")
@Controller
public class GainRatioViewController {
	private static Logger logger = LoggerFactory.getLogger(GainRatioViewController.class);

	@Autowired
	private GainRatioService gainRatioService;

	private Map<String, Object> res_map;

	@ApiOperation(value = "跳转添加、修改", notes = "跳转添加、修改")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "gainRatioId", required = false, dataType = "int", paramType = "query") })
	@RequestMapping(value = "/gainRatioView/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request, @RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "from", required = false) String from, @ModelAttribute MenuBean bean) {
		request.setAttribute("menu", bean);
		if (id != null) {
			res_map = gainRatioService.findGainRatioEntity(id);
			logger.info("GainRatioViewController============>id:" + id + "--map:" + res_map);
			request.setAttribute("entity", res_map);
			String retValue = "";
			if ("1".equals(from)) {
				retValue = "gainRatio/gainRatio1/gainRatio1_edit";
			} else if ("2".equals(from)) {
				retValue = "gainRatio/gainRatio2/gainRatio2_edit";
			} else if ("3".equals(from)) {
				retValue = "gainRatio/gainRatio3/gainRatio3_edit";
			}
			return retValue;
		} else {
			return "gainRatio/gainRatio1/gainRatio_add";
		}
	}
}
