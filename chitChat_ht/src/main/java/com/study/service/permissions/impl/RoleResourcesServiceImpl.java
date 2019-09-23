package com.study.service.permissions.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.study.mapper.permissions.UserRoleMapper;
import com.study.model.permissions.RoleResources;
import com.study.service.permissions.RoleResourcesService;
import com.study.shiro.MyShiroRealm;

import tk.mybatis.mapper.entity.Example;

@Service("roleResourcesService")
public class RoleResourcesServiceImpl extends BaseService<RoleResources> implements RoleResourcesService {
	@Resource
	private UserRoleMapper userRoleMapper;

	@Autowired
	private MyShiroRealm myShiroRealm;

	@Override
	// 更新权限
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
	public void addRoleResources(RoleResources roleResources) {
		// 删除
		Example example = new Example(RoleResources.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("roleid", roleResources.getRoleid());
		mapper.deleteByExample(example);
		// 添加
		if (!StringUtils.isEmpty(roleResources.getResourcesid())) {
			String[] resourcesArr = roleResources.getResourcesid().split(",");
			for (String resourcesId : resourcesArr) {
				RoleResources r = new RoleResources();
				r.setRoleid(roleResources.getRoleid());
				r.setResourcesid(resourcesId);
				mapper.insert(r);
			}
		}

		List<Integer> userIds = userRoleMapper.findUserIdByRoleId(roleResources.getRoleid());
		// 更新当前登录的用户的权限缓存
		myShiroRealm.clearUserAuthByUserId(userIds);

	}
}
