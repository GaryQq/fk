package com.study.service.lotterytie.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.mapper.lotterytie.LotterytieMapper;
import com.study.service.lotterytie.LotterytieService;
import com.study.util.tool.StringUtil;

@Service("lotterytieService")
public class LotterytieServiceImpl implements LotterytieService {
	@Autowired
	private LotterytieMapper lotterytieMapper;

	/** 声明全局变量返回容器 */
	private Map<String, Object> res_map = new HashMap<String, Object>();

	/**
	 * 查询caibo用户信息
	 * 
	 * @param param
	 *            user_name 用户账号 nick_name 用户昵称 user_id 用户ID
	 * @return
	 */
	public Map<String, Object> findUser(Map<String, Object> param) {
		Map<String, Object> user_map = lotterytieMapper.findUser(param);
		String user_id = StringUtil.nullBlank(user_map.get("USER_ID"));
		String user_name = StringUtil.nullBlank(user_map.get("USER_NAME"));
		String nick_name = StringUtil.nullBlank(user_map.get("NICK_NAME"));
		res_map.put("user_id", user_id);
		res_map.put("user_name", user_name);
		res_map.put("nick_name", nick_name);
		return res_map;
	}

	@Override
	public void insertChangeRecordsEntity(Map<String, Object> param) {
		lotterytieMapper.insertChangeRecordsEntity(param);
	}

	@Override
	public List<String> findWhiteUserList() {
		List<Map<String, Object>> user_list = lotterytieMapper.findWhiteUserList();
		List<String> res_list = new ArrayList<String>();
		for (Map<String, Object> map : user_list) {
			String user_name = StringUtil.nullBlank(map.get("USERNAME"));
			res_list.add(user_name);
		}
		return res_list;
	}

}
