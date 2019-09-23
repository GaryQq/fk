package com.study.service.permissions.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.study.mapper.permissions.UserMapper;
import com.study.mapper.permissions.UserRoleMapper;
import com.study.model.permissions.User;
import com.study.model.permissions.UserRole;
import com.study.service.permissions.UserService;
import com.study.util.bean.PageBean;
import com.study.util.permissions.PageBeanUtil;
import com.study.util.tool.StringUtil;

import tk.mybatis.mapper.entity.Example;

@Service("userService")
public class UserServiceImpl extends BaseService<User> implements UserService {
	@Resource
	private UserRoleMapper userRoleMapper;
	@Resource
	private UserMapper userMapper;

	@Override
	public User selectByUsername(String username) {
		Example example = new Example(User.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("username", username);
		List<User> userList = selectByExample(example);
		if (userList.size() > 0) {
			return userList.get(0);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
	public void delUser(Integer userid) {
		// 删除用户表
		mapper.deleteByPrimaryKey(userid);
		// 删除用户角色表
		Example example = new Example(UserRole.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("userid", userid);
		userRoleMapper.deleteByExample(example);
	}

	@Override
	public List<User> selectUserByDeptId(Map<String, Object> map, PageBean bean) {
		if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
			PageHelper.startPage(bean.getPage(), bean.getRows());
		}
		List<User> selectUserByDeptId = userMapper.selectUserByDeptId(map);
		return selectUserByDeptId;
	}

	@Override
	public void insertUser(User user) {
		userMapper.insertUser(user);
	}
}
