package com.study.controller.management;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.study.service.management.GainRatioService;
import com.study.util.bean.DataGridResultInfo;
import com.study.util.bean.PageBean;
import com.study.util.permissions.ResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "GainRatio", description = "gainRatio管理API")
@RestController
@RequestMapping("/gainRatio")
public class GainRatioController {
	private static Logger logger = LoggerFactory.getLogger(GainRatioController.class);

	@Autowired
	private GainRatioService gainRatioService;

	@ApiOperation(value = "gainRatio管理", notes = "gainRatio管理API")
	@RequestMapping(value = "/getGainRatioList", method = { RequestMethod.GET })
	public DataGridResultInfo getData(HttpServletRequest request,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "typeCode", required = false) String typeCode,
			@RequestParam(value = "giftType", required = false) String giftType, @ModelAttribute PageBean bean) {
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
		param.put("endTime", endTime);
		param.put("typeCode", typeCode);
		param.put("giftType", giftType);
		List<Map<String, Object>> dailyDataViewList = gainRatioService.findGainRatioList(param, bean);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(dailyDataViewList);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

	@ApiOperation(value = "修改GainRatio", notes = "修改API")
	@RequestMapping(value = "/edit")
	public String edit(HttpServletRequest request) {
		try {
			String gain_ratio_id = request.getParameter("gain_ratio_id");
			String levels = request.getParameter("levels");
			String levela = request.getParameter("levela");
			String levelb = request.getParameter("levelb");
			String levelc = request.getParameter("levelc");
			String common = request.getParameter("common");
			String gold_medal = request.getParameter("gold_medal");
			String silver_medal = request.getParameter("silver_medal");
			String copper_medal = request.getParameter("copper_medal");
			if (StringUtils.isEmpty(gain_ratio_id)) {
				logger.info("修改失败，分成id为空");
				return "fail";
			}

			Map<String, Object> param = new HashMap<>();
			param.put("levels", levels);
			param.put("levela", levela);
			param.put("levelb", levelb);
			param.put("levelc", levelc);
			param.put("common", common);
			param.put("id", gain_ratio_id);
			param.put("gold_medal", gold_medal);
			param.put("silver_medal", silver_medal);
			param.put("copper_medal", copper_medal);
			gainRatioService.updateGainRatio(param);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}
}
