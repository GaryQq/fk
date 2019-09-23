package com.study.service.permissions;

import java.util.List;
import java.util.Map;

import com.study.model.permissions.User;
import com.study.util.bean.PageBean;

public interface UserService extends IService<User> {
	public List<User> selectUserByDeptId(Map<String, Object> map, PageBean bean);

	User selectByUsername(String username);

	void delUser(Integer userid);

	public void insertUser(User user);

}
