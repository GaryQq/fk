package com.study.service.management.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.study.mapper.management.OpinionFeedbackMapper;
import com.study.service.management.OpinionFeedbackService;
import com.study.util.bean.PageBean;
import com.study.util.permissions.PageBeanUtil;

@Service("opinionFeedbackService")
public class OpinionFeedbackServiceImpl implements OpinionFeedbackService {

	private static Logger logger = LoggerFactory.getLogger(OpinionFeedbackServiceImpl.class);

	/** 声明全局变量返回容器 */
	private List<Map<String, Object>> res_list;

	/** 声明全局变量返回容器 */
	private Map<String, Object> res_map;

	@Autowired
	private OpinionFeedbackMapper opinionFeedbackMapper;

	@Override
	public List<Map<String, Object>> findFeedbackData(Map<String, Object> param, PageBean bean) {
		if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
			PageHelper.startPage(bean.getPage(), bean.getRows());
		}
		res_list = opinionFeedbackMapper.findFeedbackData(param);
		return res_list;
	}

}
