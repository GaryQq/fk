package com.study.controller.pay;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.study.util.yunzhanghu.PayYunZhangHuUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "PayYunZhangHuController", description = "云账户API")
@RestController
@RequestMapping("/payYunZhangHu")
public class PayYunZhangHuController {
	private static Logger logger = LoggerFactory.getLogger(PayYunZhangHuController.class);

	@ApiOperation(value = "账户提现", notes = "云账户提现API")
	@RequestMapping(value = "/payAccountTiXian", method = { RequestMethod.POST })
	public String payAccountTiXian(HttpServletRequest request) {
		try {
			String order_id = request.getParameter("order_id");// 订单ID
			String user_id = request.getParameter("user_id");// 用户唯一标识
			String real_name = request.getParameter("real_name");// 银行开户人
			String card_no = request.getParameter("card_no");// 银行卡号
			String id_card = request.getParameter("id_card");// 开户人身份证
			String pay = request.getParameter("pay");// 提现金额(元),最小0.1元
			String app_type = request.getParameter("app_type");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("order_id", order_id);
			param.put("user_id", user_id);
			param.put("real_name", real_name);
			param.put("card_no", card_no);
			param.put("id_card", id_card);
			param.put("pay", pay);
			param.put("app_type", app_type);
			logger.info("payAccountTiXian======>param:" + param);
			String result = PayYunZhangHuUtil.payAccountTiXian(param);
			logger.info("payAccountTiXian======>result:" + result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "账户提现记录", notes = "云账户提现API")
	@RequestMapping(value = "/findPayAccountTiXianDetail", method = { RequestMethod.POST })
	public String findPayAccountTiXianDetail(HttpServletRequest request) {
		try {
			String order_id = request.getParameter("order_id");// 订单ID
			String app_type = request.getParameter("app_type");//
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("order_id", order_id);
			param.put("app_type", app_type);
			String result = PayYunZhangHuUtil.findPayOrderDetail(param);
			JSONObject json = new JSONObject().parseObject(result);
			String code = json.getString("code");
			if (!"".equals(app_type)) {
				return result;
			} else if ("0000".equals(code)) {
				JSONObject data = json.getJSONObject("data");
				String status = data.getString("status");
				String status_message = data.getString("status_message");
				JSONObject res_json = new JSONObject();
				res_json.put("code", status);
				res_json.put("message", status_message);
				logger.info("findPayAccountTiXianDetail======>order_id:" + order_id + "--res_json:" + json.toString());
				return res_json.toString();
			}
			logger.info("findPayAccountTiXianDetail======>order_id:" + order_id + "--res_json:" + json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "fail";
	}

	@ApiOperation(value = "循环查询云账户支付单据情况", notes = "云账户提现API")
	@RequestMapping(value = "/findPayTiXianDetail", method = { RequestMethod.GET })
	public String findPayTiXianDetail(HttpServletRequest request) {
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			String result = PayYunZhangHuUtil.findPayTiXianDetail(param);
			logger.info("findPayTiXianDetail======>result:" + result);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "fail";
	}
	
	public static void main(String[] args) {
		Map<String, Object> param = new HashMap<String, Object>();
		String result = PayYunZhangHuUtil.findPayTiXianDetail(param);
		System.out.println(result);
	}
}
