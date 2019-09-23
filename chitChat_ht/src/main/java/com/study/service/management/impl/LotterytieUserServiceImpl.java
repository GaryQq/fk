package com.study.service.management.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.mapper.management.LotterytieUserMapper;
import com.study.service.management.LotterytieUserService;

@Service("lotterytieUserService")
public class LotterytieUserServiceImpl implements LotterytieUserService {

	private static Logger logger = LoggerFactory.getLogger(LotterytieUserServiceImpl.class);

	@Autowired
	private LotterytieUserMapper lotterytieUserMapper;
	
	/** 声明全局变量返回容器 */
	private List<Map<String, Object>> res_list;

	/** 声明全局变量返回容器 */
	private Map<String, Object> res_map;

	@Override
	public Map<String, Object> findUserEntity(Map<String, Object> param) {
		res_map = lotterytieUserMapper.findUserEntity(param);
		return res_map;
	}

}
