package com.study.service.management;

import java.util.List;
import java.util.Map;

import com.study.util.bean.PageBean;

/**
 * banner管理
 * 
 * @author Administrator
 *
 */
public interface BannerService {

	List<Map<String, Object>> findBannerView(Map<String, Object> param, PageBean bean);

	Map<String, Object> findBannerEntity(Integer id);

	void insertBannerEntity(Map<String, Object> param);

	void deleteBannerEntity(int parseInt);

	void updateBannerEntity(Map<String, Object> param);

	void enableBannerEntity(Map<String, Object> param);
	
	/**
	 * 查询提现列表
	 * 
	 * @param param
	 * @param bean
	 * @return
	 */
	List<Map<String, Object>> findYubanWithDrawsDetail(Map<String, Object> param, PageBean bean);
	
	public void updateTxStatus(String id,String status);
	
	/**
	 * 查询提现列表
	 * 
	 * @param param
	 * @param bean
	 * @return
	 */
	Map<String, Object> exportWithDrawsDetail(Map<String, Object> param);


}
