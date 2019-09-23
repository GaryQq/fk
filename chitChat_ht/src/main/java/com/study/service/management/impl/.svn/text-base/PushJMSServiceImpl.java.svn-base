package com.study.service.management.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.study.mapper.management.PushJMSMapper;
import com.study.service.management.PushJMSService;
import com.study.util.bean.PageBean;
import com.study.util.permissions.PageBeanUtil;

@Service("pushJMSService")
public class PushJMSServiceImpl implements PushJMSService {

	private static Logger logger = LoggerFactory.getLogger(PushJMSServiceImpl.class);

	@Autowired
	private PushJMSMapper pushJMSMapper;

	/** 声明全局变量返回容器 */
	private List<Map<String, Object>> res_list;

	/** 声明全局变量返回容器 */
	private Map<String, Object> res_map;

	@Override
	public List<Map<String, Object>> findPushJmsView(Map<String, Object> param, PageBean bean) {
		if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
			PageHelper.startPage(bean.getPage(), bean.getRows());
		}
		res_list = pushJMSMapper.findPushJmsView(param);
		return res_list;
	}

	@Override
	public Map<String, Object> findPushJMSEntity(Integer id) {
		res_map = pushJMSMapper.findPushJMSEntity(id);
		return res_map;
	}

	@Override
	public int findNickNameUserEntity(String nick_name) {
		int count = pushJMSMapper.findNickNameUserEntity(nick_name);
		return count;
	}

	@Override
	public void insertPushJMSEntity(Map<String, Object> param) {
		pushJMSMapper.insertPushJMSEntity(param);
	}

	@Override
	public void enablePushJMSEntity(Map<String, Object> param) {
		pushJMSMapper.enablePushJMSEntity(param);
	}

}
