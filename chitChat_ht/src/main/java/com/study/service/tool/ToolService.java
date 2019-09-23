package com.study.service.tool;

import java.util.List;
import java.util.Map;

import com.study.util.bean.PageBean;

public interface ToolService {
	/**
	 * 渠道绑定列表
	 * 
	 * @param param
	 * @param bean
	 * @return
	 */
	List<Map<String, Object>> getSourceInfoList(Map<String, Object> param, PageBean bean);

	/**
	 * 保存添加渠道绑定
	 * 
	 * @param param
	 */
	void insertSourceEntity(Map<String, Object> param);

}
