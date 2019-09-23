package com.study.service.management;

import java.util.List;
import java.util.Map;

import com.study.util.bean.PageBean;

/**
 * 代理相关service
 * 
 * @author Administrator
 *
 */
public interface AgencyService {

	/**
	 * 查询代理收入记录
	 * 
	 * @param param
	 * @param bean
	 * @return
	 */
	List<Map<String, Object>> findAgencyIncomeList(Map<String, Object> param, PageBean bean);
}
