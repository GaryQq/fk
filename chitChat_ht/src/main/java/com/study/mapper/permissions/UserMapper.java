package com.study.mapper.permissions;

import java.util.List;
import java.util.Map;

import com.study.model.permissions.User;
import com.study.util.permissions.MyMapper;

public interface UserMapper extends MyMapper<User> {

	public List<User> selectUserByDeptId(Map<String, Object> map);

	public void insertUser(User user);
}