package com.study.service.permissions.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.study.mapper.permissions.RoleMapper;
import com.study.mapper.permissions.RoleResourcesMapper;
import com.study.model.permissions.Role;
import com.study.model.permissions.RoleResources;
import com.study.service.permissions.RoleService;
import com.study.util.bean.PageBean;
import com.study.util.permissions.PageBeanUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("roleService")
public class RoleServiceImpl extends BaseService<Role> implements RoleService {

	@Resource
	private RoleMapper roleMapper;
	@Resource
	private RoleResourcesMapper roleResourcesMapper;

	@Override
	public Role queryRoleListWithSelected(Integer uid) {
		return roleMapper.queryRoleListWithSelected(uid);
	}

	@Override
	public List<Role> selectByPage(Role role, PageBean bean) {
		Example example = new Example(Role.class);
		if (role != null && role.getRoledesc() != null && !"".equals(role.getRoledesc().trim())) {
			Criteria createCriteria = example.createCriteria();
			createCriteria.andLike("roledesc", "%" + role.getRoledesc() + "%");
		}
		if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
			// 分页查询
			PageHelper.startPage(bean.getPage(), bean.getRows());
		}
		List<Role> rolesList = selectByExample(example);
		return rolesList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
	public void delRole(Integer roleid) {
		// 删除角色
		mapper.deleteByPrimaryKey(roleid);
		// 删除角色资源
		Example example = new Example(RoleResources.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("roleid", roleid);
		roleResourcesMapper.deleteByExample(example);

	}

	@Override
	public void insertRole(Role role) {
		roleMapper.insertRole(role);
	}
}
