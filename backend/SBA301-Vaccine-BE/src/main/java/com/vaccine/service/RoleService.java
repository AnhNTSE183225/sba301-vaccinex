package com.vaccine.service;

import com.vaccine.entity.Role;
import com.vaccine.enums.EnumRoleName;

import java.util.List;

public interface RoleService {
    Role getRoleByRoleName(EnumRoleName roleName);

    List<Role> getAllRoles();
}
