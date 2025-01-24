package com.sba301.vaccinex.service;


import com.sba301.vaccinex.pojo.Role;
import com.sba301.vaccinex.pojo.enums.EnumRoleNameType;

import java.util.List;

public interface RoleService {
    Role getRoleByRoleName(EnumRoleNameType roleName);

    Role getRoleByRoleId(int roleId);

    List<Role> getAllRoles();
}
