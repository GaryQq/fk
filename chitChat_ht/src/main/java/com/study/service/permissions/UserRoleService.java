package com.study.service.permissions;

import com.study.model.permissions.UserRole;

public interface UserRoleService extends IService<UserRole> {

    public void addUserRole(UserRole userRole);
}
