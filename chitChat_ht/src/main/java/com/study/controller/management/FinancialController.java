package com.study.controller.management;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.study.service.management.BannerService;
import com.study.service.management.FinancialService;
import com.study.util.bean.DataGridResultInfo;
import com.study.util.bean.PageBean;
import com.study.util.permissions.ResultUtil;
import com.study.util.tool.DateUtil;
import com.study.util.tool.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "FinancialController", description = "财务模块")
@RestController
@RequestMapping("/financial")
public class FinancialController {

	private static Logger logger = LoggerFactory.getLogger(FinancialController.class);

	@Autowired
	private FinancialService financialService;
	
	@Autowired
	private BannerService bannerService;

	@ApiImplicitParams({
			@ApiImplicitParam(name = "user_status", value = "充值用户类型:-1去除白名单,0白名单用户,1运营用户", required = false, dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "start_time", value = "充值时间", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "end_time", value = "充值时间", required = false, dataType = "string", paramType = "query") })
	@RequestMapping(value = "/getFinancialData", method = { RequestMethod.GET })
	public DataGridResultInfo getFinancialData(HttpServletRequest request,
			@RequestParam(value = "user_status", required = false) Integer user_status,
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
		param.put("start_time", start_time);
		param.put("end_time", end_time);
		param.put("user_status", user_status);
		List<Map<String, Object>> bannerViewList = financialService.findFinancialData(param, bean);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(bannerViewList, 1);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "user_status", value = "充值用户类型:-1去除白名单,0白名单用户,1运营用户", required = false, dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "pay_type", value = "支付方式:1微信,2支付宝,3易宝支付,4苹果支付", required = false, dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "source", value = "充值渠道", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "app_type", value = "来源类型:1语伴IOS,2语伴Android", required = false, dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "pay_status", value = "支付状态:1成功,2失败", required = false, dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "select_status", value = "查询类型:1明细,2汇总", required = false, dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "nick_name", value = "充值昵称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "start_time", value = "充值时间", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "end_time", value = "充值时间", required = false, dataType = "string", paramType = "query") })
	@RequestMapping(value = "/findFinancialOnlineDetail", method = { RequestMethod.GET })
	public DataGridResultInfo findFinancialOnlineDetail(HttpServletRequest request,
			@RequestParam(value = "user_status", required = false) Integer user_status,
			@RequestParam(value = "pay_type", required = false, defaultValue = "-1") Integer pay_type,
			@RequestParam(value = "source", required = false) String source,
			@RequestParam(value = "app_type", required = false) Integer app_type,
			@RequestParam(value = "pay_status", required = false) Integer pay_status,
			@RequestParam(value = "select_status", required = false) Integer select_status,
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
		param.put("start_time", start_time);
		param.put("end_time", end_time);
		param.put("user_status", user_status);
		if (pay_type == 1) {// 微信支付
			param.put("pay_type", "86, 90, 89, 47");
		} else if (pay_type == 2) {// 支付宝
			param.put("pay_type", "17, 79");
		} else if (pay_type == 3) {// 易宝支付
			param.put("pay_type", "93");
		} else if (pay_type == 4) {// 苹果支付
			param.put("pay_type", "80");
		}
		param.put("source", source);
		param.put("app_type", app_type);
		param.put("pay_status", pay_status);
		param.put("select_status", select_status);
		param.put("nick_name", nick_name);
		List<Map<String, Object>> bannerViewList = financialService.findFinancialOnlineDetail(param, bean);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(bannerViewList, 1);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "user_status", value = "充值用户类型:-1去除白名单,0白名单用户,1运营用户", required = false, dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "select_status", value = "查询类型:1明细,2汇总", required = false, dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "nick_name", value = "充值昵称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "start_time", value = "充值时间", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "end_time", value = "充值时间", required = false, dataType = "string", paramType = "query") })
	@RequestMapping(value = "/findFinancialOfflineDetail", method = { RequestMethod.GET })
	public DataGridResultInfo findFinancialOfflineDetail(HttpServletRequest request,
			@RequestParam(value = "user_status", required = false) Integer user_status,
			@RequestParam(value = "select_status", required = false) Integer select_status,
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
		param.put("start_time", start_time);
		param.put("end_time", end_time);
		param.put("user_status", user_status);
		param.put("select_status", select_status);
		param.put("nick_name", nick_name);
		List<Map<String, Object>> bannerViewList = financialService.findFinancialOfflineDetail(param, bean);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(bannerViewList, 1);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}
	
	@RequestMapping(value = "/yubanWithDrawList", method = { RequestMethod.GET })
	public DataGridResultInfo yubanWithDrawList(HttpServletRequest request,
			@RequestParam(value = "order_status", required = false) Integer order_status,
			@RequestParam(value = "start_time", required = false) String start_time,
			@RequestParam(value = "end_time", required = false) String end_time, @ModelAttribute PageBean bean) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("start_time", start_time);
		param.put("end_time", end_time);
		param.put("order_status", order_status);
		List<Map<String, Object>> bannerViewList = bannerService.findYubanWithDrawsDetail(param, bean);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(bannerViewList,1);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}
	
	@ApiOperation(value = "更改订单状态", notes = "根据订单号更改订单的状态")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "status", value = "状态", required = true, dataType = "string", paramType = "query")})
	@RequestMapping(value = "/updateTxStatus", method = { RequestMethod.DELETE})
	public String delete(@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "status", required = true) String status) {
		try {
			if (id != null && status!=null) {
				bannerService.updateTxStatus(id,status);
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}
	
	@RequestMapping(value = "/exportOrderWithDrawList", method = { RequestMethod.GET })
	public void exportOrderWithDrawList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "order_status", required = false) Integer order_status,
			@RequestParam(value = "start_time", required = false) String start_time,
			@RequestParam(value = "end_time", required = false) String end_time, @ModelAttribute PageBean bean) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("start_time", start_time);
		param.put("end_time", end_time);
		param.put("order_status", order_status+"");
		Map<String, Object> bannerViewList = bannerService.exportWithDrawsDetail(param);
		//执行条件
		if (!bannerViewList.isEmpty()) {
					
			//获取数据
			HSSFWorkbook hwb = (HSSFWorkbook)bannerViewList.get("content");
			String file_name = (String)bannerViewList.get("file_name");
					
			//设置response对象
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" + file_name);
					
			//输出流
			OutputStream out;
			try {
				out = response.getOutputStream();
				hwb.write(out);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
