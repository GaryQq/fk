package com.study.service.lottery.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.mapper.lottery.LotteryMapper;
import com.study.service.lottery.LotteryService;
import com.study.util.datasource.DatabaseContextHolder;
import com.study.util.datasource.DatabaseType;
import com.study.util.tool.StringUtil;

@Service("lotteryService")
public class LotteryServiceImpl implements LotteryService {

	@Autowired
	private LotteryMapper lotteryMapper;

	/** 声明全局变量返回容器 */
	private Map<String, Object> res_map = new HashMap<String, Object>();

	@Override
	public Map<String, Object> findLotteryUsers(Map<String, Object> param) {
		Map<String, Object> user_map = lotteryMapper.findLotteryUsers(param);
		String user_name = StringUtil.nullBlank(user_map.get("USER_NAME"));
		String nick_name = StringUtil.nullBlank(user_map.get("NICK_NAME"));
		String user_real_name = StringUtil.nullBlank(user_map.get("USER_REAL_NAME"));
		String user_mobile = StringUtil.nullBlank(user_map.get("USER_MOBILE"));
		String user_status = StringUtil.nullBlank(user_map.get("USER_STATUS"));
		res_map.put("user_name", user_name);
		res_map.put("nick_name", nick_name);
		res_map.put("user_real_name", user_real_name);
		res_map.put("user_mobile", user_mobile);
		res_map.put("user_status", user_status);
		return res_map;
	}

	@Override
	public void updateLotteryUsers(Map<String, Object> param) {
		lotteryMapper.updateLotteryUsers(param);
	}

}
