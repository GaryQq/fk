package com.study.service.tool.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.study.mapper.tool.ToolMapper;
import com.study.service.tool.ToolService;
import com.study.util.bean.PageBean;
import com.study.util.permissions.PageBeanUtil;

@Service("toolService")
public class ToolServiceImpl implements ToolService {

	@Autowired
	private ToolMapper toolMapper;

	/** 声明全局变量返回容器 */
	private List<Map<String, Object>> res_list;

	@Override
	public List<Map<String, Object>> getSourceInfoList(Map<String, Object> param, PageBean bean) {
		if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
			PageHelper.startPage(bean.getPage(), bean.getRows());
		}
		res_list = toolMapper.getSourceInfoList(param);
		return res_list;
	}

	@Override
	public void insertSourceEntity(Map<String, Object> param) {
		toolMapper.insertSourceEntity(param);
	}

}
