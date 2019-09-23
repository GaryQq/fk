package com.study.service.management;

import java.util.List;
import java.util.Map;

import com.study.util.bean.PageBean;

/**
 * 代理是否过审
 * 
 * @author Administrator
 *
 */
public interface AuditManagementService {
	/**
	 * 申请代理列表
	 * 
	 * @param param
	 * @param bean
	 * @return
	 */
	List<Map<String, Object>> findAuditManagementView(Map<String, Object> param, PageBean bean);

	/**
	 * 申请代理审核
	 * 
	 * @param param
	 */
	void enableUserAgentEntity(Map<String, Object> param);

	/**
	 * 申请代理信息查询
	 * 
	 * @param user_name
	 * @return
	 */
	Map<String, Object> findAuditManagementEntity(String user_name);

	/**
	 * 代理列表
	 * 
	 * @param param
	 * @param bean
	 * @return
	 */
	List<Map<String, Object>> findAuditView(Map<String, Object> param, PageBean bean);

	/**
	 * 根据账号查询代理信息
	 * 
	 * @param user_name
	 * @return
	 */
	Map<String, Object> findAuditEntity(String user_name);

	/**
	 * 添加代理
	 * 
	 * @param param
	 */
	void insertAuditEntity(Map<String, Object> param);

	/**
	 * 根据代理ID查询代理信息
	 * 
	 * @param audit_id
	 * @return
	 */
	Map<String, Object> findAuditByIdEntity(Integer audit_id);

	/**
	 * 根据代理ID修改代理信息
	 * 
	 * @param param
	 */
	void updateAuditEntity(Map<String, Object> param);

	/**
	 * 查询代理旗下老师列表
	 * 
	 * @param param
	 * @param bean
	 * @return
	 */
	List<Map<String, Object>> findAuditTeacherView(Map<String, Object> param, PageBean bean);

}
