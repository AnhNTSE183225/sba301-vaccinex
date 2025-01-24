package com.sba301.vaccinex.service;

import com.sba301.vaccinex.exception.ElementNotFoundException;
import com.sba301.vaccinex.pojo.Role;
import com.sba301.vaccinex.pojo.enums.EnumRoleNameType;
import com.sba301.vaccinex.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByRoleName(EnumRoleNameType roleName) {
        Role role = roleRepository.getRoleByRoleName(roleName);
        if (role == null) {
            throw new ElementNotFoundException("Role not found");
        }
        return role;
    }

    @Override
    public Role getRoleByRoleId(int roleId) {
        Role role = roleRepository.getRolesById(roleId);
        if (role == null) {
            throw new ElementNotFoundException("Role not found");
        }
        return role;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
