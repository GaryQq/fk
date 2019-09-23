package com.study.service.fengliaoAgency;

import com.study.util.bean.PageBean;

import java.util.List;
import java.util.Map;

public interface FengLiaoAgencyService {

	/**
	 * 查询代理渠道下注册用户
	 * 
	 * @param param
	 * @param bean
	 * @return
	 */
	List<Map<String, Object>> findUserRegisterList(Map<String, Object> param, PageBean bean);

	/**
	 * 查询代理渠道下注册用户充值明细
	 * 
	 * @param param
	 * @param bean
	 * @return
	 */
	List<Map<String, Object>> findUserPayDetailList(Map<String, Object> param, PageBean bean);

	/**
	 * 查询用户充值合计
	 * 
	 * @param param
	 * @return
	 */
	int findUserPaySumInfo(Map<String, Object> param);

	/**
	 * 查询代理提现记录
	 * 
	 * @param param
	 * @param bean
	 * @return
	 */
	List<Map<String, Object>> findAgencyPayDetailList(Map<String, Object> param, PageBean bean);

	/**
	 * 查询代理提现金额合计
	 * 
	 * @param param
	 * @return
	 */
	int findAgencyPaySumInfo(Map<String, Object> param);

	/**
	 * 查询代理累计收入
	 * 
	 * @param param
	 * @return
	 */
	Double findAgencyPayIncome(Map<String, Object> param);

	/**
	 * 查询代理账户余额
	 * 
	 * @param param
	 * @return
	 */
	Double findAgencyPayBalance(Map<String, Object> param);

	/**
	 * 查询代理当前提成比例
	 * 
	 * @param param
	 * @return
	 */
	String findAgencyPayRatio(Map<String, Object> param);

	/**
	 * 代理提现明细
	 * 
	 * @param param
	 */
	void saveAgencyPayDetail(Map<String, Object> param);

	/**
	 * 修改代理账户
	 * 
	 * @param param
	 */
	void updateAgencyAccount(Map<String, Object> param);

	/**
	 * 添加代理提现明细
	 * 
	 * @param param
	 */
	void saveAgencyAccountDetail(Map<String, Object> param);

}
