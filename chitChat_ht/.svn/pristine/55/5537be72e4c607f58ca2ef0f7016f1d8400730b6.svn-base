package com.study.service.management.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.study.mapper.management.FinancialMapper;
import com.study.service.management.FinancialService;
import com.study.util.bean.PageBean;
import com.study.util.permissions.PageBeanUtil;

@Service("financialService")
public class FinancialServiceImpl implements FinancialService {

	private static Logger logger = LoggerFactory.getLogger(FinancialServiceImpl.class);

	/**
	 * 声明全局变量返回容器
	 */
	private List<Map<String, Object>> res_list;
	/**
	 * 声明全局变量返回容器
	 */
	private Map<String, Object> res_map;

	@Autowired
	private FinancialMapper financialMapper;

	@Override
	public List<Map<String, Object>> findFinancialData(Map<String, Object> param, PageBean bean) {
		if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
			PageHelper.startPage(bean.getPage(), bean.getRows());
		}
		res_list= financialMapper.findFinancialData(param);
		return res_list;
	}

	@Override
	public List<Map<String, Object>> findFinancialOnlineDetail(Map<String, Object> param, PageBean bean) {
		if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
			PageHelper.startPage(bean.getPage(), bean.getRows());
		}
		res_list= financialMapper.findFinancialOnlineDetail(param);
		return res_list;
	}

	@Override
	public List<Map<String, Object>> findFinancialOfflineDetail(Map<String, Object> param, PageBean bean) {
		if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
			PageHelper.startPage(bean.getPage(), bean.getRows());
		}
		res_list= financialMapper.findFinancialOfflineDetail(param);
		return res_list;
	}

}
