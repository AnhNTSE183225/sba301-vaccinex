package com.sba301.vaccinex.repository;

import com.sba301.vaccinex.pojo.Role;
import com.sba301.vaccinex.pojo.enums.EnumRoleNameType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role getRoleByRoleName(EnumRoleNameType role);

    Role getRolesById(int roleID);

}
