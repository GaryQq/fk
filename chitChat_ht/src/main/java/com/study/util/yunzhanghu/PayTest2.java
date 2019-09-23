package com.study.util.yunzhanghu;

import java.util.HashMap;
import java.util.Map;

public class PayTest2 {
	public static void main(String[] args) throws Exception { 
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("order_id", "15419224106531139");
		String result = PayYunZhangHuUtil.findPayOrderDetail(param);
		System.out.println(result);
	}
}
