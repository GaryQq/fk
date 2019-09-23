package com.study.service.management.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.study.mapper.management.WhiteUserMapper;
import com.study.service.management.WhiteUserService;
import com.study.util.bean.PageBean;
import com.study.util.permissions.PageBeanUtil;

@Service("whiteUserService")
public class WhiteUserServiceImpl implements WhiteUserService {

	private static Logger logger = LoggerFactory.getLogger(WhiteUserServiceImpl.class);

	@Autowired
	private WhiteUserMapper whiteUserMapper;

	/** 声明全局变量返回容器 */
	private List<Map<String, Object>> res_list;

	/** 声明全局变量返回容器 */
	private Map<String, Object> res_map;

	@Override
	public List<Map<String, Object>> findWhiteUserView(Map<String, Object> param, PageBean bean) {
		if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
			PageHelper.startPage(bean.getPage(), bean.getRows());
		}
		res_list = whiteUserMapper.findWhiteUserView(param);
		return res_list;
	}

	@Override
	public void insertWhiteUserEntity(Map<String, Object> param) {
		whiteUserMapper.insertWhiteUserEntity(param);
	}

}
