package com.study.service.management.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.study.mapper.management.AgencyMapper;
import com.study.service.management.AgencyService;
import com.study.util.bean.PageBean;
import com.study.util.permissions.PageBeanUtil;

@Service("agencyService")
public class AgencyServiceImpl implements AgencyService {

	private static Logger logger = LoggerFactory.getLogger(AgencyServiceImpl.class);

	@Autowired
	private AgencyMapper agencyMapper;
	
	/** 声明全局变量返回容器 */
	private List<Map<String, Object>> res_list;

	/** 声明全局变量返回容器 */
	private Map<String, Object> res_map;

	@Override
	public List<Map<String, Object>> findAgencyIncomeList(Map<String, Object> param, PageBean bean) {
		if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
			PageHelper.startPage(bean.getPage(), bean.getRows());
		}
		res_list = agencyMapper.findAgencyIncomeList(param);
		return res_list;
	}
}
