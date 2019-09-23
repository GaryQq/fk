package com.study.mapper.management;

import java.util.List;
import java.util.Map;

public interface BannerMapper {

	public List<Map<String, Object>> findBannerView(Map<String, Object> param);

	public Map<String, Object> findBannerEntity(Integer id);

	public void insertBannerEntity(Map<String, Object> param);

	public void deleteBannerEntity(int id);

	public void updateBannerEntity(Map<String, Object> param);

	public void enableBannerEntity(Map<String, Object> param);
	
	public List<Map<String, Object>> findYubanWithDrawsDetail(Map<String, Object> param);
	
	public Map<String,Object> getTxAmount(int id);
	
	public double getUserAccount(String user_name);
	
	public void updateUserAccount(Map<String, Object> param);
	
	public void insertUserDetail(Map<String, Object> param);
	
	public void updateTxOrderStatus(Map<String, Object> param);

}
